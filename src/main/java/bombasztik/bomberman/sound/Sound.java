package bombasztik.bomberman.sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Objects;

public class Sound {
    public Clip clip;

    public Sound(String path) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        setFile(path);
    }

    /**
     * Sets the provided .wav file as the clip to be played later.
     * You can change every sound's volume here if needed
     *
     * @param path a String that contains the reference to the .wav file.
     *
     * @throws UnsupportedAudioFileException
     * @throws  IOException
     * @throws  LineUnavailableException
     */
    private void setFile(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResource(path)));
        clip = AudioSystem.getClip();
        clip.open(ais);

        // Change volume if needed
        // FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        // gainControl.setValue(+6.0f);
    }


    /**
     * Plays the music on loop
     */
    public void playMusic() {
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Stops the music and resets it to the beginning
     */
    public void stopMusic() {
        clip.setFramePosition(0);
        clip.stop();
    }

    /**
     * Plays a sound effect once,
     * makes sure to stop before playing again if its already playing,
     * and makes sure to play from the beginning
     */
    public void playSoundEffect() {
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }
}
