package BattleRoyal;

import Doctrina.Canvas;
import Doctrina.Direction;
import Doctrina.CollidableRepository;
import Doctrina.StaticEntity;

import java.awt.*;
import java.util.Random;
import java.util.Scanner;

public class Enemy extends Entity {
    private static final int ENEMY_SIZE = 32;
    private static final int ENEMY_SPEED = 2;
    private static final int DETECTION_RADIUS = 300; // Radius within which the enemy can detect other entities
    private static final int SHOOTING_RANGE = 200; // Range within which the enemy will start shooting
    private static final int SHOOT_INTERVAL = 60; // Frames between shots

    private Random random;

    private Entity targetEntity; // The current target of the enemy
    private int shootCooldown; // Cooldown timer for shooting

    public Enemy(World world) {
        super(world, ENEMY_SIZE, ENEMY_SIZE, ENEMY_SPEED);
        this.world = world;
        this.shootCooldown = 0;
        this.random = new Random();
    }

    @Override
    public void update() {
        super.update();

        if (!isAlive) {
            return; // Skip updating if the enemy is dead
        }

        // Acquire a target if we don't have one or if the current target is dead
        if (targetEntity == null || !targetEntity.isAlive()) {
            targetEntity = findNearestEntity();
        }

        if (targetEntity != null) {
            // Move towards the target
            moveTowardsTarget();

            // Decrease shooting cooldown
            if (shootCooldown > 0) {
                shootCooldown--;
            }

            // Check if the target is within shooting range
            if (isWithinShootingRange(targetEntity) && shootCooldown <= 0) {
                // Shoot at the target
                shootAtTarget();
                shootCooldown = SHOOT_INTERVAL; // Reset cooldown
            }
        } else {
            // No target found, implement wandering behavior
            wander();
        }
    }

    // Method to find the nearest entity within the detection radius
    private Entity findNearestEntity() {
        Entity nearestEntity = null;
        double nearestDistance = Double.MAX_VALUE;

        for (StaticEntity entity : CollidableRepository.getInstance()) {
            if (entity instanceof Entity && entity != this && ((Entity) entity).isAlive()) {
                Entity potentialTarget = (Entity) entity;
                double distance = getDistanceTo(potentialTarget);

                if (distance <= DETECTION_RADIUS && distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestEntity = potentialTarget;
                }
            }
        }

        return nearestEntity;
    }

    // Calculate the distance to another entity
    private double getDistanceTo(Entity entity) {
        int dx = entity.getX() - this.x;
        int dy = entity.getY() - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    // Move towards the current target
    private void moveTowardsTarget() {
        int dx = targetEntity.getX() - this.x;
        int dy = targetEntity.getY() - this.y;

        // Normalize the direction vector
        double length = Math.sqrt(dx * dx + dy * dy);
        if (length == 0) {
            return; // Target is at the same position
        }

        int deltaX = (int) Math.round((dx / length) * speed);
        int deltaY = (int) Math.round((dy / length) * speed);

        moveWithinBounds(deltaX, deltaY);
    }

    // Check if the target is within shooting range
    private boolean isWithinShootingRange(Entity entity) {
        double distance = getDistanceTo(entity);
        return distance <= SHOOTING_RANGE;
    }

    // Shoot at the target
    private void shootAtTarget() {
        // Determine the direction to shoot based on the target's position
        Direction shootDirection = getDirectionTo(targetEntity);
        shoot(shootDirection);
    }

    // Get the cardinal direction towards the target entity
    private Direction getDirectionTo(Entity entity) {
        int dx = entity.getX() - this.x;
        int dy = entity.getY() - this.y;

        if (Math.abs(dx) > Math.abs(dy)) {
            // Horizontal direction is stronger
            return dx > 0 ? Direction.RIGHT : Direction.LEFT;
        } else {
            // Vertical direction is stronger
            return dy > 0 ? Direction.DOWN : Direction.UP;
        }
    }

    // Implement wandering behavior when no target is available
    private void wander() {
        // Simple wandering logic: random movement

        int deltaX = (random.nextInt(20) - 1) * speed; // -1, 0, or 1 multiplied by speed
        int deltaY = (random.nextInt(20) - 1) * speed;

        moveWithinBounds(deltaX, deltaY);
    }

    @Override
    protected void onDeath() {
        // Custom logic for enemy death, e.g., remove from game or spawn loot
        System.out.println("Enemy defeated.");
        // Remove enemy from the collision repository
        CollidableRepository.getInstance().unregisterEntity(this);
    }

    // Modified draw method to include camera offsets and health bar
    @Override
    public void draw(Canvas canvas, int cameraX, int cameraY) {
        int drawX = x - cameraX;
        int drawY = y - cameraY;

        // Draw the enemy as a red rectangle
        canvas.drawRectangle(drawX, drawY, width, height, Color.RED);

        // Draw health bar and bullets
        drawHealthBarAndBullets(canvas, cameraX, cameraY);
    }

    // Optional: Override the draw method without camera offsets for compatibility
    @Override
    public void draw(Canvas canvas) {
        // If no camera offsets are provided, default to zero
        draw(canvas, 0, 0);
    }
}
