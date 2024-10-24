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
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;

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
            // During the pause phase, reduce the pause duration until it reaches 0
            if (pauseDuration > 0) {
                pauseDuration--;
            } else {
                // Pause is over, resume shrinking and reset the pause state
                isPaused = false;
                pauseDuration = 400;  // Reset pause duration for the next phase (20 seconds)
                shrinkStepsLeft = shrinkCooldown;  // Restart shrink steps for the next phase
            }
        } else {
            // If shrink steps are still remaining, smoothly shrink
            if (shrinkStepsLeft > 0) {
                int shrinkAmountPerStep = (radius - targetRadius) / shrinkStepsLeft;
                radius -= shrinkAmountPerStep;  // Decrease radius gradually
                shrinkStepsLeft--;              // Reduce the steps left for shrinking
            }

            // When all shrinking steps are done, initiate the pause phase before next transition
            if (shrinkStepsLeft == 0) {
                isPaused = true;  // Start the pause phase
                handlePhaseTransition();  // Set up the next phase
            }
        }
    }

    // Handle transitioning between phases
    private void handlePhaseTransition() {
        currentPhase++;

        if (currentPhase == 2) {
            // Phase 2: Shrink to a smaller radius and move to a new center
            targetRadius = Math.max(radius / 2, 100); // Shrink to half the size or a minimum of 100
        } else if (currentPhase == 3) {
            // Phase 3: Shrink further and move to another random center
            targetRadius = Math.max(radius / 2, 50); // Shrink to half or a minimum of 50
        } else if (currentPhase == 4) {
            // Final phase: The storm closes over the entire map
            targetRadius = 0;  // Complete closure
            // Stop randomizing the center in the final phase
        }
    }

    // Randomize the center of the storm within the canvas dimensions
    private void randomizeCenter() {
        // Only randomize the center if we're not in the final phase
        if (currentPhase < 4) {
            xCenter = random.nextInt(canvasWidth);
            yCenter = random.nextInt(canvasHeight);
        }
    }

    // Check if a player is inside the storm
    public boolean isPlayerInSafeZone(int playerX, int playerY) {
        int distance = (int) Math.sqrt(Math.pow(playerX - xCenter, 2) + Math.pow(playerY - yCenter, 2));
        return distance < radius;
    }

    // Draw the storm (blue outside, transparent inside)
    public void draw(Canvas canvas) {
        // Draw the storm as a large blue circle (outer storm area)
        Color stormColor = new Color(0, 0, 255, 100);  // Blue color with transparency
        canvas.drawCircle(xCenter, yCenter, radius + 50, stormColor);  // Large storm circle

        // Draw the safe zone inside the storm as a transparent circle
        Color clearColor = new Color(0, 0, 0, 0);  // Fully transparent color
        canvas.drawCircle(xCenter, yCenter, radius, clearColor);  // Safe zone circle
    }
}
