package bombasztik.bomberman.object;

import bombasztik.bomberman.view.GuiManager;
import bombasztik.bomberman.model.GameModel;

import java.awt.*;

public class Path extends SuperObject {

    public Path(double x, double y, GameModel model) {
        super(x, y, model);
    }

    /**
     * This method is used to draw the path on the screen.
     * It sets the active image to the appropriate path sprite based on the selected map in the model.
     * Then, it draws the image at the appropriate location on the screen, scaled according to the GuiManager's scale.
     *
     * @param g2 The Graphics2D object used for drawing.
     * @param gm The GuiManager object used for managing the game's GUI.
     */
    @Override
    public void draw(Graphics2D g2, GuiManager gm) {
        activeImage = gm.assetLoader.paths[model.selectedMap - 1];
        g2.drawImage(activeImage, (int) ((x + model.tileSize) * gm.scale), (int) ((y + model.tileSize) * gm.scale), gm.view.scaledTileSize, gm.view.scaledTileSize, null);
    }
}
