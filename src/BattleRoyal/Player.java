package BattleRoyal;

import Doctrina.Canvas;
import Doctrina.Direction;
import Doctrina.MovementController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Player extends Entity {
    private static final String SPRITE_PATH = "images/player.png";
    private static final int STORM_DAMAGE = 1; // Damage per frame when in the storm
    private static final int ANIMATION_SPEED = 8;

    private int currentAnimationFrame = 1;
    private int nextFrame = ANIMATION_SPEED;
    private BufferedImage image;
    private Image[] rightFrames;
    private Image[] leftFrames;
    private Image[] upFrames;
    private Image[] downFrames;

    private Storm storm;
    private BattleRoyalGame game;
    private MovementController controller;


    public Player(MovementController controller, Storm storm, BattleRoyalGame game, World world) {
        super(world, 32, 32, 5);  // Call Entity constructor with random position, size, and speed

        this.storm = storm;
        this.game = game;
        this.controller = controller;
        load();  // Load player sprite and animation frames
    }




    private void load() {
        loadSpriteSheet();
        loadAnimationFrames();
    }

    private void loadAnimationFrames() {
        downFrames = new Image[3];
        downFrames[0] = image.getSubimage(0, 128, width, height);
        downFrames[1] = image.getSubimage(32, 128, width, height);
        downFrames[2] = image.getSubimage(64, 128, width, height);

        leftFrames = new Image[3];
        leftFrames[0] = image.getSubimage(0, 160, width, height);
        leftFrames[1] = image.getSubimage(32, 160, width, height);
        leftFrames[2] = image.getSubimage(64, 160, width, height);

        rightFrames = new Image[3];
        rightFrames[0] = image.getSubimage(0, 192, width, height);
        rightFrames[1] = image.getSubimage(32, 192, width, height);
        rightFrames[2] = image.getSubimage(64, 192, width, height);

        upFrames = new Image[3];
        upFrames[0] = image.getSubimage(0, 224, width, height);
        upFrames[1] = image.getSubimage(32, 224, width, height);
        upFrames[2] = image.getSubimage(64, 224, width, height);
    }

    private void loadSpriteSheet() {
        try {
            image = ImageIO.read(
                    this.getClass().getClassLoader().getResourceAsStream(SPRITE_PATH)
            );
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update() {
        super.update();  // Call the update logic from Entity
        moveWithController();  // Handle player-specific movement

        // Update animation frames
        if (controller.isMoving()) {
            --nextFrame;
            if (nextFrame == 0) {
                ++currentAnimationFrame;
                if (currentAnimationFrame >= leftFrames.length) {
                    currentAnimationFrame = 0;
                }
                nextFrame = ANIMATION_SPEED;
            }
        } else {
            currentAnimationFrame = 1;
        }

        // Apply storm damage if the player is outside the safe zone
        if (!storm.isPlayerInSafeZone(getX(), getY())) {
            takeDamage(STORM_DAMAGE);
        }
    }

    public int getY() {
        return x;
    }

    public int getX() {
        return y;
    }

    @Override
    protected void onDeath() {
        game.onPlayerDeath();  // Notify the game that the player has died
    }

    // Method to draw a temporary red rectangle around the player (hitbox)
    private void drawHitbox(Canvas canvas) {
        Color transparentRed = new Color(255, 0, 0, 128);  // Red with transparency
        canvas.drawRectangle(x, y, width, height, transparentRed);  // Draw hitbox
    }

    @Override
    public void draw(Canvas canvas) {
        if (controller.getDirection() == Direction.RIGHT) {
            canvas.drawImage(rightFrames[currentAnimationFrame], x, y);
        } else if (controller.getDirection() == Direction.LEFT) {
            canvas.drawImage(leftFrames[currentAnimationFrame], x, y);
        } else if (controller.getDirection() == Direction.UP) {
            canvas.drawImage(upFrames[currentAnimationFrame], x, y);
        } else if (controller.getDirection() == Direction.DOWN) {
            canvas.drawImage(downFrames[currentAnimationFrame], x, y);
        }

        // Draw the hitbox around the player
        drawHitbox(canvas);

        // Draw the player's health bar
        drawHealthBar(canvas);
    }

    @Override
    protected void moveWithController() {
        Direction direction = controller.getDirection();

        if (direction != null) {
            switch (direction) {
                case UP:
                    y -= speed;
                    break;
                case DOWN:
                    y += speed;
                    break;
                case LEFT:
                    x -= speed;
                    break;
                case RIGHT:
                    x += speed;
                    break;
            }
        }
    }
}
