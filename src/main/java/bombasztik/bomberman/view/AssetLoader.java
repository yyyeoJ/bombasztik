package bombasztik.bomberman.view;

import bombasztik.bomberman.entity.Direction;
import bombasztik.bomberman.object.ExplosionType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class AssetLoader {
    // Background images
    public BufferedImage backgroundImageNormal, backgroundImageIce, backgroundImageLava;
    public BufferedImage gameOverbackground, roundOverBackground;

    // Scoreboard images
    public BufferedImage scoreboard, scoreboardPlayer3Disabled;
    public BufferedImage newGameBackground;
    public BufferedImage menuBackground;

    public BufferedImage buttonImage;

    // Number of themes
    private final int themeSize = Theme.values().length;

    // Path
    public BufferedImage[] paths = new BufferedImage[themeSize];

    // Wall
    private final int numberOfWalls = 3;
    public BufferedImage[][] walls = new BufferedImage[themeSize][numberOfWalls];

    // Powerups and sabotage
    public BufferedImage detonator, ghost, invincible, placebox, plusbomb, plusrange, plusspeed, sabotage;

    // Boxes
    private final int numberOfBoxes = 8;
    public BufferedImage[][] boxes = new BufferedImage[themeSize][numberOfBoxes];

    // Bombs
    private final int numberOfBombTypes = 3;
    private final int numberOfBombImages = 5;
    public BufferedImage[][] bombs = new BufferedImage[numberOfBombTypes][numberOfBombImages];

    // Explosion
    private final int numberOfExplosionSizes = 4;
    private final int numberOfExplosionTypes = 7;
    public BufferedImage[][] explosions = new BufferedImage[numberOfExplosionSizes][numberOfExplosionTypes];

    // Players
    private final int numberOfPlayers = 3;
    private final int normalOrArmored = 3;
    private final int numberOfDirections = 4;
    private final int numberOfSprites = 2;
    public BufferedImage[][][][] players = new BufferedImage[numberOfPlayers][normalOrArmored][numberOfDirections][numberOfSprites];

    private final int numberOfDieImages = 7;
    public BufferedImage[][] playersDieImages = new BufferedImage[numberOfPlayers][numberOfDieImages];

    // Monsters
    public BufferedImage[][][] monsters = new BufferedImage[themeSize][numberOfDirections][numberOfSprites];
    public BufferedImage monsterDieImage;

    public Font customFont;


    /**
     * Creates an AssetLoader class that loads every asset at once, this optimizes rendering speed.
     *
     * @throws  IOException
     * @throws  FontFormatException
     */
    public AssetLoader() throws IOException, FontFormatException {

        buttonImage = loadImage("/menu/button-normal.png");


        // Custom font
        InputStream fontInputStream = getClass().getResourceAsStream("/PressStart2P-Regular.ttf");

        assert fontInputStream != null;
        customFont = Font.createFont(Font.TRUETYPE_FONT, fontInputStream);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        ge.registerFont(customFont);

        // Load background images
        backgroundImageNormal = loadImage("/menu/game-background-normal.png");
        backgroundImageIce = loadImage("/menu/game-background-ice.png");
        backgroundImageLava = loadImage("/menu/game-background-lava.png");
        newGameBackground = loadImage("/menu/newgame-background.png");
        menuBackground = loadImage("/menu/menu-background.png");
        gameOverbackground = loadImage("/menu/gameover-background.png");
        roundOverBackground = loadImage("/menu/roundover-background.png");

        scoreboard = loadImage("/menu/scoreboard.png");
        scoreboardPlayer3Disabled = loadImage("/menu/scoreboard-disabledplayer3.png");

        // Load path images
        for (Theme theme : Theme.values()) {
            paths[theme.ordinal()] = loadImage("/tiles/path-" + theme.name().toLowerCase() + ".png");
        }

        // Load wall images
        for (Theme theme : Theme.values()) {
            for (int i = 0; i < walls[theme.ordinal()].length; i++) {
                walls[theme.ordinal()][i] = loadImage("/tiles/wall-" + theme.name().toLowerCase() + "-" + (i + 1) + ".png");
            }
        }

        // Load powerup images
        invincible = loadImage("/powerups/invincible.png");
        placebox = loadImage("/powerups/placebox.png");
        plusbomb = loadImage("/powerups/plusbomb.png");
        plusspeed = loadImage("/powerups/plusspeed.png");
        detonator = loadImage("/powerups/detonator.png");
        ghost = loadImage("/powerups/ghost.png");
        plusrange = loadImage("/powerups/plusrange.png");

        // Load sabotage image
        sabotage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/powerups/sabotage.png")));

        // Load box images
        for (Theme theme : Theme.values()) {
            for (int i = 0; i < boxes[theme.ordinal()].length; i++) {
                boxes[theme.ordinal()][i] = loadImage("/box/box-" + theme.name().toLowerCase() + "-" + (i + 1) + ".png");
            }
        }

        // Load bomb images
        for (int i = 0; i < bombs.length; i++) {
            for (int j = 0; j < bombs[i].length; j++) {
                if (j % 5 >= 3) {
                    bombs[i][j] = loadImage("/bomb/bomb" + (i + 1) + "-detonator-" + (j - 2) + ".png");
                } else {
                    bombs[i][j] = loadImage("/bomb/bomb" + (i + 1) + "-" + (j + 1) + ".png");
                }
            }
        }

        // Load explosion images
        for (int i = 0; i < explosions.length; i++) {
            for (ExplosionType et : ExplosionType.values()) {
                explosions[i][et.ordinal()] = loadImage("/explosion/explosion-" + (i + 1) + "-" + et.name().toLowerCase() + ".png");
            }
        }

        // Load player images
        for (int i = 0; i < players.length; i++) {
            for (int j = 0; j < players[i].length; j++) {
                for (Direction direction : Direction.values()) {
                    for (int k = 0; k < players[i][j][direction.ordinal()].length; k++) {
                        if (j == 0) {
                            players[i][j][direction.ordinal()][k] = loadImage("/player/player" + (i + 1) + "-" + direction.name().toLowerCase() + (k + 1) + ".png");
                        } else if (j == 1) {
                            players[i][j][direction.ordinal()][k] = loadImage("/player/player" + (i + 1) + "-armored-" + direction.name().toLowerCase() + (k + 1) + ".png");
                        } else if (j == 2) {
                            players[i][j][direction.ordinal()][k] = loadImage("/player/player" + (i + 1) + "-armored2-" + direction.name().toLowerCase() + (k + 1) + ".png");
                        }
                    }
                }
            }

            // Load player die images
            for (int j = 0; j < playersDieImages[i].length; j++) {
                playersDieImages[i][j] = loadImage("/player/player" + (i + 1) + "-die" + (j + 1) + ".png");
            }
        }

        // Load monster images
        for (Theme theme : Theme.values()) {
            for (Direction direction : Direction.values()) {
                monsters[theme.ordinal()][direction.ordinal()][0] = loadImage("/monster/monster-" + theme.name().toLowerCase() + "-" + direction.name().toLowerCase() + "1.png");
                monsters[theme.ordinal()][direction.ordinal()][1] = loadImage("/monster/monster-" + theme.name().toLowerCase() + "-" + direction.name().toLowerCase() + "2.png");
            }
        }

        monsterDieImage = loadImage("/monster/monster-dead.png");
    }

    /**
     * Loads an Image provded as a relative path.
     *
     * @param path A string that contains the relative path to the Image to be loaded.
     *
     * @throws  IOException
     *
     */
    BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(path)));
    }

    /**
     * Loads an Image, scales it up, and returns it as an Icon
     *
     * @param imagePath A string that contains the relative path to the Image to be loaded.
     * @param scale the percentage of scaling as a number (1.5 = 150%)
     *
     * @throws  IOException
     *
     */
    public static ImageIcon scaleImage(String imagePath, double scale) throws IOException {
        ImageIcon scaledIcon;

        // Load the image
        Image originalImage = ImageIO.read(Objects.requireNonNull(NewGamePanel.class.getResource(imagePath)));
        int width = originalImage.getWidth(null);
        int height = originalImage.getHeight(null);

        // Scale the image
        int scaledWidth = (int) (width * scale);
        int scaledHeight = (int) (height * scale);
        Image scaledImage = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

        // Create a scaled ImageIcon
        scaledIcon = new ImageIcon(scaledImage);

        return scaledIcon;
    }
}
