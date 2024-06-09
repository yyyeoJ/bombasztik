package bombasztik.bomberman.item.powerup;

import bombasztik.bomberman.entity.Player;
import bombasztik.bomberman.view.GuiManager;
import bombasztik.bomberman.item.Item;
import bombasztik.bomberman.model.GameModel;

import java.awt.*;

public class PlusRange extends Item {

    public PlusRange(double x, double y, GameModel model) {
        super(x, y, model);
    }

    /**
     * Draws the PlusRange item on the game screen.
     * This method is responsible for rendering the PlusRange item on the game screen.
     * It checks if the PlusRange item is supposed to be shown (toShow is true).
     * If so, it draws the image of the PlusRange item at the appropriate position on the game screen.
     * The position is calculated based on the x and y coordinates of the PlusRange item, the size of the tiles in the game model, and the scale of the GuiManager.
     * The size of the drawn image is the scaled size of a tile.
     *
     * @param g2 the Graphics2D object used for drawing
     * @param gm the GuiManager object used for managing the game's graphical user interface
     */
    @Override
    public void draw(Graphics2D g2, GuiManager gm) {
        if (toShow) {
            g2.drawImage(gm.assetLoader.plusrange, (int) ((x + model.tileSize) * gm.scale), (int) ((y + model.tileSize) * gm.scale), gm.view.scaledTileSize, gm.view.scaledTileSize, null);
        }
    }

    /**
     * Handles the interaction between the player and the PlusRange item.
     * This method is called when a player intersects with the PlusRange item.
     * If the PlusRange item is visible (toShow is true), the method does the following:
     * Sets the intersecting player as the player of this PlusRange item.
     * Hides the PlusRange item (sets toShow to false).
     * Prepares the PlusRange item to make its effect (sets makeEffect to true).
     *
     * @param player the Player object that intersects with the PlusRange item
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
     * Updates the state of the PlusRange item.
     * This method is called to update the state of the PlusRange item.
     * If the PlusRange item is supposed to make its effect (makeEffect is true), the method does the following:
     * Turns off the PlusRange item's makeEffect state (sets makeEffect to false).
     * Increases the player's bombRange by 1.
     */
    @Override
    public void update() {
        if (makeEffect) {
            // Run once
            makeEffect = false;

            player.bombRange += 1;
        }
    }
}
