package bombasztik.bomberman.sound;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class SoundManager {
    public Sound menuSound;
    public Sound buttonSound = new Sound("/sounds/button.wav");
    public Sound gameSound;
    public Sound explosionSound = new Sound("/sounds/explosion.wav");
    public Sound powerupSound = new Sound("/sounds/powerup.wav");
    public Sound alertSound = new Sound("/sounds/alert.wav");
    public Sound deathSound = new Sound("/sounds/death.wav");
    public Sound roundOverSound = new Sound("/sounds/roundover.wav");
    public Sound gameOverSound = new Sound("/sounds/gameover.wav");

    /**
     * Creates a class that stores all the loaded sounds to be referenced later.
     *
     * @throws UnsupportedAudioFileException
     * @throws LineUnavailableException
     * @throws IOException
     */
    public SoundManager() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
    }
}
