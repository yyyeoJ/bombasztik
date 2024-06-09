package bombasztik.bomberman.view;

import bombasztik.bomberman.entity.Player;
import bombasztik.bomberman.sound.Sound;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

import static javax.swing.SwingConstants.CENTER;

public class RoundOverPanel extends JPanel {
    private final GuiManager gm;
    public Player winner;

    JLabel WinnerLabel = new JLabel();
    JLabel MainLabel1 = new JLabel();
    JLabel WinnerIcon = new JLabel();
    JButton Button1 = new JButton();
    JButton Button2 = new JButton();
    JLabel Player1Icon = new JLabel();
    JLabel Player2Icon = new JLabel();
    JLabel Player3Icon = new JLabel();

    public RoundOverPanel(GuiManager gm) throws IOException {
        this.gm = gm;
        initComponents();
    }

    private void initComponents() throws IOException {
        setPreferredSize(new Dimension((int) (340 * gm.scale), (int) (340 * gm.scale)));

        MainLabel1.setFont(gm.assetLoader.customFont.deriveFont((float) (20 * gm.scale)));
        MainLabel1.setForeground(new Color(255, 255, 255));

        WinnerLabel.setFont(gm.assetLoader.customFont.deriveFont((float) (20 * gm.scale)));
        WinnerLabel.setForeground(new Color(255, 255, 255));

        WinnerIcon.setPreferredSize(new Dimension((int) (20 * gm.scale), (int) (20 * gm.scale)));

        Player1Icon.setPreferredSize(new Dimension((int) (20 * gm.scale), (int) (20 * gm.scale)));

        Player2Icon.setPreferredSize(new Dimension((int) (20 * gm.scale), (int) (20 * gm.scale)));

        Player3Icon.setPreferredSize(new Dimension((int) (20 * gm.scale), (int) (20 * gm.scale)));

        Button1.setPreferredSize(new Dimension((int) (100 * gm.scale), (int) (20 * gm.scale)));
        Button1.setVerticalTextPosition(CENTER);
        Button1.setHorizontalTextPosition(CENTER);
        Button1.setFont(gm.assetLoader.customFont.deriveFont((float) (8 * gm.scale)));
        Button1.setForeground(new Color(255, 255, 255));
        Button1.setIcon(AssetLoader.scaleImage("/menu/menuButton.png", gm.scale));
        Button1.addActionListener(evt -> {
            try {
                Button1ActionPerformed();
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        });

        Button2.setPreferredSize(new Dimension((int) (100 * gm.scale), (int) (20 * gm.scale)));
        Button2.setVerticalTextPosition(CENTER);
        Button2.setHorizontalTextPosition(CENTER);
        Button2.setFont(gm.assetLoader.customFont.deriveFont((float) (8 * gm.scale)));
        Button2.setForeground(new Color(255, 255, 255));
        Button2.setText("Exit");
        Button2.setIcon(AssetLoader.scaleImage("/menu/menuButton.png", gm.scale));
        Button2.addActionListener(evt -> Button2ActionPerformed());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap((int) (90 * gm.scale), (int) (90 * gm.scale), (int) (90 * gm.scale))
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(WinnerLabel)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(WinnerIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(MainLabel1)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap((int) (120 * gm.scale), (int) (120 * gm.scale), (int) (120 * gm.scale))
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(Button2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(Button1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap((int) (20 * gm.scale), (int) (20 * gm.scale), (int) (20 * gm.scale))
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(Player3Icon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(Player2Icon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(Player1Icon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                                .addContainerGap((int) (109 * gm.scale), Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap((int) (40 * gm.scale), (int) (40 * gm.scale), (int) (40 * gm.scale))
                                .addComponent(MainLabel1)
                                .addGap((int) (10 * gm.scale), (int) (10 * gm.scale), (int) (10 * gm.scale))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(WinnerIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(WinnerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addGap((int) (10 * gm.scale), (int) (10 * gm.scale), (int) (10 * gm.scale))
                                .addComponent(Player1Icon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap((int) (10 * gm.scale), (int) (10 * gm.scale), (int) (10 * gm.scale))
                                .addComponent(Player2Icon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap((int) (10 * gm.scale), (int) (10 * gm.scale), (int) (10 * gm.scale))
                                .addComponent(Player3Icon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap((int) (5 * gm.scale), (int) (5 * gm.scale), (int) (5 * gm.scale))
                                .addComponent(Button1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap((int) (20 * gm.scale), (int) (20 * gm.scale), (int) (20 * gm.scale))
                                .addComponent(Button2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0))
        );
    }

    /**
     * Sets the details for the round over screen:
     * The winner's icon, and the winners name, or "Draw" if its a draw
     */
    private void setWinnerDetails() {
        if (winner == null) {
            WinnerIcon.setIcon(null);
            WinnerLabel.setText("  Draw");
            WinnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        } else {
            WinnerLabel.setText("Winner:");
        }
    }

    /**
     * Sets the player icons depending on the winner, and if 3 player mode is selected or not.
     */
    private void setPlayerIcons() {
        try {
            Player1Icon.setIcon(AssetLoader.scaleImage("/menu/player1-icon.png", gm.scale));
            Player2Icon.setIcon(AssetLoader.scaleImage("/menu/player2-icon.png", gm.scale));

            if (gm.model.player3mode) {
                Player3Icon.setIcon(AssetLoader.scaleImage("/menu/player3-icon.png", gm.scale));
            }

            // Set the actual winner icon
            if (winner != null) {
                WinnerIcon.setIcon(AssetLoader.scaleImage("/menu/player" + gm.model.winnerId + "-icon-winner.png", gm.scale));
            }

            // Set the full winner icon to winner icon
            if (winner != null && winner.roundsWon == gm.model.rounds) {
                switch (winner.getId()) {
                    case 1 -> Player1Icon.setIcon(AssetLoader.scaleImage("/menu/player1-icon-winner.png", gm.scale));
                    case 2 -> Player2Icon.setIcon(AssetLoader.scaleImage("/menu/player2-icon-winner.png", gm.scale));
                    case 3 -> Player3Icon.setIcon(AssetLoader.scaleImage("/menu/player3-icon-winner.png", gm.scale));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setButton1Text() {
        if (winner != null && winner.roundsWon == gm.model.rounds) {
            Button1.setText("Go to menu");
        } else {
            Button1.setText("Next Round");
        }
    }

    private Image getBackgroundImage() {
        if (winner != null && winner.roundsWon == gm.model.rounds) {
            return gm.assetLoader.gameOverbackground;
        } else {
            return gm.assetLoader.roundOverBackground;
        }
    }

    /**
     * Draws the player's scores on the screen
     */
    private void drawPlayerScores(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString(":" + gm.model.players.get(0).roundsWon + "|" + gm.model.rounds, (int) (160 * gm.scale), (int) (115 * gm.scale));
        g.drawString(":" + gm.model.players.get(1).roundsWon + "|" + gm.model.rounds, (int) (160 * gm.scale), (int) (145 * gm.scale));

        if (gm.newgame.player3Selector.isSelected()) {
            g.drawString(":" + gm.model.players.get(2).roundsWon + "|" + gm.model.rounds, (int) (160 * gm.scale), (int) (175 * gm.scale));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        setWinnerDetails();
        setPlayerIcons();
        setButton1Text();

        MainLabel1.setText((winner != null && winner.roundsWon == gm.model.rounds) ? "Game Over" : ("Round: " + gm.model.currentRound));

        g.drawImage(getBackgroundImage(), 0, 0, (int) (340 * gm.scale), (int) (340 * gm.scale), this);
        g.setFont(gm.assetLoader.customFont.deriveFont((float) (9 * gm.scale)));
        drawPlayerScores(g);
    }

    /**
     * Back to menu or Next round
     */
    private void Button1ActionPerformed() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        // Ha valaki nyert -> vissza a menübe
        for (Player p : gm.model.players) {
            if (p.roundsWon == gm.model.rounds) {
                gm.backToMenu();
                return;
            }
        }

        gm.controller.resetModel();
        gm.model.soundManager.roundOverSound.stopMusic();

        Random random = new Random();
        int randomNumber = random.nextInt(5) + 1;
        gm.model.soundManager.gameSound = new Sound("/sounds/battle" + randomNumber + ".wav");
        gm.model.soundManager.gameSound.playMusic();
    }

    /**
     * Exits game
     */
    private void Button2ActionPerformed() {
        gm.model.soundManager.gameOverSound.stopMusic();
        gm.model.soundManager.roundOverSound.stopMusic();
        System.exit(0);
    }
}
