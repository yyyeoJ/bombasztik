package bombasztik.bomberman.object;

import bombasztik.bomberman.entity.Entity;
import bombasztik.bomberman.view.GuiManager;
import bombasztik.bomberman.model.GameModel;

import java.awt.*;

public class Explosion extends SuperObject {
    public ExplosionType explosionType;
    public boolean animateBackwards = false;

    public Explosion(GameModel model, double x, double y, ExplosionType explosionType) {
        super(x, y, model);
        this.explosionType = explosionType;
        getSolidArea().setBounds(3, 3, 14, 14);
    }

    /**
     * This method is used to update the state of the explosion.
     * It increments the spriteCounter and updates the spriteNum based on certain conditions.
     * It also handles the animation direction and visibility of the explosion.
     */
    public void update() {
        spriteCounter++;

        // Change explosion sprite every 4 frames
        if (spriteCounter == 4) {
            if (animateBackwards) {
                spriteNum--;
            } else {
                spriteNum++;
            }
            spriteCounter = 0;
        }

        // If the explosion reaches the first sprite, stop animating
        if (spriteNum == -1) {
            toShow = false;
        }

        // If the explosion reaches the middle sprite, start animating backwards
        if (spriteNum == 3) {
            animateBackwards = true;
        }
    }

    /**
     * This method is invoked when the explosion collides with an entity.
     * It checks if the entity is not invincible and if the explosion animation has started (spriteNum > -1).
     * If these conditions are met, it starts the death animation for the entity and plays the death sound effect.
     *
     * @param entity The entity that the explosion collides with.
     */
    @Override
    public void collidesWithEntity(Entity entity) {
        if (!entity.invincible && spriteNum > -1) {
            if (model.soundManager != null) {
                if (!entity.startDeathAnimation) {
                    model.soundManager.deathSound.playSoundEffect();
                }
            }

            entity.startDeathAnimation = true;
        }
    }

    /**
     * This method is used to draw the explosion on the screen.
     * It checks if the explosion is set to be shown (toShow is true).
     * If so, it sets the active image to the appropriate explosion sprite based on the sprite number and explosion type.
     * Then, it draws the image at the appropriate location on the screen, scaled according to the GuiManager's scale.
     *
     * @param g2 The Graphics2D object used for drawing.
     * @param gm The GuiManager object used for managing the game's GUI.
     */
    @Override
    public void draw(Graphics2D g2, GuiManager gm) {
        if (toShow) {
            activeImage = gm.assetLoader.explosions[spriteNum][explosionType.ordinal()];
            g2.drawImage(activeImage, (int) ((x + model.tileSize) * gm.scale), (int) ((y + model.tileSize) * gm.scale), gm.view.scaledTileSize, gm.view.scaledTileSize, null);
        }
    }
}
