package bombasztik.bomberman.entity;

import bombasztik.bomberman.view.GuiManager;
import bombasztik.bomberman.model.GameModel;
import bombasztik.bomberman.object.SuperObject;

import java.awt.*;
import java.util.Random;

public class Monster extends Entity {
    private final Random random = new Random();

    public Monster(GameModel model, double x, double y, int id) {
        super(model, x, y, id);
        setSolidArea(new Rectangle(0, 0, 20, 20));
        actualSpeed = 1;
        randomDirection();
    }

    /**
     * This method determines whether the monster should change its direction.
     * The monster has a 50% chance to try to change its direction when it's at the center of a tile.
     *
     * @return boolean - true if the monster should change direction, false otherwise
     */
    private boolean shouldChangeDirection() {
        // Calculate if dragon is at center
        int dragonX = getColIndex();
        int dragonY = getRowIndex();
        SuperObject object = getModel().getMap().layout[dragonY][dragonX];

        boolean sameValues = getSolidArea().getX() == object.getSolidArea().getX() &&
                getSolidArea().getY() == object.getSolidArea().getY() &&
                getSolidArea().getWidth() == object.getSolidArea().getWidth() &&
                getSolidArea().getHeight() == object.getSolidArea().getHeight();

        return random.nextDouble() < 0.5 && sameValues;
    }

    /**
     * This method is used to change the direction of the monster if it is null or if it hits a wall.
     * It first generates a random direction and checks if the monster's current direction is null.
     * If it is, it sets the monster's direction to the new random direction.
     * It then calculates the new position of the monster based on the new direction.
     * If the new direction is different from the current direction and the new position is not colliding with any object, box, or bomb,
     * it sets the monster's direction to the new direction.
     */
    private void randomDirection() {
        Direction newDirection = Direction.values()[random.nextInt(Direction.values().length)];

        if (direction == null) {
            direction = newDirection;
            return;
        }

        int entityX = getColIndex();
        int entityY = getRowIndex();

        switch (newDirection) {
            case UP -> entityY--;
            case DOWN -> entityY++;
            case LEFT -> entityX--;
            case RIGHT -> entityX++;
        }

        if (newDirection != direction && !(getModel().getMap().layout[entityY][entityX].collision) && getModel().getMap().layout[entityY][entityX].box == null && !getModel().getMap().layout[entityY][entityX].hasBomb) {
            direction = newDirection;
        }
    }

    /**
     * This method is used to change the direction of the monster randomly to make it unpredictable.
     * It works like the function responsible for changing directions when hitting a wall,
     * Except it has only a 10% chance for the monster to make a 180-degree turn.
     */
    private void changeDirectionRandomly(){
        Direction newDirection = Direction.values()[random.nextInt(Direction.values().length)];
        int entityX = getColIndex();
        int entityY = getRowIndex();

        switch (newDirection) {
            case UP -> entityY--;
            case DOWN -> entityY++;
            case LEFT -> entityX--;
            case RIGHT -> entityX++;
        }
        Random random = new Random();
        if(random.nextDouble() < 0.9){
            if (newDirection != direction.getOpposite() && newDirection != direction && !(getModel().getMap().layout[entityY][entityX].collision) && getModel().getMap().layout[entityY][entityX].box == null && !getModel().getMap().layout[entityY][entityX].hasBomb) {
                direction = newDirection;
            }
        }
        else{
            randomDirection();
        }
    }



    /**
     * This method is used to move the monster in the direction it is facing.
     * It first checks the direction of the monster and moves it accordingly.
     */
    private void moveInDirection() {
        switch (direction) {
            case Direction.UP -> y -= actualSpeed;
            case Direction.DOWN -> y += actualSpeed;
            case Direction.LEFT -> x -= actualSpeed;
            case Direction.RIGHT -> x += actualSpeed;
        }
    }

    /**
     * This method is used to update the sprite of the monster.
     * It increments the sprite counter and changes the sprite every 10 frames.
     */
    private void updateSprite() {
        spriteCounter++;

        // Change monster sprite every 10 frames
        if (spriteCounter > 10) {
            spriteNum = spriteNum == 0 ? 1 : 0;
            spriteCounter = 0;
        }
    }

    /**
     * This method is responsible for moving the monster.
     * It first checks for any collisions with other entities or objects.
     * If there are no collisions, it checks if the monster should change its direction.
     * If it should, it changes the direction of the monster.
     * Then, it moves the monster in the current direction.
     * If there are collisions, it changes the direction of the monster.
     * Finally, it updates the sprite of the monster.
     */
    public void move() {
        // Check collisions
        collisionOn = false;
        getModel().collH.checkEntityCollision(this);
        getModel().collH.checkEntityObjectCollision(this);

        if (!collisionOn) {
            if (shouldChangeDirection()) {
                changeDirectionRandomly();
            }

            moveInDirection();
        } else {
            randomDirection();
        }

        updateSprite();
    }

    /**
     * This method is used to update the state of the monster.
     * If the monster is alive, it checks if the death animation should start.
     * If the death animation should start, it increments the death sprite counter.
     * If the death sprite counter is greater than or equal to 30, it calls the die method to kill the monster.
     * If the death animation should not start, it calls the move method to move the monster.
     */
    @Override
    public void update() {
        if (alive) {
            if (startDeathAnimation) {
                if (dieSpriteCounter < 30) dieSpriteCounter++;
                else die();
            } else {
                move();
            }
        }
    }

    /**
     * This method is used to draw the monster on the game screen.
     * If the monster is alive, it first checks if the death animation should start.
     * If the death animation should start, it sets the active image to the monster's death image.
     * If the death animation should not start, it sets the active image to the monster's current sprite
     * based on the selected map and the monster's direction.
     * It then draws the active image at the monster's current position, scaled according to the game's scale.
     *
     * @param g2 Graphics2D object used for drawing on the game screen.
     * @param gm GuiManager object used for managing the game's GUI.
     */
    @Override
    public void draw(Graphics2D g2, GuiManager gm) {
        if (alive) {
            activeImage = startDeathAnimation ? gm.assetLoader.monsterDieImage : gm.assetLoader.monsters[getModel().selectedMap - 1][direction.ordinal()][spriteNum];
            g2.drawImage(activeImage, (int) ((x + getModel().tileSize) * gm.scale), (int) ((y + getModel().tileSize) * gm.scale), gm.view.scaledTileSize, gm.view.scaledTileSize, null);
        }
    }
}
