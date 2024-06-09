package bombasztik.bomberman.view;

import bombasztik.bomberman.sound.Sound;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class MenuPanel extends JPanel {
    private final Image MenuBackgroundImage;
    public Image backgroundImage;

    private final GuiManager gm;

    public MenuPanel(GuiManager gm) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        this.gm = gm;

        Random random = new Random();
        int randomNumber = random.nextInt(5) + 1;
        gm.model.soundManager.menuSound = new Sound("/sounds/menu" + randomNumber + ".wav");
        gm.model.soundManager.menuSound.playMusic();

        setPreferredSize(new Dimension((int) (340 * gm.scale), (int) (340 * gm.scale)));
        setBackground(new Color(255, 0, 0));
        setDoubleBuffered(true);
        setFocusable(false);
        setLayout(new OverlayLayout(this));

        backgroundImage = gm.assetLoader.backgroundImageNormal;

        MenuBackgroundImage = gm.assetLoader.menuBackground;

        JLabel titleLabel = new JLabel();
        titleLabel.setIcon(AssetLoader.scaleImage("/menu/title.png", gm.scale));

        JButton newGameButton = new JButton();
        newGameButton.setIcon(AssetLoader.scaleImage("/menu/button-normal.png", gm.scale));
        newGameButton.setPreferredSize(new Dimension((int) (100 * gm.scale), (int) (20 * gm.scale)));
        newGameButton.setVerticalTextPosition(SwingConstants.CENTER);
        newGameButton.setHorizontalTextPosition(SwingConstants.CENTER);
        newGameButton.setFont(gm.assetLoader.customFont.deriveFont((float) (8 * gm.scale)));
        newGameButton.setForeground(new Color(255, 255, 255));
        newGameButton.setText("New Game");
        newGameButton.setContentAreaFilled(false);
        newGameButton.setBorderPainted(false);
        newGameButton.addActionListener(evt -> newGameButtonActionPerformed());

        JButton exitButton = new JButton();
        exitButton.setIcon(AssetLoader.scaleImage("/menu/button-normal.png", gm.scale));
        exitButton.setPreferredSize(new Dimension((int) (100 * gm.scale), (int) (20 * gm.scale)));
        exitButton.setVerticalTextPosition(SwingConstants.CENTER);
        exitButton.setHorizontalTextPosition(SwingConstants.CENTER);
        exitButton.setFont(gm.assetLoader.customFont.deriveFont((float) (8 * gm.scale)));
        exitButton.setForeground(new Color(255, 255, 255));
        exitButton.setText("Exit");
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        exitButton.addActionListener(evt -> exitButtonActionPerformed());

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap((int) (65 * gm.scale), (int) (65 * gm.scale), (int) (65 * gm.scale))
                                                .addComponent(titleLabel))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap((int) (120 * gm.scale), (int) (120 * gm.scale), (int) (120 * gm.scale))
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addComponent(exitButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(newGameButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
                                .addGap((int) (65 * gm.scale), (int) (65 * gm.scale), (int) (65 * gm.scale)))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap((int) (80 * gm.scale), (int) (80 * gm.scale), (int) (80 * gm.scale))
                                .addComponent(titleLabel)
                                .addGap((int) (80 * gm.scale), (int) (80 * gm.scale), (int) (80 * gm.scale))
                                .addComponent(newGameButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap((int) (20 * gm.scale), (int) (20 * gm.scale), (int) (20 * gm.scale))
                                .addComponent(exitButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap((int) (80 * gm.scale), Short.MAX_VALUE))
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        g.drawImage(MenuBackgroundImage, gm.view.scaledTileSize, gm.view.scaledTileSize, (int) (300 * gm.scale), (int) (300 * gm.scale), this);
    }

    private void newGameButtonActionPerformed() {
        gm.cardLayout.show(gm.cardPanel, "newgame");
        gm.model.soundManager.buttonSound.playSoundEffect();
    }

    private void exitButtonActionPerformed() {
        gm.model.soundManager.buttonSound.playSoundEffect();
        System.exit(0);
    }
}
