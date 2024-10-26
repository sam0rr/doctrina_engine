package BattleRoyal;

import Doctrina.Canvas;
import Doctrina.Direction;
import Doctrina.MovementController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    private static final String SPRITE_PATH = "images/player.png";
    private static final double SCALE_FACTOR = 1.7;
    private static final int ANIMATION_SPEED = 8;

    private int currentAnimationFrame = 1;
    private int nextFrame = ANIMATION_SPEED;
    private BufferedImage image;
    private Image[] rightFrames, leftFrames, upFrames, downFrames;
    private Direction lastDirection;
    private Storm storm;
    private BattleRoyalGame game;
    private MovementController controller;

    public Player(MovementController controller, Storm storm, BattleRoyalGame game, World world) {
        super(world, (int) (32 * SCALE_FACTOR), (int) (32 * SCALE_FACTOR), 5);
        this.storm = storm;
        this.game = game;
        this.controller = controller;
        this.lastDirection = Direction.DOWN;
        load();  // Load player sprite and animation frames
    }

    private void load() {
        loadSpriteSheet();
        loadAnimationFrames();
    }

    private void loadSpriteSheet() {
        try {
            image = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(SPRITE_PATH));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadAnimationFrames() {
        downFrames = new Image[3];
        downFrames[0] = getScaledImage(image.getSubimage(0, 128, 32, 32));
        downFrames[1] = getScaledImage(image.getSubimage(32, 128, 32, 32));
        downFrames[2] = getScaledImage(image.getSubimage(64, 128, 32, 32));

        leftFrames = new Image[3];
        leftFrames[0] = getScaledImage(image.getSubimage(0, 160, 32, 32));
        leftFrames[1] = getScaledImage(image.getSubimage(32, 160, 32, 32));
        leftFrames[2] = getScaledImage(image.getSubimage(64, 160, 32, 32));

        rightFrames = new Image[3];
        rightFrames[0] = getScaledImage(image.getSubimage(0, 192, 32, 32));
        rightFrames[1] = getScaledImage(image.getSubimage(32, 192, 32, 32));
        rightFrames[2] = getScaledImage(image.getSubimage(64, 192, 32, 32));

        upFrames = new Image[3];
        upFrames[0] = getScaledImage(image.getSubimage(0, 224, 32, 32));
        upFrames[1] = getScaledImage(image.getSubimage(32, 224, 32, 32));
        upFrames[2] = getScaledImage(image.getSubimage(64, 224, 32, 32));
    }

    private Image getScaledImage(BufferedImage img) {
        int scaledWidth = (int) (img.getWidth() * SCALE_FACTOR);
        int scaledHeight = (int) (img.getHeight() * SCALE_FACTOR);
        BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, img.getType());

        Graphics2D g2d = scaledImage.createGraphics();
        g2d.drawImage(img, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        return scaledImage;
    }

    @Override
    public void update() {
        super.update();
        moveWithController();

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
    }

    // Method to shoot a bullet in the last facing direction
    public void shoot() {
        shoot(lastDirection); // Use inherited shoot method from Entity with lastDirection
    }

    @Override
    protected void onDeath() {
        game.onPlayerDeath();
    }

    @Override
    public void draw(Canvas canvas) {
        draw(canvas, 0, 0); // Default draw with no offset
    }

    @Override
    public void draw(Canvas canvas, int cameraX, int cameraY) {
        Direction direction = controller.getDirection();
        if (direction == null) {
            direction = lastDirection;
        } else {
            lastDirection = direction; // Update the last direction if moving
        }

        int drawX = x - cameraX;
        int drawY = y - cameraY;

        if (direction == Direction.RIGHT) {
            canvas.drawImage(rightFrames[currentAnimationFrame], drawX, drawY);
        } else if (direction == Direction.LEFT) {
            canvas.drawImage(leftFrames[currentAnimationFrame], drawX, drawY);
        } else if (direction == Direction.UP) {
            canvas.drawImage(upFrames[currentAnimationFrame], drawX, drawY);
        } else if (direction == Direction.DOWN) {
            canvas.drawImage(downFrames[currentAnimationFrame], drawX, drawY);
        }

        drawHealthBarAndBullets(canvas, cameraX, cameraY);
    }

    protected void moveWithController() {
        Direction direction = controller.getDirection();

        if (direction != null) {
            lastDirection = direction;
            int deltaX = 0;
            int deltaY = 0;
            switch (direction) {
                case UP -> deltaY = -speed;
                case DOWN -> deltaY = speed;
                case LEFT -> deltaX = -speed;
                case RIGHT -> deltaX = speed;
            }
            moveWithinBounds(deltaX, deltaY);
        }
    }
}
