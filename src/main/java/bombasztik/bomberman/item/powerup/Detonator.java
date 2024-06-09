package bombasztik.bomberman.item.powerup;

import bombasztik.bomberman.entity.Player;
import bombasztik.bomberman.view.GuiManager;
import bombasztik.bomberman.item.Item;
import bombasztik.bomberman.model.GameModel;

import java.awt.*;
import java.io.IOException;

public class Detonator extends Item {

    public Detonator(double x, double y, GameModel model) {
        super(x, y, model);
    }

    /**
     * Handles the interaction between the player and the Detonator item.
     * This method is called when a player intersects with the Detonator item.
     * If the Detonator item is visible, the method sets the player as the intersecting player,
     * hides the Detonator item, and prepares the Detonator item to make its effect.
     *
     * @param player the Player object that intersects with the Detonator item
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
     * Draws the Detonator item on the game screen.
     * This method is responsible for rendering the Detonator item on the game screen.
     * It checks if the Detonator item is supposed to be shown.
     * If so, it draws the image of the Detonator item at the appropriate position on the game screen.
     * The position is calculated based on the x and y coordinates of the Detonator item,
     * the size of the tiles in the game model, and the scale of the GuiManager.
     * The size of the drawn image is the scaled size of a tile.
     *
     * @param g2 the Graphics2D object used for drawing
     * @param gm the GuiManager object used for managing the game's graphical user interface
     */
    @Override
    public void draw(Graphics2D g2, GuiManager gm) {

        if (toShow) {
            g2.drawImage(gm.assetLoader.detonator, (int) ((x + model.tileSize) * gm.scale), (int) ((y + model.tileSize) * gm.scale), gm.view.scaledTileSize, gm.view.scaledTileSize, null);
        }
    }

    /**
     * Updates the state of the Detonator item.
     * This method is called to update the state of the Detonator item.
     * If the Detonator item is supposed to make its effect, the method runs once.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void update() throws IOException {
        if (makeEffect) {
            // Run once
            makeEffect = false;

            player.hasDetonator = true;
        }
    }
}
