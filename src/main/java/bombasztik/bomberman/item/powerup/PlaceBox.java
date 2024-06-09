package bombasztik.bomberman.item.powerup;

import bombasztik.bomberman.entity.Player;
import bombasztik.bomberman.view.GuiManager;
import bombasztik.bomberman.item.Item;
import bombasztik.bomberman.model.GameModel;

import java.awt.*;
import java.io.IOException;

public class PlaceBox extends Item {

    public PlaceBox(double x, double y, GameModel model) {
        super(x, y, model);
    }

    /**
     * Handles the interaction between the player and the PlaceBox item.
     * This method is called when a player intersects with the PlaceBox item.
     * If the PlaceBox item is visible (toShow is true), the method does the following:
     * Sets the intersecting player as the player of this PlaceBox item.
     * Hides the PlaceBox item (sets toShow to false).
     * Prepares the PlaceBox item to make its effect (sets makeEffect to true).
     *
     * @param player the Player object that intersects with the PlaceBox item
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
     * Draws the PlaceBox item on the game screen.
     * This method is responsible for rendering the PlaceBox item on the game screen.
     * It checks if the PlaceBox item is supposed to be shown (toShow is true).
     * If so, it draws the image of the PlaceBox item at the appropriate position on the game screen.
     * The position is calculated based on the x and y coordinates of the PlaceBox item, the size of the tiles in the game model, and the scale of the GuiManager.
     * The size of the drawn image is the scaled size of a tile.
     *
     * @param g2 the Graphics2D object used for drawing
     * @param gm the GuiManager object used for managing the game's graphical user interface
     */
    @Override
    public void draw(Graphics2D g2, GuiManager gm) {
        if (toShow) {
            g2.drawImage(gm.assetLoader.placebox, (int) ((x + model.tileSize) * gm.scale), (int) ((y + model.tileSize) * gm.scale), gm.view.scaledTileSize, gm.view.scaledTileSize, null);
        }
    }

    /**
     * Updates the state of the PlaceBox item.
     * This method is called to update the state of the PlaceBox item.
     * If the PlaceBox item is supposed to make its effect (makeEffect is true), the method does the following:
     * Turns off the PlaceBox item's makeEffect state (sets makeEffect to false).
     * Increases the player's boxLimit by 3.
     *
     * @throws IOException if an input or output exception occurred
     */
    @Override
    public void update() throws IOException {
        if (makeEffect) {
            // Run once
            makeEffect = false;

            player.boxLimit += 3;
        }
    }
}