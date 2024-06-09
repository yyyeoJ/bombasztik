package bombasztik.bomberman.item.sabotage;

import bombasztik.bomberman.entity.Player;
import bombasztik.bomberman.view.GuiManager;
import bombasztik.bomberman.item.Item;
import bombasztik.bomberman.model.GameModel;

import java.awt.*;

public class PlaceAllBombs extends Item {

    public PlaceAllBombs(double x, double y, GameModel model) {
        super(x, y, model);
    }

    /**
     * Handles the interaction between the player and the PlaceAllBombs item.
     * This method is called when a player intersects with the PlaceAllBombs item.
     * If the PlaceAllBombs item is visible (toShow is true), the method does the following:
     * Sets the intersecting player as the player of this PlaceAllBombs item.
     * Hides the PlaceAllBombs item (sets toShow to false).
     * Prepares the PlaceAllBombs item to make its effect (sets makeEffect to true).
     * Sets the player's hasPlaceAllBombs state to true.
     * Sets the player's hasNoBombs state to false.
     *
     * @param player the Player object that intersects with the PlaceAllBombs item
     */
    @Override
    public void playerIntersects(Player player) {
        if (toShow) {
            this.player = player;
            toShow = false;
            makeEffect = true;

            player.hasPlaceAllBombs = true;
            player.hasNoBombs = false;
            System.out.println("placeAllBombs");
        }
    }

    /**
     * Draws the PlaceAllBombs item on the game screen.
     * This method is responsible for rendering the PlaceAllBombs item on the game screen.
     * It checks if the PlaceAllBombs item is supposed to be shown (toShow is true).
     * If so, it draws the image of the PlaceAllBombs item at the appropriate position on the game screen.
     * The position is calculated based on the x and y coordinates of the PlaceAllBombs item, the size of the tiles in the game model, and the scale of the GuiManager.
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
     * Updates the state of the PlaceAllBombs item.
     * This method is called to update the state of the PlaceAllBombs item.
     * If the PlaceAllBombs item is supposed to make its effect (makeEffect is true), the method does the following:
     * Checks if the player has placed fewer bombs than their limit. If so, it sets the player's placeBombPressed state to true.
     * Checks if the timer equals 6 times the frames per second of the model, or if the player is not alive, or if the player does not have the PlaceAllBombs effect.
     * If any of the above conditions are true, it resets the timer, removes the PlaceAllBombs effect from the player, and turns off the PlaceAllBombs item's makeEffect state.
     * Increments the timer.
     */
    @Override
    public void update() {
        if (makeEffect) {
            // If player has bomb place it
            if (player.bombsPlaced < player.bombLimit) {
                player.setPlaceBombPressed(true);
            }

            if (timer == 6 * model.FPS || !player.alive || !player.hasPlaceAllBombs) {
                timer = 0;
                makeEffect = false;
                player.hasPlaceAllBombs = false;
            }

            timer++;
        }
    }
}
