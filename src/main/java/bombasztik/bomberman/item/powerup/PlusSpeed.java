package bombasztik.bomberman.item.powerup;

import bombasztik.bomberman.entity.Player;
import bombasztik.bomberman.view.GuiManager;
import bombasztik.bomberman.item.Item;
import bombasztik.bomberman.model.GameModel;

import java.awt.*;

public class PlusSpeed extends Item {

    public PlusSpeed(double x, double y, GameModel model) {
        super(x, y, model);
    }

    /**
     * Draws the PlusSpeed item on the game screen.
     * This method is responsible for rendering the PlusSpeed item on the game screen.
     * It checks if the PlusSpeed item is supposed to be shown (toShow is true).
     * If so, it draws the image of the PlusSpeed item at the appropriate position on the game screen.
     * The position is calculated based on the x and y coordinates of the PlusSpeed item, the size of the tiles in the game model, and the scale of the GuiManager.
     * The size of the drawn image is the scaled size of a tile.
     *
     * @param g2 the Graphics2D object used for drawing
     * @param gm the GuiManager object used for managing the game's graphical user interface
     */
    @Override
    public void draw(Graphics2D g2, GuiManager gm) {
        if (toShow) {
            g2.drawImage(gm.assetLoader.plusspeed, (int) ((x + model.tileSize) * gm.scale), (int) ((y + model.tileSize) * gm.scale), gm.view.scaledTileSize, gm.view.scaledTileSize, null);
        }
    }

    /**
     * Handles the interaction between the player and the PlusSpeed item.
     * This method is called when a player intersects with the PlusSpeed item.
     * If the PlusSpeed item is visible (toShow is true), the method does the following:
     * Sets the intersecting player as the player of this PlusSpeed item.
     * Hides the PlusSpeed item (sets toShow to false).
     * Prepares the PlusSpeed item to make its effect (sets makeEffect to true).
     *
     * @param player the Player object that intersects with the PlusSpeed item
     */
    @Override
    public void playerIntersects(Player player) {
        if (toShow) {
            this.player = player;
            toShow = false;
            makeEffect = true;
        }
    }

    /**
     * Updates the state of the PlusSpeed item.
     * This method is called to update the state of the PlusSpeed item.
     * If the PlusSpeed item is supposed to make its effect (makeEffect is true), the method does the following:
     * Turns off the PlusSpeed item's makeEffect state (sets makeEffect to false).
     * Sets the player's actual speed to the player's fast speed.
     * Sets the player's hasPlusSpeed state to true.
     * Sets the player's hasSlowness state to false.
     * This method is designed to run once per intersection with a player.
     */
    @Override
    public void update() {
        if (makeEffect) {
            // Run once
            makeEffect = false;

            player.actualSpeed = player.getFastSpeed();
            player.hasPlusSpeed = true;
            player.hasSlowness = false;
        }
    }
}
