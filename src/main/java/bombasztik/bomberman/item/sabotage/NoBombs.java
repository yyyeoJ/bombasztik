package bombasztik.bomberman.item.sabotage;

import bombasztik.bomberman.entity.Player;
import bombasztik.bomberman.view.GuiManager;
import bombasztik.bomberman.item.Item;
import bombasztik.bomberman.model.GameModel;

import java.awt.*;

public class NoBombs extends Item {

    public NoBombs(double x, double y, GameModel model) {
        super(x, y, model);
    }

    /**
     * Handles the interaction between the player and the NoBombs item.
     * This method is called when a player intersects with the NoBombs item.
     * If the NoBombs item is visible (toShow is true), the method does the following:
     * Sets the intersecting player as the player of this NoBombs item.
     * Hides the NoBombs item (sets toShow to false).
     * Prepares the NoBombs item to make its effect (sets makeEffect to true).
     * Sets the player's hasNoBombs state to true.
     * Sets the player's hasPlaceAllBombs state to false.
     *
     * @param player the Player object that intersects with the NoBombs item
     */
    @Override
    public void playerIntersects(Player player) {
        if (toShow) {
            this.player = player;
            toShow = false;
            makeEffect = true;

            player.hasNoBombs = true;
            player.hasPlaceAllBombs = false;
            System.out.println("noBombs");
        }
    }

    /**
     * Draws the NoBombs item on the game screen.
     * This method is responsible for rendering the NoBombs item on the game screen.
     * It checks if the NoBombs item is supposed to be shown (toShow is true).
     * If so, it draws the image of the NoBombs item at the appropriate position on the game screen.
     * The position is calculated based on the x and y coordinates of the NoBombs item, the size of the tiles in the game model, and the scale of the GuiManager.
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
     * Updates the state of the NoBombs item.
     * This method is called to update the state of the NoBombs item.
     * If the NoBombs item is supposed to make its effect (makeEffect is true), the method does the following:
     * Checks if the timer equals the effectTime, or if the player is not alive, or if the player does not have the NoBombs effect.
     * If any of the above conditions are true, it resets the timer, removes the NoBombs effect from the player, and turns off the NoBombs item's makeEffect state.
     * Increments the timer.
     */
    @Override
    public void update() {
        if (makeEffect) {
            if (timer == effectTime || !player.alive || !player.hasNoBombs) {
                timer = 0;
                player.hasNoBombs = false;
                makeEffect = false;
            }

            timer++;
        }
    }
}
