package bombasztik.bomberman.view;

import bombasztik.bomberman.sound.Sound;

import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class NewGamePanel extends JPanel {
    private final JLabel mapDisplay = new JLabel();
    private final JLabel mapLabel = new JLabel();
    private final JLabel roundLabel = new JLabel();
    public JCheckBox player3Selector = new JCheckBox();
    JLabel player3icon = new JLabel();
    JLabel player3label = new JLabel();
    JLabel player3controls = new JLabel();
    JLabel newgameLabel = new JLabel();
    JLabel player1icon = new JLabel();
    JLabel player1label = new JLabel();
    JLabel player1controls = new JLabel();
    JLabel player2icon = new JLabel();
    JLabel player2label = new JLabel();
    JLabel player2controls = new JLabel();
    JButton prevMapButton = new JButton();
    JButton nextMapButton = new JButton();
    Box.Filler filler1 = new Box.Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(0, 32767));
    JButton startButton = new JButton();
    JButton menuButton = new JButton();
    JButton prevRoundButton = new JButton();
    JButton nextRoundButton = new JButton();
    public Image MenuBackgroundImage;
    public Image backgroundImage;
    private final GuiManager gm;

    public NewGamePanel(GuiManager gm) throws IOException {
        this.gm = gm;

        setPreferredSize(new Dimension((int) (340 * gm.scale), (int) (340 * gm.scale)));
        setBackground(new Color(255, 0, 0));
        setDoubleBuffered(true);
        setFocusable(false);
        setLayout(new OverlayLayout(this));
        backgroundImage = gm.assetLoader.backgroundImageNormal;

        MenuBackgroundImage = gm.assetLoader.newGameBackground;

        newgameLabel.setFont(gm.assetLoader.customFont.deriveFont((float) (15 * gm.scale)));
        newgameLabel.setForeground(new Color(255, 255, 255));
        newgameLabel.setText("New Game");

        player1icon.setIcon(AssetLoader.scaleImage("/menu/player1-icon.png", gm.scale));
        player1label.setFont(gm.assetLoader.customFont.deriveFont((float) (7 * gm.scale)));
        player1label.setForeground(new Color(255, 255, 255));
        player1label.setText("Player1");
        player1controls.setFont(gm.assetLoader.customFont.deriveFont((float) (5 * gm.scale)));
        player1controls.setForeground(new Color(255, 255, 255));
        player1controls.setText("W A S D E Q");

        player2icon.setIcon(AssetLoader.scaleImage("/menu/player2-icon.png", gm.scale));
        player2label.setFont(gm.assetLoader.customFont.deriveFont((float) (7 * gm.scale)));
        player2label.setForeground(new Color(255, 255, 255));
        player2label.setText("Player2");
        player2controls.setFont(gm.assetLoader.customFont.deriveFont((float) (5 * gm.scale)));
        player2controls.setForeground(new Color(255, 255, 255));
        player2controls.setText("↑ ← ↓ → [-] [.]  ");
        player3icon.setIcon(AssetLoader.scaleImage("/menu/player3-icon-disabled.png", gm.scale));

        player3label.setFont(gm.assetLoader.customFont.deriveFont((float) (7 * gm.scale)));
        player3label.setForeground(new Color(200, 200, 200));
        player3label.setText("Player3");
        player3controls.setFont(gm.assetLoader.customFont.deriveFont((float) (5 * gm.scale)));
        player3controls.setForeground(new Color(200, 200, 200));
        player3controls.setText("I J K L O U");

        prevMapButton.setIcon(AssetLoader.scaleImage("/menu/arrow-left.png", gm.scale));
        prevMapButton.setMaximumSize(new Dimension((int) (20 * gm.scale), (int) (20 * gm.scale)));
        prevMapButton.setMinimumSize(new Dimension((int) (20 * gm.scale), (int) (20 * gm.scale)));
        prevMapButton.setPreferredSize(new Dimension((int) (20 * gm.scale), (int) (20 * gm.scale)));
        prevMapButton.setContentAreaFilled(false);
        prevMapButton.setBorderPainted(false);
        prevMapButton.addActionListener(evt -> {
            try {
                prevMapButtonActionPerformed();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        nextMapButton.setIcon(AssetLoader.scaleImage("/menu/arrow-right.png", gm.scale));
        nextMapButton.setMaximumSize(new Dimension((int) (20 * gm.scale), (int) (20 * gm.scale)));
        nextMapButton.setMinimumSize(new Dimension((int) (20 * gm.scale), (int) (20 * gm.scale)));
        nextMapButton.setPreferredSize(new Dimension((int) (20 * gm.scale), (int) (20 * gm.scale)));
        nextMapButton.setContentAreaFilled(false);
        nextMapButton.setBorderPainted(false);
        nextMapButton.addActionListener(evt -> {
            try {
                nextMapButtonActionPerformed();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        mapLabel.setFont(gm.assetLoader.customFont.deriveFont((float) (5 * gm.scale)));
        mapLabel.setForeground(new Color(255, 255, 255));
        mapLabel.setText("Map: 1");

        startButton.setPreferredSize(new Dimension((int) (100 * gm.scale), (int) (20 * gm.scale)));
        startButton.setVerticalTextPosition(SwingConstants.CENTER);
        startButton.setHorizontalTextPosition(SwingConstants.CENTER);
        startButton.setFont(gm.assetLoader.customFont.deriveFont((float) (10 * gm.scale)));
        startButton.setForeground(new Color(255, 255, 255));
        startButton.setText("Start");
        startButton.setIcon(AssetLoader.scaleImage("/menu/button-normal.png", gm.scale));
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.addActionListener(evt -> {
            try {
                startButtonActionPerformed();
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        });

        menuButton.setPreferredSize(new Dimension((int) (100 * gm.scale), (int) (20 * gm.scale)));
        menuButton.setVerticalTextPosition(SwingConstants.CENTER);
        menuButton.setHorizontalTextPosition(SwingConstants.CENTER);
        menuButton.setFont(gm.assetLoader.customFont.deriveFont((float) (7 * gm.scale)));
        menuButton.setForeground(new Color(255, 255, 255));
        menuButton.setText("Back to menu");
        menuButton.setIcon(AssetLoader.scaleImage("/menu/button-normal.png", gm.scale));
        menuButton.setContentAreaFilled(false);
        menuButton.setBorderPainted(false);
        menuButton.addActionListener(evt -> {
            try {
                gm.backToMenu();
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        });


        prevRoundButton.setIcon(AssetLoader.scaleImage("/menu/arrow-left.png", gm.scale));
        prevRoundButton.setMaximumSize(new Dimension((int) (20 * gm.scale), (int) (20 * gm.scale)));
        prevRoundButton.setMinimumSize(new Dimension((int) (20 * gm.scale), (int) (20 * gm.scale)));
        prevRoundButton.setPreferredSize(new Dimension((int) (20 * gm.scale), (int) (20 * gm.scale)));
        prevRoundButton.setContentAreaFilled(false);
        prevRoundButton.setBorderPainted(false);
        prevRoundButton.addActionListener(evt -> prevRoundButtonActionPerformed());

        roundLabel.setFont(gm.assetLoader.customFont.deriveFont((float) (5 * gm.scale)));
        roundLabel.setForeground(new Color(255, 255, 255));
        roundLabel.setText("Rounds: " + gm.model.rounds);

        nextRoundButton.setIcon(AssetLoader.scaleImage("/menu/arrow-right.png", gm.scale));
        nextRoundButton.setMaximumSize(new Dimension((int) (20 * gm.scale), (int) (20 * gm.scale)));
        nextRoundButton.setMinimumSize(new Dimension((int) (20 * gm.scale), (int) (20 * gm.scale)));
        nextRoundButton.setPreferredSize(new Dimension((int) (20 * gm.scale), (int) (20 * gm.scale)));
        nextRoundButton.setContentAreaFilled(false);
        nextRoundButton.setBorderPainted(false);
        nextRoundButton.addActionListener(evt -> nextRoundButtonActionPerformed());

        player3Selector.setMaximumSize(new Dimension((int) (8 * gm.scale), (int) (8 * gm.scale)));
        player3Selector.setMinimumSize(new Dimension((int) (8 * gm.scale), (int) (8 * gm.scale)));
        player3Selector.setPreferredSize(new Dimension((int) (8 * gm.scale), (int) (8 * gm.scale)));
        player3Selector.setOpaque(false);
        player3Selector.setContentAreaFilled(false);
        player3Selector.setBorderPainted(false);
        player3Selector.addActionListener(evt -> {
            try {
                player3SelectorActionPerformed();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(startButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(menuButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                                .addGap((int) (50 * gm.scale), (int) (50 * gm.scale), (int) (50 * gm.scale))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addComponent(prevRoundButton, GroupLayout.PREFERRED_SIZE, (int) (20 * gm.scale), GroupLayout.PREFERRED_SIZE)
                                                .addGap((int) (8 * gm.scale), (int) (8 * gm.scale), (int) (8 * gm.scale))
                                                .addComponent(roundLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, (int) (14 * gm.scale), Short.MAX_VALUE)
                                                .addComponent(nextRoundButton, GroupLayout.PREFERRED_SIZE, (int) (20 * gm.scale), GroupLayout.PREFERRED_SIZE)
                                                .addGap((int) (34 * gm.scale), (int) (34 * gm.scale), (int) (34 * gm.scale))
                                                .addComponent(filler1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                .addComponent(player2icon)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(player2label)
                                                                        .addComponent(player2controls)))
                                                        .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                .addComponent(player1icon)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(player1label)
                                                                        .addComponent(player1controls)))
                                                        .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                .addComponent(player3icon)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(player3label)
                                                                                .addGap((int) (2 * gm.scale), (int) (2 * gm.scale), (int) (2 * gm.scale))
                                                                                .addComponent(player3Selector, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                                        .addComponent(player3controls))))))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, (int) (9 * gm.scale), Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(mapDisplay)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(prevMapButton, GroupLayout.PREFERRED_SIZE, (int) (20 * gm.scale), GroupLayout.PREFERRED_SIZE)
                                                .addGap((int) (18 * gm.scale), (int) (18 * gm.scale), (int) (18 * gm.scale))
                                                .addComponent(mapLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(nextMapButton, GroupLayout.PREFERRED_SIZE, (int) (20 * gm.scale), GroupLayout.PREFERRED_SIZE)))
                                .addGap((int) (50 * gm.scale), (int) (50 * gm.scale), (int) (50 * gm.scale)))
                        .addGroup(layout.createSequentialGroup()
                                .addGap((int) (110 * gm.scale), (int) (110 * gm.scale), (int) (110 * gm.scale))
                                .addComponent(newgameLabel)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap((int) (49 * gm.scale), (int) (49 * gm.scale), (int) (49 * gm.scale))
                                .addComponent(newgameLabel)
                                .addGap((int) (34 * gm.scale), (int) (34 * gm.scale), (int) (34 * gm.scale))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(mapDisplay)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(player1icon)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap((int) (2 * gm.scale), (int) (2 * gm.scale), (int) (2 * gm.scale))
                                                                .addComponent(player1label)
                                                                .addGap((int) (5 * gm.scale), (int) (5 * gm.scale), (int) (5 * gm.scale))
                                                                .addComponent(player1controls)))
                                                .addGap((int) (15 * gm.scale), (int) (15 * gm.scale), (int) (15 * gm.scale))
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(player2icon)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap((int) (2 * gm.scale), (int) (2 * gm.scale), (int) (2 * gm.scale))
                                                                .addComponent(player2label)
                                                                .addGap((int) (5 * gm.scale), (int) (5 * gm.scale), (int) (5 * gm.scale))
                                                                .addComponent(player2controls)))
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap((int) (15 * gm.scale), (int) (15 * gm.scale), (int) (15 * gm.scale))
                                                                .addComponent(player3icon))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap((int) (15 * gm.scale), (int) (15 * gm.scale), (int) (15 * gm.scale))
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(player3label)
                                                                        .addComponent(player3Selector, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                                .addGap((int) (5 * gm.scale), (int) (5 * gm.scale), (int) (5 * gm.scale))
                                                                .addComponent(player3controls)))))
                                .addGap((int) (10 * gm.scale), (int) (10 * gm.scale), (int) (10 * gm.scale))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap((int) (20 * gm.scale), (int) (20 * gm.scale), (int) (20 * gm.scale))
                                                .addComponent(filler1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(nextMapButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(prevMapButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(nextRoundButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(prevRoundButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap((int) (8 * gm.scale), (int) (8 * gm.scale), (int) (8 * gm.scale))
                                                                .addComponent(roundLabel))))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap((int) (8 * gm.scale), (int) (8 * gm.scale), (int) (8 * gm.scale))
                                                .addComponent(mapLabel)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(startButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap((int) (10 * gm.scale), (int) (10 * gm.scale), (int) (10 * gm.scale))
                                .addComponent(menuButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap((int) (50 * gm.scale), (int) (50 * gm.scale), (int) (50 * gm.scale)))
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        roundLabel.setText("Rounds: " + gm.model.rounds);
        mapLabel.setText("Map: " + gm.model.selectedMap);

        try {
            mapDisplay.setIcon(AssetLoader.scaleImage("/menu/map" + gm.model.selectedMap + ".png", gm.scale));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        super.paintComponent(g);

        switch (gm.model.selectedMap) {
            case 1 -> backgroundImage = gm.assetLoader.backgroundImageNormal;
            case 2 -> backgroundImage = gm.assetLoader.backgroundImageIce;
            case 3 -> backgroundImage = gm.assetLoader.backgroundImageLava;
        }

        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        g.drawImage(MenuBackgroundImage, 2 * gm.view.scaledTileSize, 2 * gm.view.scaledTileSize, (int) (260 * gm.scale), (int) (260 * gm.scale), this);
    }

    private void startButtonActionPerformed() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        gm.model.soundManager.buttonSound.playSoundEffect();

        Random random = new Random();
        int randomNumber = random.nextInt(5) + 1;
        gm.model.soundManager.gameSound = new Sound("/sounds/battle" + randomNumber + ".wav");
        gm.model.soundManager.gameSound.playMusic();

        gm.cardPanel.add(gm.view, "game");
        gm.cardLayout.show(gm.cardPanel, "game");
        gm.roundOverPanel = new RoundOverPanel(gm);
        gm.cardPanel.add(gm.roundOverPanel, "roundOver");
        gm.model.init();
        gm.controller.initKeyHandler();
        gm.controller.startGameThread();

        gm.model.soundManager.menuSound.stopMusic();
    }

    private void prevMapButtonActionPerformed() throws IOException {
        gm.model.soundManager.buttonSound.playSoundEffect();
        if (gm.model.selectedMap == 1) {
            gm.model.selectedMap = 3;
        } else {
            gm.model.selectedMap -= 1;
        }

        mapLabel.setText("Map: " + gm.model.selectedMap);
        mapDisplay.setIcon(AssetLoader.scaleImage("/menu/map" + gm.model.selectedMap + ".png", gm.scale));

        repaint();
    }

    private void nextMapButtonActionPerformed() throws IOException {
        gm.model.soundManager.buttonSound.playSoundEffect();
        if (gm.model.selectedMap == 3) {
            gm.model.selectedMap = 1;
        } else {
            gm.model.selectedMap += 1;
        }

        mapLabel.setText("Map: " + gm.model.selectedMap);
        mapDisplay.setIcon(AssetLoader.scaleImage("/menu/map" + gm.model.selectedMap + ".png", gm.scale));

        repaint();
    }

    private void prevRoundButtonActionPerformed() {
        gm.model.soundManager.buttonSound.playSoundEffect();
        if (gm.model.rounds == 3) {
            gm.model.rounds = 9;
        } else {
            gm.model.rounds -= 2;
        }

        roundLabel.setText("Rounds: " + gm.model.rounds);

        repaint();
    }

    private void nextRoundButtonActionPerformed() {
        gm.model.soundManager.buttonSound.playSoundEffect();
        if (gm.model.rounds == 9) {
            gm.model.rounds = 3;
        } else {
            gm.model.rounds += 2;
        }

        roundLabel.setText("Rounds: " + gm.model.rounds);

        repaint();
    }

    public void player3SelectorActionPerformed() throws IOException {
        gm.model.soundManager.buttonSound.playSoundEffect();
        if (player3Selector.isSelected()) {
            player3icon.setIcon(AssetLoader.scaleImage("/menu/player3-icon.png", gm.scale));
            player3controls.setForeground(new Color(255, 255, 255));
            player3label.setForeground(new Color(255, 255, 255));
            gm.model.player3mode = true;
        } else {
            player3icon.setIcon(AssetLoader.scaleImage("/menu/player3-icon-disabled.png", gm.scale));
            player3controls.setForeground(new Color(200, 200, 200));
            player3label.setForeground(new Color(200, 200, 200));
            gm.model.player3mode = false;
        }
    }
}
