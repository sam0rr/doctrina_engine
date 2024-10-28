package Viking;

import Doctrina.Canvas;
import Doctrina.Game;
import Doctrina.RenderingEngine;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class VikingGame extends Game {
    private GamePad gamePad;
    private Player player;
    private World world;

    private Tree tree;
    private int soundCooldown;  // Cooldown variable should be a field

    @Override
    protected void initialize() {

        gamePad = new GamePad();
        player = new Player(gamePad);
        world = new World();
        world.load();
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

        RenderingEngine.getInstance().getScreen().fullscreen();
        RenderingEngine.getInstance().getScreen().hideCursor();
    }

    @Override
    protected void update() {

        player.update();

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

            SoundEffect.FIRE.play();
        }

    }

    @Override
    protected void draw(Canvas canvas) {
        world.draw(canvas);
        player.draw(canvas);

        if (player.getY() < tree.getY() + 52) {
            player.draw(canvas);
            tree.draw(canvas);
        } else {
            tree.draw(canvas);
            player.draw(canvas);
        }
    }
}
