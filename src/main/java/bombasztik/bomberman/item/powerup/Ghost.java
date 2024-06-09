package bombasztik.bomberman.item.powerup;

import bombasztik.bomberman.entity.Player;
import bombasztik.bomberman.view.GuiManager;
import bombasztik.bomberman.item.Item;
import bombasztik.bomberman.model.GameModel;
import bombasztik.bomberman.object.SuperObject;

import java.awt.*;

public class Ghost extends Item {

    public Ghost(double x, double y, GameModel model) {
        super(x, y, model);
    }

    /**
     * Handles the interaction between the player and the Ghost item.
     * If the Ghost item is visible, the method hides the Ghost item.
     * It then checks if the player already has a Ghost item.
     * If so, it resets the timer of the existing Ghost item and turns off the player's ghostAlert and blink states, then returns.
     * If the player does not already have a Ghost item, it sets the intersecting player as the player of this Ghost item,
     * turns off the player's blink state, and prepares the Ghost item to make its effect.
     *
     * @param player the Player object that intersects with the Ghost item
     */
    @Override
    public void playerIntersects(Player player) {
        if (toShow) {
            toShow = false;

            // Ha playernek van ezen kívül ghostja, annak a timer resetelése, ez nem csinál semmit
            for (Item item : model.items) {
                if (item instanceof Ghost && item.player == player) {
                    item.timer = 0;
                    player.ghostAlert = false;
                    player.blink = false;
                    return;
                }
            }

            this.player = player;
            player.blink = false;
            makeEffect = true;
        }
    }

    /**
     * Draws the Ghost item on the game screen.
     * This method is responsible for rendering the Ghost item on the game screen.
     * It checks if the Ghost item is supposed to be shown.
     * If so, it draws the image of the Ghost item at the appropriate position on the game screen.
     * The position is calculated based on the x and y coordinates of the Ghost item, the size of the tiles in the game model, and the scale of the GuiManager.
     * The size of the drawn image is the scaled size of a tile.
     *
     * @param g2 the Graphics2D object used for drawing
     * @param gm the GuiManager object used for managing the game's graphical user interface
     */
    @Override
    public void draw(Graphics2D g2, GuiManager gm) {
        if (toShow) {
            g2.drawImage(gm.assetLoader.ghost, (int) ((x + model.tileSize) * gm.scale), (int) ((y + model.tileSize) * gm.scale), gm.view.scaledTileSize, gm.view.scaledTileSize, null);
        }
    }

    /**
     * This method is called to update the state of the Ghost item.
     * If the Ghost item is supposed to make its effect (makeEffect is true), the method does the following:
     * Sets the player's ghost state to true.
     * Alerts the player when the timer reaches two-thirds of the effect time by setting the player's ghostAlert state to true and playing a sound effect.
     * Resets the ghost state when the timer reaches the effect time or the player is not alive.
     * Increments the timer.
     */
    @Override
    public void update() {
        if (makeEffect) {
            player.ghost = true;

            // Alert player
            if (timer == effectTime - (effectTime / 3)) {
                player.ghostAlert = true;
                if (model.soundManager != null) {
                    model.soundManager.alertSound.playSoundEffect();
                }
            }

            // Reset ghost
            if (timer == effectTime || !player.alive) {
                if (!player.startDeathAnimation) {
                    player.ghost = false;
                }

                Rectangle playerSolidArea = player.getSolidArea();

                for (SuperObject[] row : model.getMap().layout) {
                    for (SuperObject o : row) {
                        if (o.collision || o.box != null) {
                            if (playerSolidArea.intersects(o.getSolidArea())) {
                                player.startDeathAnimation = true;
                                if (model.soundManager != null) {
                                    model.soundManager.deathSound.playSoundEffect();
                                }
                            }
                        }
                    }
                }

                timer = 0;
                player.ghostAlert = false;
                player.blink = false;
                makeEffect = false;
            }

            timer++;
        }
    }
}
