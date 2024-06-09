package bombasztik.bomberman.object;

import bombasztik.bomberman.entity.Entity;
import bombasztik.bomberman.entity.Player;
import bombasztik.bomberman.view.GuiManager;
import bombasztik.bomberman.model.GameModel;

import java.awt.*;

public class Box extends SuperObject {
    public Player owner = null;
    public boolean isTopOrBottom = false;
    public boolean startAnimation = false;

    public Box(double x, double y, GameModel model) {
        super(x, y, model);
    }

    /**
     * This method is used to update the animation of the box.
     * It increments the spriteCounter and updates the spriteNum and spriteCounter based on certain conditions.
     * It also handles the disappearance of the box when it reaches the last sprite.
     */
    private void updateAnimation() {
        spriteCounter++;

        if (spriteCounter == 6) {
            spriteNum++;
            spriteCounter = 0;
        }

        // When box reaches the last sprite, it disappears
        if (spriteNum == 6) {
            toShow = false;

            if (owner != null) {
                owner.boxLimit += 1;
            }
        }
    }

    /**
     * This method is used to update the state of the box.
     * It updates the collision status of the box if it is placed by the player.
     * It also updates the animation of the box if it is set to start the animation.
     */
    public void update() {
        // Update box's collision if it's placed by player
        if (owner != null) {
            boolean intersecting = false;

            for (Entity entity : model.entities) {
                if (getSolidArea().intersects(entity.getSolidArea())) {
                    intersecting = true;
                }
            }

            this.collision = !intersecting;
        }

        if (startAnimation) {
            updateAnimation();
        }
    }

    /**
     * This method is used to determine the sprite index for the box.
     *
     * @return int This returns the sprite index.
     */
    private int getSpriteIndex() {
        if (startAnimation) {
            return spriteNum;
        }

        if (isTopOrBottom) {
            isTopOrBottom = false;
            return 6;
        }

        if (!collision) {
            return 0;
        }

        isTopOrBottom = true;
        return 7;
    }

/**
     * This method is used to draw the box on the screen.
     *
     * @param g2 The graphics object used to draw the box.
     * @param gm The GUI manager used to scale the box.
     */
    @Override
    public void draw(Graphics2D g2, GuiManager gm) {
        if (toShow) {
            activeImage = gm.assetLoader.boxes[model.selectedMap - 1][getSpriteIndex()];
            g2.drawImage(activeImage, (int) ((x + model.tileSize) * gm.scale), (int) ((y + model.tileSize) * gm.scale), gm.view.scaledTileSize, gm.view.scaledTileSize, null);
        }
    }
}
