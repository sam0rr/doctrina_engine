package BattleRoyal;

import Doctrina.Canvas;
import java.awt.Color;
import java.util.Random;

public class Storm {
    private int radius;
    private int targetRadius;  // New target radius for smooth transition
    private int xCenter, yCenter; // Center coordinates of the storm
    private int shrinkCooldown;   // Time before next shrink
    private int currentPhase;     // Current phase of the storm
    private int pauseDuration;    // Time to pause between shrink phases
    private boolean isPaused;     // Indicates whether the storm is in a pause phase
    private Random random;        // For generating random positions

    // Canvas dimensions (for random center positioning)
    private int canvasWidth;
    private int canvasHeight;

    // Variables to manage smooth shrinking
    private int shrinkStepsLeft;  // Remaining frames to shrink

    public Storm(int canvasWidth, int canvasHeight) {
        this.shrinkCooldown = 500;  // Example cooldown between shrinks (500 frames)
        this.currentPhase = 1;      // Start in phase 1
        this.pauseDuration = 600;  // Pause for 20 seconds at 60 FPS (20 * 60 = 1200 frames)
        this.isPaused = false;      // Start with no pause
        this.random = new Random();
        this.canvasWidth = 2400;
        this.canvasHeight = 2400;

        // Start with the storm covering the entire map (no storm effect initially)
        this.radius = (Math.max(canvasWidth, canvasHeight)); // Large enough to cover the whole map
        this.targetRadius = radius;

        // Initialize shrinking steps
        this.shrinkStepsLeft = shrinkCooldown;

        // Set the initial position for the storm center at a random location
        randomizeCenter();
    }

    // Shrink the storm radius smoothly during the cooldown period
    public void shrink() {
        if (isPaused) {
            if (pauseDuration > 0) {
                pauseDuration--;
            } else {
                isPaused = false;
                pauseDuration = 400;
                shrinkStepsLeft = shrinkCooldown;
            }
        } else {
            if (shrinkStepsLeft > 0) {
                int shrinkAmountPerStep = (radius - targetRadius) / shrinkStepsLeft;
                radius -= shrinkAmountPerStep;
                shrinkStepsLeft--;
            }

            if (shrinkStepsLeft == 0) {
                isPaused = true;
                handlePhaseTransition();
            }
        }
    }

    private void handlePhaseTransition() {
        currentPhase++;

        if (currentPhase == 2) {
            targetRadius = Math.max(radius / 2, 100);
        } else if (currentPhase == 3) {
            targetRadius = Math.max(radius / 2, 50);
        } else if (currentPhase == 4) {
            targetRadius = 0;
        }
    }

    private void randomizeCenter() {
        if (currentPhase < 4) {
            xCenter = random.nextInt(canvasWidth);
            yCenter = random.nextInt(canvasHeight);
        }
    }

    public boolean isPlayerInSafeZone(int playerX, int playerY) {
        int distance = (int) Math.sqrt(Math.pow(playerX - xCenter, 2) + Math.pow(playerY - yCenter, 2));
        return distance < radius;
    }

    // Draw the storm (blue outside, transparent inside) with camera offsets
    public void draw(Canvas canvas, int offsetX, int offsetY) {
        // Apply camera offset to the storm's center
        int drawX = xCenter + offsetX;
        int drawY = yCenter + offsetY;

        // Draw the storm as a large blue circle (outer storm area)
        Color stormColor = new Color(0, 0, 255, 100);  // Blue color with transparency
        canvas.drawCircle(drawX, drawY, radius + 50, stormColor);  // Large storm circle

        // Draw the safe zone inside the storm as a transparent circle
        Color clearColor = new Color(0, 0, 0, 0);  // Fully transparent color
        canvas.drawCircle(drawX, drawY, radius, clearColor);  // Safe zone circle
    }
}
