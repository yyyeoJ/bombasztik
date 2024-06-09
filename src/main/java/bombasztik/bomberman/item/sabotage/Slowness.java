package bombasztik.bomberman.item.sabotage;

import bombasztik.bomberman.entity.Player;
import bombasztik.bomberman.view.GuiManager;
import bombasztik.bomberman.item.Item;
import bombasztik.bomberman.model.GameModel;

import java.awt.*;

public class Slowness extends Item {

    public Slowness(double x, double y, GameModel model) {
        super(x, y, model);
    }

    /**
     * Handles the interaction between the player and the Slowness item.
     * This method is called when a player intersects with the Slowness item.
     * If the Slowness item is visible (toShow is true), the method does the following:
     * Sets the intersecting player as the player of this Slowness item.
     * Hides the Slowness item (sets toShow to false).
     * Prepares the Slowness item to make its effect (sets makeEffect to true).
     * Sets the player's hasSlowness state to true.
     *
     * @param player the Player object that intersects with the Slowness item
     */
    @Override
    public void playerIntersects(Player player) {
        if (toShow) {
            this.player = player;
            toShow = false;
            makeEffect = true;

            player.hasSlowness = true;
            System.out.println("slowness");
        }
    }

    /**
     * Draws the Slowness item on the game screen.
     * This method is responsible for rendering the Slowness item on the game screen.
     * It checks if the Slowness item is supposed to be shown (toShow is true).
     * If so, it draws the image of the Slowness item at the appropriate position on the game screen.
     * The position is calculated based on the x and y coordinates of the Slowness item, the size of the tiles in the game model, and the scale of the GuiManager.
     * The size of the drawn image is the scaled size of a tile.
     *
     * @param g2 the Graphics2D object used for drawing
     * @param gm the GuiManager object used for managing the game's graphical user interface
     */
    @Override
    public void draw(Graphics2D g2, GuiManager gm) {
        if (toShow) {
            g2.drawImage(gm.assetLoader.sabotage, (int) ((x + model.tileSize) * gm.scale), (int) ((y + model.tileSize) * gm.scale), gm.view.scaledTileSize, gm.view.scaledTileSize, null);
        }
    }

    /**
     * Updates the state of the Slowness item.
     * This method is called to update the state of the Slowness item.
     * If the Slowness item is supposed to make its effect (makeEffect is true), the method does the following:
     * Sets the player's actual speed to their slow speed.
     * Checks if the timer equals the effect time, or if the player is not alive, or if the player does not have the Slowness effect.
     * If any of the above conditions are true, it checks if the player has the PlusSpeed effect.
     * If so, it sets the player's actual speed to their fast speed. Otherwise, it sets the player's actual speed to their original speed.
     * Resets the timer, removes the Slowness effect from the player, and turns off the Slowness item's makeEffect state.
     * Increments the timer.
     */
    @Override
    public void update() {
        if (makeEffect) {
            player.actualSpeed = player.getSlowSpeed();

            if (timer == effectTime || !player.alive || !player.hasSlowness) {
                if (player.hasPlusSpeed) {
                    player.actualSpeed = player.getFastSpeed();
                } else {
                    player.actualSpeed = player.getOriginalSpeed();
                }

                timer = 0;
                player.hasSlowness = false;
                makeEffect = false;
            }

            timer++;
        }
    }
}
