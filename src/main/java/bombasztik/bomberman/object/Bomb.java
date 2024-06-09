package bombasztik.bomberman.object;

import bombasztik.bomberman.entity.Entity;
import bombasztik.bomberman.entity.Player;
import bombasztik.bomberman.view.GuiManager;
import bombasztik.bomberman.item.Item;
import bombasztik.bomberman.model.GameModel;

import java.awt.*;
import java.util.ArrayList;

public class Bomb extends SuperObject {
    public Player owner;
    public int range;
    public boolean isDetonated = false;

    public Bomb(double x, double y, GameModel model, Player owner) {
        super(x, y, model);
        this.owner = owner;
        range = owner.hasLessRange ? 1 : owner.bombRange;
    }

    /**
     * Updates the animation of the bomb.
     * Increments the sprite counter.
     * If the sprite counter reaches 30, it resets the counter and increments the sprite number if the bomb has not detonated.
     * If the sprite number reaches 3, it triggers the bomb to explode.
     */
    private void updateAnimation() {
        spriteCounter++;

        // Change bomb sprite every 30 frames
        if (spriteCounter == 30) {
            if (!isDetonated) {
                spriteNum++;
            }

            spriteCounter = 0;
        }

        // Explode bomb when it reaches the last sprite
        if (spriteNum == 3) {
            explode();
        }
    }

    /**
     * Updates the state of the bomb.
     * If the owner of the bomb is dead and the bomb
     * is set to detonate, it explodes.
     * Checks for collision with other entities in the game world and updates the collision status accordingly.
     * Finally, updates the animation of the bomb.
     * If the owner of the bomb is dead and the bomb is set to detonate, it explodes,
     * affecting its surroundings.
     */
    public void update() {
        // If owner is dead, explode his bombs
        if (!owner.alive && isDetonated) {
            explode();
        }

        Rectangle bombSolidArea = getSolidArea();

        boolean intersecting = false;
        for (Entity entity : model.entities) {
            Rectangle entitySolidArea = entity.getSolidArea();


            if (bombSolidArea.intersects(entitySolidArea)) {
                intersecting = true;
            }
        }

        this.collision = !intersecting;

        updateAnimation();
    }

    /**
     * Triggers the explosion of the bomb.
     * Plays the explosion sound effect if available in the sound manager.
     * Updates various properties and game elements
     * Creates explosion effects at the bomb's position and in its blast range, expanding in four directions.
     * Finally, adds the generated explosions to the game objects.
     */
    public void explode() {
        if (model.soundManager != null) {
            model.soundManager.explosionSound.playSoundEffect();
        }

        toShow = false;
        owner.bombsPlaced -= 1;

        int bombX = getColIndex();
        int bombY = getRowIndex();

        model.getMap().layout[bombY][bombX].hasBomb = false;

        model.explosions.add(new Explosion(model, (bombX) * model.tileSize, (bombY) * model.tileSize, ExplosionType.CENTER));

        ArrayList<Point> topPositions = new ArrayList<>();
        ArrayList<Point> bottomPositions = new ArrayList<>();
        ArrayList<Point> leftPositions = new ArrayList<>();
        ArrayList<Point> rightPositions = new ArrayList<>();

        for (int i = 1; i <= range; i++) {
            // top
            topPositions.add(new Point((bombX) * model.tileSize, (bombY - i) * model.tileSize));
            // bottom
            bottomPositions.add(new Point((bombX) * model.tileSize, (bombY + i) * model.tileSize));
            // left
            leftPositions.add(new Point((bombX - i) * model.tileSize, (bombY) * model.tileSize));
            // right
            rightPositions.add(new Point((bombX + i) * model.tileSize, (bombY) * model.tileSize));
        }

        // top
        handleExplosions(topPositions, ExplosionType.TOP, ExplosionType.VERTICAL);
        // bottom
        handleExplosions(bottomPositions, ExplosionType.BOTTOM, ExplosionType.VERTICAL);
        // left
        handleExplosions(leftPositions, ExplosionType.LEFT, ExplosionType.HORIZONTAL);
        // right
        handleExplosions(rightPositions, ExplosionType.RIGHT, ExplosionType.HORIZONTAL);

        // Add explosions to objects
        model.objects.addAll(model.explosions);
    }

    /**
     * Handles the placement and effects of explosions at specified positions.
     * Creates explosions at the given positions, considering whether each explosion
     * is at the end or middle of a blast, and updates game elements accordingly.
     *
     * @param positions   The list of positions where explosions should be placed.
     * @param endType     The type of explosion for the end of the blast.
     * @param middleType  The type of explosion for positions within the blast.
     */
    private void handleExplosions(ArrayList<Point> positions, ExplosionType endType, ExplosionType middleType) {
        for (int i = 0; i < positions.size(); i++) {
            Point pos = positions.get(i);

            // Get the tile index where the explosion is placed
            int tileRowIndex = pos.y / model.tileSize;
            int tileColIndex = pos.x / model.tileSize;

            if (model.getMap().layout[tileRowIndex][tileColIndex].collision) {
                break;
            }

            ExplosionType type = (i == positions.size() - 1) ? endType : middleType;
            model.explosions.add(new Explosion(model, pos.x, pos.y, type));

            // Explosion lerakás -> ha tileon van doboz, lerak, összetör, break;
            if (model.getMap().layout[tileRowIndex][tileColIndex].box != null) {
                // Box explosion
                model.getMap().layout[tileRowIndex][tileColIndex].box.startAnimation = true;
                model.getMap().layout[tileRowIndex][tileColIndex].box = null;
                break;
            }

            for (Item item : model.items) {
                int itemX = (int) (item.x / model.tileSize);
                int itemY = (int) (item.y / model.tileSize);
                if (tileRowIndex == itemY && tileColIndex == itemX) {
                    item.setToShow(false);
                }
            }
        }
    }

    /**
     * This method is used to determine the type of the bomb based on its range.
     *
     * @return int This returns the type of the bomb.
     */
    public int getBombType() {
        if (range <= 2) return 0;
        if (range == 3) return 1;
        return 2;
    }

    /**
     * This method is used to determine the sprite number of the bomb.
     *
     * @return int This returns the sprite number of the bomb.
     */
    public int getSpriteNum() {
        if (isDetonated) {
            if (spriteCounter < 15) return 3;
            return 4;
        }

        return spriteNum;
    }

    /**
     * This method is used to draw the bomb on the game screen.
     *
     * @param g2 The graphics object used to draw the bomb.
     * @param gm The GUI manager used to scale the bomb.
     */
    @Override
    public void draw(Graphics2D g2, GuiManager gm) {
        if (toShow) {
            activeImage = gm.assetLoader.bombs[getBombType()][getSpriteNum()];
            g2.drawImage(activeImage, (int) ((x + model.tileSize) * gm.scale), (int) ((y + model.tileSize) * gm.scale), gm.view.scaledTileSize, gm.view.scaledTileSize, null);
        }
    }
}
