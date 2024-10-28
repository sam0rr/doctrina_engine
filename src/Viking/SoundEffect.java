package Viking;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public enum SoundEffect {

    FIRE(100);
    //MURLOC("audios/murloc.wav");
    private int maxCooldown;
    private int cooldown;
    private String path;
    private AudioInputStream stream;

    SoundEffect(int maxCooldown) {
        this.maxCooldown = maxCooldown;
        this.path = path;
    }

    public void play() {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream stream = AudioSystem.getAudioInputStream(this.getClass().getClassLoader().getResourceAsStream("audio/fire.wav"));
            clip.open(stream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}