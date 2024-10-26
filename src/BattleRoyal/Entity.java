package BattleRoyal;

import Doctrina.*;
import Doctrina.Canvas;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public abstract class Entity extends MovableEntity {
    protected int health;
    protected boolean isAlive;
    protected int speed;
    protected World world;
    private static final int MAX_HEALTH = 100;
    private static final int MAX_POSITION_ATTEMPTS = 100;

    // List to store bullets
    protected List<Bullets> bullets;

    public Entity(World world, int width, int height, int speed) {
        if (world == null) {
            throw new IllegalArgumentException("World cannot be null when creating an Entity.");
        }

        this.world = world;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.health = MAX_HEALTH;
        this.isAlive = true;
        this.bullets = new ArrayList<>();

        // Place entity in a non-colliding position
        placeInNonCollidingPosition();

        // Register entity for collision detection
        CollidableRepository.getInstance().registerEntity(this);
    }

    private void placeInNonCollidingPosition() {
        Random random = new Random();
        boolean positioned = false;
        int attempts = 0;

        while (!positioned && attempts < MAX_POSITION_ATTEMPTS) {
            this.x = random.nextInt(world.getWidth() - width);
            this.y = random.nextInt(world.getHeight() - height);
            positioned = !isCollidingAtInitialization();
            attempts++;
        }

        if (!positioned) {
            System.out.println("Warning: Could not find a non-colliding position after " + MAX_POSITION_ATTEMPTS + " attempts. Placing entity at (" + x + ", " + y + ")");
        } else {
            System.out.println("Entity initialized at position: (" + x + ", " + y + ")");
        }
    }

    private boolean isCollidingAtInitialization() {
        for (StaticEntity entity : CollidableRepository.getInstance()) {
            if (entity != this && isCollidingWith(x, y, entity)) {
                return true;
            }
        }
        return false;
    }

    // Method for updating entity and its bullets
    public void update() {
        if (health <= 0 && isAlive) {
            isAlive = false;
            onDeath();
            CollidableRepository.getInstance().unregisterEntity(this); // Unregister upon death
        }
        updateBullets(); // Update bullets for movement and collision
    }

    // Updates bullets' movement and handles out-of-bounds cleanup
    public void updateBullets() {
        Iterator<Bullets> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullets bullet = iterator.next();
            bullet.update();
            if (bullet.isMarkedForRemoval()) {
                iterator.remove(); // Safely remove the bullet
            }
        }
    }



    // Method to shoot a bullet in a specified direction
    public void shoot(Direction direction) {
        Bullets bullet = new Bullets(this, direction, world); // Create bullet in specified direction
        bullets.add(bullet); // Add bullet to entity's bullet list
        CollidableRepository.getInstance().registerEntity(bullet); // Register the bullet
    }


    // Draws the health bar above the entity and bullets on the canvas
    protected void drawHealthBarAndBullets(Canvas canvas, int cameraX, int cameraY) {
        int drawX = x - cameraX;
        int drawY = y - cameraY;

        // Draw health bar
        int barWidth = width;
        int barHeight = 5;
        int barX = drawX;
        int barY = drawY - 15; // Position the health bar above the entity

        canvas.drawRectangle(barX, barY, barWidth, barHeight, Color.RED);

        // Draw green health bar based on current health
        int healthWidth = (int) ((health / (float) MAX_HEALTH) * barWidth);
        if (healthWidth > 0) {
            canvas.drawRectangle(barX, barY, healthWidth, barHeight, Color.GREEN);
        }

        // Draw bullets with correct camera offsets
        for (Bullets bullet : bullets) {
            bullet.draw(canvas, cameraX, cameraY); // Pass cameraX and cameraY
        }
    }


    protected void moveWithinBounds(int deltaX, int deltaY) {
        int newX = x + deltaX;
        int newY = y + deltaY;

        // Check map boundaries
        if (newX < 0 || newX + width > world.getWidth() || newY < 0 || newY + height > world.getHeight()) {
            return; // Out of bounds, cancel movement
        }

        // Check for collisions with other entities
        for (StaticEntity entity : CollidableRepository.getInstance()) {
            if (entity != this && isCollidingWith(newX, newY, entity)) {
                return; // Collision detected, cancel movement
            }
        }

        // Update position if no collisions and within bounds
        x = newX;
        y = newY;
    }

    private boolean isCollidingWith(int newX, int newY, StaticEntity entity) {
        Rectangle newBounds = new Rectangle(newX, newY, width, height);
        return newBounds.intersects(entity.getBounds());
    }

    public void takeDamage(int damage) {
        if (isAlive) {
            System.out.println("Entity " + this.getClass().getSimpleName() + " taking damage: " + damage);
            health -= damage;
            System.out.println("New health: " + health);

            if (health <= 0) {
                health = 0;
                isAlive = false;
                System.out.println("Entity " + this.getClass().getSimpleName() + " has died.");
                onDeath();
                CollidableRepository.getInstance().unregisterEntity(this); // Unregister upon death
            }
        } else {
            System.out.println("Entity " + this.getClass().getSimpleName() + " is already dead, no damage applied.");
        }
    }


    public boolean isAlive() {
        return isAlive;
    }

    // Draw method for rendering entity along with health bar and bullets
    public void draw(Canvas canvas, int cameraX, int cameraY) {
        int drawX = x - cameraX;
        int drawY = y - cameraY;

        // Draw the entity
        canvas.drawRectangle(drawX, drawY, width, height, Color.BLUE);

        // Correctly passing cameraX and cameraY
        drawHealthBarAndBullets(canvas, cameraX, cameraY);
    }


    protected abstract void onDeath();
}
