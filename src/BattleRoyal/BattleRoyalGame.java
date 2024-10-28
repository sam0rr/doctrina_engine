package BattleRoyal;

import Doctrina.*;
import Doctrina.Canvas;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BattleRoyalGame extends Game {
    private GamePad gamePad;
    private Player player;
    private World world;
    private Camera camera;
    private Storm storm;
    private int cooldown;
    private List<Enemy> enemies;

    private final int numberOfEnnemies = 20;

    @Override
    protected void initialize() {
        CollidableRepository.getInstance().clear();
        gamePad = new GamePad();
        world = new World();
        world.load();

        storm = new Storm(world, 5, 5, 5, 5,
                5, 5, 5, 5);
        player = new Player(gamePad, storm, this, world);
        camera = new Camera(1000, 650, world);
        cooldown = 0;

        enemies = new ArrayList<>();
        for (int i = 0; i < numberOfEnnemies; i++) {
            enemies.add(new Enemy(world));
        }

        try {
            Clip backgroundClip = AudioSystem.getClip();
            AudioInputStream backgroundStream = AudioSystem.getAudioInputStream(
                    getClass().getClassLoader().getResourceAsStream("audio/background.wav")
            );
            backgroundClip.open(backgroundStream);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        RenderingEngine.getInstance().getScreen().fullscreen();
        RenderingEngine.getInstance().getScreen().hideCursor();
    }

    @Override
    protected void update() {
        player.update();
        storm.updateStorm();
        camera.update(player);

        for (Enemy enemy : enemies) {
            enemy.update();
        }

        if (gamePad.isQuitPressed()) {
            stop();
        }

        if (cooldown > 0) {
            cooldown--;
        }

        // Trigger shooting if fire button is pressed and cooldown is zero
        if (gamePad.isFirePressed() && cooldown == 0) {
            player.shoot(); // Make the player shoot a bullet in the last facing direction
            cooldown = 20;  // Set cooldown to prevent rapid shooting

            // Play shooting sound
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream fireStream = AudioSystem.getAudioInputStream(
                        getClass().getClassLoader().getResourceAsStream("audio/fire.wav")
                );
                clip.open(fireStream);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void draw(Canvas canvas) {
        int offsetX = camera.getX();
        int offsetY = camera.getY();

        world.draw(canvas, -offsetX, -offsetY);
        storm.draw(canvas, -offsetX, -offsetY);

        // Draw player with bullets
        player.draw(canvas, offsetX, offsetY);

        for (Enemy enemy : enemies) {
            enemy.draw(canvas, offsetX, offsetY);
        }

        // Display storm and game info
        String phaseInfo = storm.getPhaseInfo();
        Rectangle clipBounds = canvas.getGraphics2D().getClipBounds();
        int textX = (clipBounds != null ? clipBounds.width : 800) - 200;
        int textY = 20;
        canvas.drawString(phaseInfo, textX, textY, Color.WHITE);
        canvas.drawString(GameTime.getElapsedFormattedTime(), 10, 40, Color.WHITE);
        canvas.drawString("FPS: " + GameTime.getCurrentFps(), 10, 60, Color.WHITE);
    }

    public void onPlayerDeath() {
        System.out.println("Player has died. Game Over.");
        stop();
    }
}
