package bombasztik.bomberman.view;

import bombasztik.bomberman.controller.GameController;
import bombasztik.bomberman.model.GameModel;
import bombasztik.bomberman.sound.Sound;
import bombasztik.bomberman.sound.SoundManager;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Random;

public class GuiManager {
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenSize = toolkit.getScreenSize();

    public double scale = Double.parseDouble(String.format(Locale.US, "%.1f", (0.00245 * screenSize.height)));

    public CardLayout cardLayout;
    public JPanel cardPanel;
    public RoundOverPanel roundOverPanel;
    public NewGamePanel newgame;

    public AssetLoader assetLoader = new AssetLoader();
    public GameModel model = new GameModel();
    public GamePanel view = new GamePanel(this);
    public GameController controller = new GameController(view, model);

    /**
     * This class manages all the different panels that make up the game.
     *
     * @throws  IOException
     * @throws FontFormatException
     * @throws  UnsupportedAudioFileException
     * @throws  LineUnavailableException
     */
    public GuiManager() throws IOException, FontFormatException, UnsupportedAudioFileException, LineUnavailableException {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Bombasztik");
        window.setSize((int) (340 * scale), (int) (340 * scale));

        URL iconURL = getClass().getResource("/icon.png");
        assert iconURL != null;
        ImageIcon icon = new ImageIcon(iconURL);
        window.setIconImage(icon.getImage());

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        MenuPanel menu = new MenuPanel(this);
        newgame = new NewGamePanel(this);

        cardPanel.add(menu, "menu");
        cardPanel.add(newgame, "newgame");

        window.getContentPane().add(cardPanel);
        cardLayout.show(cardPanel, "menu");

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }


    /**
     * This function returns to the main menu, stops all the music, and plays the menu music.
     * It also resets everything selected in the new game menu to its default value.
     * Resets the model the view and the controller.
     *
     * @param path A string that contains the relative path to the Image to be loaded.
     *
     * @throws  IOException
     *
     */
    public void backToMenu() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        newgame.player3Selector.setSelected(false);
        newgame.player3SelectorActionPerformed();

        model.soundManager.roundOverSound.stopMusic();
        model.soundManager.gameOverSound.stopMusic();
        model.soundManager.menuSound.stopMusic();
        if (model.soundManager.gameSound != null) {
            model.soundManager.gameSound.stopMusic();
        }

        Random random = new Random();
        int randomNumber = random.nextInt(5) + 1;
        model.soundManager.menuSound = new Sound("/sounds/menu" + randomNumber + ".wav");
        model.soundManager.menuSound.playMusic();

        SoundManager temp = model.soundManager;
        model = new GameModel();
        view = new GamePanel(this);
        controller = new GameController(view, model);
        cardLayout.show(cardPanel, "menu");
        model.soundManager = temp;
    }
}
