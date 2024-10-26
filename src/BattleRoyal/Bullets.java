package BattleRoyal;

import Doctrina.*;
import Doctrina.Canvas;

import java.awt.*;

public class Bullets extends MovableEntity {
    private final Direction bulletDirection;
    private final int damage = 20; // Set a fixed damage value for each bullet
    private final World world; // Reference to the game world for boundary checks
    private final Entity ownerEntity; // The entity that fired the bullet
    private boolean isMarkedForRemoval = false;

    public Bullets(Entity entity, Direction direction, World world) {
        this.ownerEntity = entity;
        this.bulletDirection = direction;
        this.world = world; // Initialize the world reference
        initialize(entity);
    }

    private void initialize(Entity entity) {
        setSpeed(10); // Set the speed of the bullet

        // Set dimensions and position based on the direction of the bullet
        switch (bulletDirection) {
            case RIGHT:
                setDimensions(8, 4);
                teleport(entity.getX() + entity.getWidth(), entity.getY() + entity.getHeight() / 2 - 2);
                break;
            case LEFT:
                setDimensions(8, 4);
                teleport(entity.getX() - 8, entity.getY() + entity.getHeight() / 2 - 2);
                break;
            case DOWN:
                setDimensions(4, 8);
                teleport(entity.getX() + entity.getWidth() / 2 - 2, entity.getY() + entity.getHeight());
                break;
            case UP:
                setDimensions(4, 8);
                teleport(entity.getX() + entity.getWidth() / 2 - 2, entity.getY() - 8);
                break;
        }
    }

    @Override
    public void update() {
        if (isMarkedForRemoval) {
            return; // Skip update if bullet is marked for removal
        }
        move(bulletDirection);  // Move bullet in the direction it was fired

        // Check if bullet is within bounds
        if (!isWithinBounds()) {
            removeBullet(); // Mark bullet for removal if out of bounds
            return; // Exit update since bullet is no longer active
        }

        // Check for collision with entities
        Entity hitEntity = checkCollision();
        if (hitEntity != null) {
            hitEntity.takeDamage(damage); // Apply damage to the entity
            removeBullet(); // Mark bullet for removal upon collision
        }
    }

    // Detect collision with entities
    public Entity checkCollision() {
        Rectangle bulletBounds = getBounds();
        for (StaticEntity entity : CollidableRepository.getInstance()) {
            if (entity instanceof Entity && entity != this && entity != ownerEntity) {
                Entity target = (Entity) entity;
                if (bulletBounds.intersects(target.getBounds())) {
                    removeBullet();
                    System.out.println("entity hit: (" + x + ", " + y + ")");
                    return target; // Return entity if collision is detected

                }
            }
        }
        return null;
    }

    public boolean isMarkedForRemoval() {
        return isMarkedForRemoval;
    }

    private void removeBullet() {
        if (!isMarkedForRemoval) {
            isMarkedForRemoval = true;
            CollidableRepository.getInstance().unregisterEntity(this); // Unregister from collision detection
            System.out.println("Bullet removed");
        }
    }


    public boolean isWithinBounds() {
        return x >= 0 && x + width <= world.getWidth() && y >= 0 && y + height <= world.getHeight();
    }

    // Draw the bullet with camera offsets
    public void draw(Canvas canvas, int cameraX, int cameraY) {
        if (isMarkedForRemoval) {
            return; // Skip drawing if bullet is marked for removal
        }
        int drawX = x - cameraX;
        int drawY = y - cameraY;
        canvas.drawRectangle(drawX, drawY, width, height, Color.YELLOW);
    }


    @Override
    public void draw(Canvas canvas) {
        // Not used in this context
    }

}