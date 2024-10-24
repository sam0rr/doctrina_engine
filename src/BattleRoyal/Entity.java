package BattleRoyal;

import Doctrina.Canvas;

import java.util.Random;

public abstract class Entity {
    public int x, y;             // Position
    protected int width, height; // Dimensions
    protected int health;        // Health
    protected boolean isAlive;   // Is the entity alive?
    protected int speed;         // Movement speed
    private static final int MAX_HEALTH = 100;  // Maximum health for all entities


    // Constructor for setting initial position, dimensions, and speed
    public Entity(World world, int width, int height, int speed) {
        Random random = new Random();
        this.x = random.nextInt(world.getWidth());  // Random x position within the world width
        this.y = random.nextInt(world.getHeight()); // Random y position within the world height
        this.width = width;
        this.height = height;
        this.speed = speed;          // Set the speed
        this.health = MAX_HEALTH;    // Start with full health
        this.isAlive = true;         // Initially, the entity is alive
    }


    // Common update method to be called by all entities
    public void update() {
        if (health <= 0 && isAlive) {
            isAlive = false;
            onDeath();  // Handle death logic
        }
    }

    // Method to take damage
    public void takeDamage(int damage) {
        if (isAlive) {
            health -= damage;
            if (health < 0) {
                health = 0;
            }
            if (health == 0) {
                isAlive = false;
                onDeath();  // Call the death handler when health reaches 0
            }
        }
    }

    // Method to heal
    public void heal(int amount) {
        if (isAlive) {
            health += amount;
            if (health > MAX_HEALTH) {
                health = MAX_HEALTH;
            }
        }
    }

    // Method to check if the entity is alive
    public boolean isAlive() {
        return isAlive;
    }

    // Method to be called when the entity dies
    protected abstract void onDeath();

    // Method to draw the entity's health bar
    protected void drawHealthBar(Canvas canvas) {
        int barWidth = 32;
        int barHeight = 5;
        int barX = x;
        int barY = y - 10;  // Positioning the bar above the entity

        // Red background for the health bar
        canvas.drawRectangle(barX, barY, barWidth, barHeight, java.awt.Color.RED);

        // Green foreground representing current health
        int healthWidth = (int) ((health / 100.0f) * barWidth);
        canvas.drawRectangle(barX, barY, healthWidth, barHeight, java.awt.Color.GREEN);
    }

    // Abstract method to draw the entity
    public abstract void draw(Canvas canvas);

    // Placeholder for movement logic (to be overridden by subclasses)
    protected abstract void moveWithController();
}
