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

    private Storm storm;  // Storm instance

    private Tree tree;
    private int soundCooldown;  // Cooldown variable should be a field

    @Override
    protected void initialize() {
        gamePad = new GamePad();
        world = new World();
        world.load();

        // Initialize the storm before the player, so it's available when creating the player
        storm = new Storm(world.getWidth(), world.getHeight());  // Pass the world dimensions to the storm

        player = new Player(gamePad, storm, this, world);  // Pass the world object as well
// Pass the storm and game reference to the player

        soundCooldown = 0;  // Initialize the cooldown
        tree = new Tree(300,350);
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
        player.update();  // Update the player, which will now apply storm damage if outside the zone
        storm.shrink();   // Update the storm (shrink it over time)

        if (gamePad.isQuitPressed()) {
            stop();
        }

        if (player.getY() < tree.getY() + 52) {
            tree.blockadeFromTop();
        } else {
            tree.blockadeFromBottom();
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
        // First, draw the world
        world.draw(canvas);

        // Draw the storm after the world
        storm.draw(canvas);  // Ensure the storm is drawn

        // Now, draw the player and tree
        if (player.getY() < tree.getY() + 52) {
            player.draw(canvas);
            tree.draw(canvas);
        } else {
            tree.draw(canvas);
            player.draw(canvas);
        }
    }

    // Method to handle player death and stop the game
    public void onPlayerDeath() {
        System.out.println("Player has died. Game Over.");
        stop();  // Stop the game when the player dies
    }
}
