package BattleRoyal;

import Doctrina.Canvas;
import Doctrina.GameTime;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Storm {
    private int radius;
    private int targetRadius;
    private int xCenter, yCenter;
    private int currentPhase;
    private boolean isStormActive;
    private Random random;

    private int mapWidth;
    private int mapHeight;
    private long phaseStartTime;
    private long shrinkStartTime;
    private long lastUpdateTime;

    private boolean isWaiting;
    private boolean isShrinking;

    // Phase and shrink durations in seconds
    private final int phase1DurationSec, phase2DurationSec, phase3DurationSec, phase4DurationSec;
    private final int phase1ShrinkDurationSec, phase2ShrinkDurationSec, phase3ShrinkDurationSec, phase4ShrinkDurationSec;
    private float shrinkSpeedPerSec;

    public Storm(World world, int phase1DurationSec, int phase2DurationSec, int phase3DurationSec, int phase4DurationSec,
                 int phase1ShrinkDurationSec, int phase2ShrinkDurationSec, int phase3ShrinkDurationSec, int phase4ShrinkDurationSec) {
        this.phase1DurationSec = phase1DurationSec;
        this.phase2DurationSec = phase2DurationSec;
        this.phase3DurationSec = phase3DurationSec;
        this.phase4DurationSec = phase4DurationSec;

        this.phase1ShrinkDurationSec = phase1ShrinkDurationSec;
        this.phase2ShrinkDurationSec = phase2ShrinkDurationSec;
        this.phase3ShrinkDurationSec = phase3ShrinkDurationSec;
        this.phase4ShrinkDurationSec = phase4ShrinkDurationSec;

        this.currentPhase = 1;
        this.isStormActive = true;
        this.isWaiting = true;
        this.isShrinking = false;

        this.mapWidth = world.getWidth();
        this.mapHeight = world.getHeight();
        this.radius = (int) (Math.sqrt(mapWidth * mapWidth + mapHeight * mapHeight));
        this.targetRadius = Math.max(radius / 2, 600);

        this.lastUpdateTime = GameTime.getCurrentTime();
        this.random = new Random();

        randomizeCenter();
        System.out.println("Initial storm setup: radius=" + radius + ", xCenter=" + xCenter + ", yCenter=" + yCenter);

        setShrinkSpeedPerSec(getCurrentShrinkDurationSec());
    }

    public void updateStorm() {
        if (!isStormActive) return;

        long currentTime = GameTime.getCurrentTime();
        float deltaTimeSec = (currentTime - lastUpdateTime) / 1000f;
        lastUpdateTime = currentTime;

        if (isWaiting) {
            updateWaiting(currentTime);
        } else if (isShrinking) {
            updateShrinking(deltaTimeSec);
        }
    }

    private void updateWaiting(long currentTime) {
        if (phaseStartTime == 0) phaseStartTime = currentTime;

        int currentPhaseDurationMs = getPhaseDurationSec() * 1000;
        long elapsedTimeMs = currentTime - phaseStartTime;

        if (elapsedTimeMs >= currentPhaseDurationMs) {
            isWaiting = false;
            isShrinking = true;
            shrinkStartTime = currentTime;
            setShrinkSpeedPerSec(getCurrentShrinkDurationSec());
            System.out.println("Phase duration over. Starting to shrink.");
        }
    }

    private void updateShrinking(float deltaTimeSec) {
        float shrinkAmount = shrinkSpeedPerSec * deltaTimeSec;
        radius = (int) Math.max(radius - shrinkAmount, targetRadius);


        if (radius <= targetRadius) {
            isShrinking = false;
            handlePhaseTransition();
        }
    }

    private void handlePhaseTransition() {
        currentPhase++;
        if (currentPhase > 4) {
            System.out.println("All phases completed.");
            isStormActive = false;
            return;
        }

        phaseStartTime = 0;
        targetRadius = calculateTargetRadiusForPhase(currentPhase);
        setShrinkSpeedPerSec(getCurrentShrinkDurationSec());
        isWaiting = true;
    }

    private int calculateTargetRadiusForPhase(int phase) {
        return switch (phase) {
            case 2 -> Math.max(radius / 2, 300);
            case 3 -> Math.max(radius / 2, 150);
            case 4 -> 0;
            default -> radius;
        };
    }

    public String getPhaseInfo() {
        if (!isStormActive) return "Storm completed.";

        long currentTime = GameTime.getCurrentTime();
        int timeLeftSeconds = 0;

        if (isWaiting) {
            int currentPhaseDurationSec = getPhaseDurationSec();
            long elapsedTimeSec = (currentTime - phaseStartTime) / 1000;
            timeLeftSeconds = Math.max(currentPhaseDurationSec - (int) elapsedTimeSec, 0);
        } else if (isShrinking) {
            int shrinkDurationSec = getCurrentShrinkDurationSec();
            long elapsedShrinkTimeSec = (currentTime - shrinkStartTime) / 1000;
            timeLeftSeconds = Math.max(shrinkDurationSec - (int) elapsedShrinkTimeSec, 0);
        }

        return "Phase " + currentPhase + " - Time Left: " + timeLeftSeconds + "s";
    }

    private int getPhaseDurationSec() {
        return switch (currentPhase) {
            case 2 -> phase2DurationSec;
            case 3 -> phase3DurationSec;
            case 4 -> phase4DurationSec;
            default -> phase1DurationSec;
        };
    }

    private int getCurrentShrinkDurationSec() {
        return switch (currentPhase) {
            case 2 -> phase2ShrinkDurationSec;
            case 3 -> phase3ShrinkDurationSec;
            case 4 -> phase4ShrinkDurationSec;
            default -> phase1ShrinkDurationSec;
        };
    }

    private void setShrinkSpeedPerSec(int shrinkDurationSec) {
        int shrinkDistance = radius - targetRadius;
        this.shrinkSpeedPerSec = (float) shrinkDistance / shrinkDurationSec;
    }

    private void randomizeCenter() {
        if (currentPhase < 4) {
            xCenter = random.nextInt(mapWidth);
            yCenter = random.nextInt(mapHeight);
            System.out.println("New storm center: xCenter=" + xCenter + ", yCenter=" + yCenter);
        }
    }

    public void draw(Canvas canvas, int offsetX, int offsetY) {
        Graphics2D g2d = canvas.getGraphics2D();
        Area stormArea = new Area(new Rectangle2D.Float(offsetX, offsetY, mapWidth, mapHeight));
        int safeZoneX = xCenter + offsetX;
        int safeZoneY = yCenter + offsetY;
        Area safeZone = new Area(new Ellipse2D.Float(safeZoneX - radius, safeZoneY - radius, radius * 2, radius * 2));
        stormArea.subtract(safeZone);
        g2d.setColor(new Color(0, 0, 255, 100));
        g2d.fill(stormArea);
    }
}
