package BattleRoyal;

import Doctrina.Canvas;
import Doctrina.Game;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class BattleRoyalGame extends Game {
    private GamePad gamePad;
    private Player player;
    private World world;
    private Camera camera;  // Camera instance

    private Storm storm;  // Storm instance
    private int soundCooldown;  // Cooldown variable should be a field

    @Override
    protected void initialize() {
        gamePad = new GamePad();
        world = new World();
        world.load();

        // Initialize the storm before the player, so it's available when creating the player
        storm = new Storm(world.getWidth(), world.getHeight());  // Pass the world dimensions to the storm

        // Initialize the player and camera
        player = new Player(gamePad, storm, this, world);
        camera = new Camera(1000, 650, world);  // Pass world to the camera
// Set camera dimensions to match the screen width and height
        soundCooldown = 0;  // Initialize the cooldown

        try {
            Clip backgroundClip = AudioSystem.getClip();
            AudioInputStream backgroundStream = AudioSystem.getAudioInputStream(
                    getClass().getClassLoader().getResourceAsStream("audio/background.wav")
            );
            backgroundClip.open(backgroundStream);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);  // Loop background music continuously
            backgroundClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void update() {
        player.update();  // Update the player
        storm.shrink();   // Update the storm
        camera.update(player);  // Update camera to follow the player

        if (gamePad.isQuitPressed()) {
            stop();
        }

        if (soundCooldown > 0) {
            soundCooldown--;  // Decrease cooldown over time
        }

        if (gamePad.isFirePressed() && soundCooldown == 0) {
            soundCooldown = 100;

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

        // Draw the world based on the camera's offset
        world.draw(canvas, -offsetX, -offsetY);

        // Draw the storm based on camera offset
        storm.draw(canvas, -offsetX, -offsetY);

        // Draw player and other entities with camera offet
        player.draw(canvas, offsetX, offsetY);

    }


    // Method to handle player death and stop the game
    public void onPlayerDeath() {
        System.out.println("Player has died. Game Over.");
        stop();  // Stop the game when the player dies
    }
}
