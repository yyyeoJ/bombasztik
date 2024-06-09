package bombasztik.bomberman.view;

import bombasztik.bomberman.entity.*;
import bombasztik.bomberman.item.Item;
import bombasztik.bomberman.object.*;
import bombasztik.bomberman.object.Box;
import bombasztik.bomberman.model.GameModel;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class GamePanel extends JPanel {
    public GuiManager gm;
    public Image backgroundImage;
    public BufferedImage alertImage;
    public GameModel model;

    public int scaledTileSize;

    public GamePanel(GuiManager gm) throws IOException {
        this.model = gm.model;
        this.gm = gm;

        scaledTileSize = (int) (model.tileSize * gm.scale);

        setPreferredSize(new Dimension((int) (340 * gm.scale), (int) (340 * gm.scale)));
        setBackground(new Color(255, 0, 0));
        setDoubleBuffered(true);
        setFocusable(false);

        alertImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/powerups/alert.png")));

        JButton menuButton = getMenuButton(gm);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap((int) (120 * gm.scale), (int) (120 * gm.scale), (int) (120 * gm.scale))
                                .addComponent(menuButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap((int) (120 * gm.scale), Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap((int) (220 * gm.scale), Short.MAX_VALUE)
                                .addComponent(menuButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap((int) (100 * gm.scale), (int) (100 * gm.scale), (int) (100 * gm.scale)))
        );
    }

    private JButton getMenuButton(GuiManager gm) {
        JButton menuButton = new JButton();
        menuButton.setPreferredSize(new Dimension((int) (100 * gm.scale), (int) (20 * gm.scale)));
        menuButton.setVerticalTextPosition(SwingConstants.CENTER);
        menuButton.setHorizontalTextPosition(SwingConstants.CENTER);
        menuButton.setFont(gm.assetLoader.customFont.deriveFont((float) (7 * gm.scale)));
        menuButton.setForeground(new Color(255, 255, 255));
        menuButton.setText("");
        menuButton.setOpaque(false);

        menuButton.addActionListener(evt -> {
            try {
                if (!gm.controller.isRunning()) {
                    gm.backToMenu();
                }
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        });

        return menuButton;
    }

    // PaintComponent method
    public void paintComponent(Graphics g) {
        switch (model.selectedMap) {
            case 1 -> backgroundImage = gm.assetLoader.backgroundImageNormal;
            case 2 -> backgroundImage = gm.assetLoader.backgroundImageIce;
            case 3 -> backgroundImage = gm.assetLoader.backgroundImageLava;
        }

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        if (model.player3mode) {
            g.drawImage(gm.assetLoader.scoreboard, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.drawImage(gm.assetLoader.scoreboardPlayer3Disabled, 0, 0, getWidth(), getHeight(), this);
        }

        // Render paths
        for (Path path : new ArrayList<>(model.paths)) {
            path.draw(g2, gm);
        }

        // Render items
        for (Item item : new ArrayList<>(model.items)) {
            item.draw(g2, gm);
        }

        // Render explosions
        for (Explosion explosion : new ArrayList<>(model.explosions)) {
            explosion.draw(g2, gm);
        }

        // Render bombs
        for (Bomb bomb : new ArrayList<>(model.bombs)) {
            bomb.draw(g2, gm);
        }

        // Render walls bottom
        for (Wall wall : new ArrayList<>(model.walls)) {
            wall.draw(g2, gm);
        }

        // Render boxes bottom
        for (Box box : new ArrayList<>(model.boxes)) {
            box.draw(g2, gm);
        }

        // Render entities
        for (Entity entity : new ArrayList<>(model.entities)) {
            if (entity.alive) {
                entity.draw(g2, gm);
            }
        }

        // Render walls top
        for (Wall wall : new ArrayList<>(model.walls)) {
            wall.draw(g2, gm);
        }

        // Render box top
        for (Box box : new ArrayList<>(model.boxes)) {
            if (box.isTopOrBottom) {
                box.draw(g2, gm);
            }
        }

        // Render Ghost entities + Powerup alerts + dying entities
        for (Entity entity : new ArrayList<>(model.entities)) {
            if (entity.invincibleAlert || entity.ghostAlert) {
                g2.drawImage(alertImage, (int) ((entity.x * gm.scale) + scaledTileSize), (int) ((entity.y * gm.scale) + scaledTileSize - scaledTileSize), scaledTileSize, scaledTileSize, null);
            }

            if (entity.ghost || entity.startDeathAnimation) {
                entity.draw(g2, gm);
            }
        }

        if (!model.players.isEmpty()) {
            // Paint scoreboard labels
            g2.setColor(Color.WHITE);
            g2.setFont(gm.assetLoader.customFont.deriveFont((float) (5 * gm.scale)));

            // Player labels
            if (model.player3mode) {
                // player1
                g2.drawString(":" + model.players.get(0).roundsWon + "|" + model.rounds, (int) (78 * gm.scale), (int) (9 * gm.scale));
                if (model.players.get(0).alive) {
                    g2.drawString(
                            ":" + (model.players.getFirst().hasNoBombs ? "X" : (model.players.getFirst().bombLimit - model.players.getFirst().bombsPlaced) + "|" + model.players.getFirst().bombLimit)
                            , (int) (78 * gm.scale), (int) (18 * gm.scale));
                }

                // player2
                g2.drawString(":" + model.players.get(1).roundsWon + "|" + model.rounds, (int) (138 * gm.scale), (int) (9 * gm.scale));
                if (model.players.get(1).alive) {
                    g2.drawString(
                            ":" + (model.players.get(1).hasNoBombs ? "X" : (model.players.get(1).bombLimit - model.players.get(1).bombsPlaced) + "|" + model.players.get(1).bombLimit)
                            , (int) (138 * gm.scale), (int) (18 * gm.scale));
                }

                // player3
                g2.drawString(":" + model.players.get(2).roundsWon + "|" + model.rounds, (int) (198 * gm.scale), (int) (9 * gm.scale));
                if (model.players.get(2).alive) {
                    g2.drawString(
                            ":" + (model.players.get(2).hasNoBombs ? "X" : (model.players.get(2).bombLimit - model.players.get(2).bombsPlaced) + "|" + model.players.get(2).bombLimit)
                            , (int) (198 * gm.scale), (int) (18 * gm.scale));
                }

                // Round counter
                g2.setFont(gm.assetLoader.customFont.deriveFont((float) (4 * gm.scale)));
                g2.drawString("Round:" + (model.currentRound > 10 ? model.currentRound : "0" + model.currentRound), (int) (244 * gm.scale), (int) (13 * gm.scale));
            } else {
                // player1
                g2.drawString(":" + model.players.get(0).roundsWon + "|" + model.rounds, (int) (106 * gm.scale), (int) (9 * gm.scale));
                if (model.players.get(0).alive) {
                    g2.drawString(
                            ":" + (model.players.getFirst().hasNoBombs ? "X" : (model.players.getFirst().bombLimit - model.players.getFirst().bombsPlaced) + "|" + model.players.getFirst().bombLimit)
                            , (int) (106 * gm.scale), (int) (18 * gm.scale));
                }

                // player2
                g2.drawString(":" + model.players.get(1).roundsWon + "|" + model.rounds, (int) (226 * gm.scale), (int) (9 * gm.scale));
                if (model.players.get(1).alive) {
                    g2.drawString(
                            ":" + (model.players.get(1).hasNoBombs ? "X" : (model.players.get(1).bombLimit - model.players.get(1).bombsPlaced) + "|" + model.players.get(1).bombLimit)
                            , (int) (226 * gm.scale), (int) (18 * gm.scale));
                }

                // Round counter
                g2.setFont(gm.assetLoader.customFont.deriveFont((float) (4 * gm.scale)));
                g2.drawString("Round:" + (model.currentRound > 10 ? model.currentRound : "0" + model.currentRound), (int) (154 * gm.scale), (int) (12 * gm.scale));
            }

            if (!gm.controller.isRunning()) {
                g2.setColor(new Color(0, 0, 0, 100));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Color.WHITE);
                g2.setFont(gm.assetLoader.customFont.deriveFont((float) (15 * gm.scale)));
                g2.drawString("PAUSED", (int) (125 * gm.scale), getHeight() / 2);
                g2.drawImage(gm.assetLoader.buttonImage, (int) (120 * gm.scale), (int) (220 * gm.scale), (int) (100 * gm.scale), (int) (20 * gm.scale), this);
                g2.setFont(gm.assetLoader.customFont.deriveFont((float) (7 * gm.scale)));
                g2.drawString("Back to menu", (int) (128 * gm.scale), (int) (234 * gm.scale));

                model.soundManager.gameSound.stopMusic();
            }
        }

        g2.dispose();
    }
}
