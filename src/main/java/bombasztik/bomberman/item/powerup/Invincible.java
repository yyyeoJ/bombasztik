package bombasztik.bomberman.item.powerup;

import bombasztik.bomberman.entity.Player;
import bombasztik.bomberman.view.GuiManager;
import bombasztik.bomberman.item.Item;
import bombasztik.bomberman.model.GameModel;

import java.awt.*;

public class Invincible extends Item {

    public Invincible(double x, double y, GameModel model) {
        super(x, y, model);
    }

    /**
     * Handles the interaction between the player and the Invincible item.
     * This method is called when a player intersects with the Invincible item.
     * If the Invincible item is visible, the method hides the Invincible item.
     * It then checks if the player already has an Invincible item.
     * If so, it resets the timer of the existing Invincible item and turns off the player's invincibleAlert state, then returns.
     * If the player does not already have an Invincible item, it sets the intersecting player as the player of this Invincible item,
     * and prepares the Invincible item to make its effect.
     *
     * @param player the Player object that intersects with the Invincible item
     */
    @Override
    public void playerIntersects(Player player) {
        if (toShow) {
            toShow = false;

            for (Item item : model.items) {
                if (item instanceof Invincible && item.player == player) {
                    item.timer = 0;
                    player.invincibleAlert = false;
                    return;
                }
            }

            this.player = player;
            makeEffect = true;
        }
    }

    /**
     * Draws the Invincible item on the game screen.
     * This method is responsible for rendering the Invincible item on the game screen.
     * It checks if the Invincible item is supposed to be shown (toShow is true).
     * If so, it draws the image of the Invincible item at the appropriate position on the game screen.
     * The position is calculated based on the x and y coordinates of the Invincible item, the size of the tiles in the game model, and the scale of the GuiManager.
     * The size of the drawn image is the scaled size of a tile.
     *
     * @param g2 the Graphics2D object used for drawing
     * @param gm the GuiManager object used for managing the game's graphical user interface
     */
    @Override
    public void draw(Graphics2D g2, GuiManager gm) {
        if (toShow) {
            g2.drawImage(gm.assetLoader.invincible, (int) ((x + model.tileSize) * gm.scale), (int) ((y + model.tileSize) * gm.scale), gm.view.scaledTileSize, gm.view.scaledTileSize, null);
        }
    }

    /**
     * Updates the state of the Invincible item.
     * This method is called to update the state of the Invincible item.
     * If the Invincible item is supposed to make its effect (makeEffect is true), the method does the following:
     * Sets the player's invincible state to true.
     * Alerts the player when the timer reaches two-thirds of the effect time by setting the player's invincibleAlert state to true and playing a sound effect.
     * If the timer exceeds two-thirds of the effect time, it checks if there is another Invincible item for the player.
     * If so, it turns off the player's invincibleAlert state.
     * Resets the invincibility when the timer reaches the effect time or the player is not alive.
     * Increments the timer.
     */
    @Override
    public void update() {
        if (makeEffect) {
            player.invincible = true;

            // Alert player
            if (timer == effectTime - (effectTime / 3)) {
                player.invincibleAlert = true;
                if (model.soundManager != null) {
                    model.soundManager.alertSound.playSoundEffect();
                }
            }

            if (timer > effectTime - (effectTime / 3)) {
                player.invincibleAlert = true;

                // Turn off alert if there is another
                for (Item item : model.items) {
                    if (item instanceof Invincible && item.player == player && item != this) {
                        player.invincibleAlert = false;
                        break;
                    }
                }
            }

            // Reset invincibility
            if (timer == effectTime || !player.alive) {
                timer = 0;

                player.invincible = false;
                player.invincibleAlert = false;
                makeEffect = false;
            }

            timer++;
        }
    }
}

