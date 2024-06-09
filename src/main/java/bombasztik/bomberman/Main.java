package bombasztik.bomberman;

import bombasztik.bomberman.view.GuiManager;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, FontFormatException, UnsupportedAudioFileException, LineUnavailableException {
        new GuiManager();
    }
}
