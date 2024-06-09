package bombasztik.bomberman.object;

import bombasztik.bomberman.entity.Entity;
import bombasztik.bomberman.view.GuiManager;
import bombasztik.bomberman.model.GameModel;

import java.awt.*;

public class Wall extends SuperObject {
    public boolean isTopOrBottom = false;

    public Wall(double x, double y, GameModel model) {
        super(x, y, model);
        collision = true;
    }

    /**
     * This method is invoked when an entity collides with the wall.
     * It checks if the wall is solid and the entity is not a ghost.
     * If so, it performs corner smoothing on the entity.
     * If the entity is a ghost, it checks if the wall is a border wall.
     * If the wall is a border wall and the ghost entity intersects with it,
     * it sets the entity's collisionOn flag to true.
     *
     * @param entity The entity that collides with the wall.
     */
    @Override
    public void collidesWithEntity(Entity entity) {
        if (collision && !entity.ghost) {
            cornerSmoothing(entity);
        }

        // If the player is a ghost, it can pass through walls
        if (entity.ghost) {
            // Check if the wall is a border wall (e.g., if it's on the edge of the map)
            boolean isBorderWall = (x == 0 || x == model.tileSize * (model.cols - 1) || y == 0 || y == model.tileSize * (model.rows - 1));

            // If the is a border wall and the player intersects with border wall, set collisionOn to true
            if (isBorderWall) {
                assert entity.nextSolidArea() != null;
                if (entity.nextSolidArea().intersects(getSolidArea())) {
                    entity.collisionOn = true;
                }
            }
        }
    }

    /**
     * This method is used to draw the wall on the screen.
     * It sets the active image to the appropriate wall sprite based on the selected map in the model.
     * Then, it draws the image at the appropriate location on the screen, scaled according to the GuiManager's scale.
     *
     * @param g2 The Graphics2D object used for drawing.
     * @param gm The GuiManager object used for managing the game's GUI.
     */
    @Override
    public void draw(Graphics2D g2, GuiManager gm) {
        activeImage = gm.assetLoader.walls[model.selectedMap - 1][isTopOrBottom ? 1 : 2];
        isTopOrBottom = !isTopOrBottom;
        g2.drawImage(activeImage, (int) ((x + model.tileSize) * gm.scale), (int) ((y + model.tileSize) * gm.scale), gm.view.scaledTileSize, gm.view.scaledTileSize, null);
    }
}
