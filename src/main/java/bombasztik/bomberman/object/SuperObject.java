package bombasztik.bomberman.object;

import bombasztik.bomberman.entity.Direction;
import bombasztik.bomberman.entity.Entity;
import bombasztik.bomberman.entity.Player;
import bombasztik.bomberman.view.GuiManager;
import bombasztik.bomberman.model.GameModel;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class SuperObject {
    GameModel model;

    public double x;
    public double y;

    public int spriteCounter = 0;
    public int spriteNum = 0;

    public BufferedImage activeImage;
    private final Rectangle solidArea;

    public boolean collision = false;
    public boolean toShow = true;

    public Box box;
    public boolean hasBomb = false;

    public SuperObject(Double x, Double y, GameModel model) {
        this.model = model;
        this.x = x;
        this.y = y;

        solidArea = new Rectangle(0, 0, model.tileSize, model.tileSize);
    }

    /**
     * This is an abstract method that is used to draw the object on the screen.
     *
     * @param g2 The Graphics2D object used for drawing.
     * @param gm The GuiManager object used for managing the game's GUI.
     */
    public abstract void draw(Graphics2D g2, GuiManager gm);

    /**
     * This method is used to get the column index of the object on the game grid.
     * It calculates the index by dividing the object's x-coordinate by the size of a tile in the model.
     *
     * @return The column index of the object on the game grid.
     */
    public int getColIndex() {
        return (int) (x / model.tileSize);
    }

    /**
     * This method is used to get the row index of the object on the game grid.
     * It calculates the index by dividing the object's y-coordinate by the size of a tile in the model.
     *
     * @return The row index of the object on the game grid.
     */
    public int getRowIndex() {
        return (int) (y / model.tileSize);
    }

    /**
     * This method is used to get the solid area of the object.
     * It calculates the object's x and y coordinates by adding the object's x and y to the solid area's x and y respectively.
     * Then, it creates a new Rectangle with these coordinates and the width and height of the solid area.
     *
     * @return A Rectangle representing the solid area of the object.
     */
    public Rectangle getSolidArea() {
        double objectX = x + solidArea.x;
        double objectY = y + solidArea.y;

        return new Rectangle((int) objectX, (int) objectY, solidArea.width, solidArea.height);
    }

    /**
     * This method is used to handle the collision of this object with an entity.
     * If the entity is an instance of Player and there is a collision and the entity is not a ghost,
     * the cornerSmoothing method is called with the entity as an argument.
     * If the entity is not an instance of Player, the collisionOn attribute of the entity is set to true.
     *
     * @param entity The Entity object that this object may collide with.
     */
    public void collidesWithEntity(Entity entity) {
        if(entity instanceof Player){
            if (collision && !entity.ghost) {
                cornerSmoothing(entity);
            }
        }
        else{
            entity.collisionOn = true;
        }
    }

    /**
     * This method is used to check if the path at the given row and column index is empty.
     * It checks if the layout at the given index is not an instance of Wall,
     * if the box at the given index is null, and if the layout at the given index does not have a bomb.
     *
     * @param rowIndex The row index of the path to check.
     * @param colIndex The column index of the path to check.
     * @return A boolean value indicating whether the path at the given index is empty.
     */
    private boolean checkEmptyPath(int rowIndex, int colIndex) {
        return !(model.getMap().layout[rowIndex][colIndex] instanceof Wall) &&
                model.getMap().layout[rowIndex][colIndex].box == null &&
                !model.getMap().layout[rowIndex][colIndex].hasBomb;
    }

    /**
     * This method checks if the given row and column index is not on the border of the game grid.
     * The game grid is considered to have a border if the row or column index is either 0 or 14.
     *
     * @param rowIndex The row index to check.
     * @param colIndex The column index to check.
     * @return A boolean value indicating whether the given row and column index is not on the border of the game grid.
     */
    private boolean isNotBorder(int rowIndex, int colIndex) {
        return rowIndex != 0 && rowIndex != 14 && colIndex != 0 && colIndex != 14;
    }

    /**
     * This method is used to smooth the corners of the entity's movement.
     * It checks the intersection of the entity's corners with the object's corners.
     * Depending on the direction of the entity's movement and the corner of intersection, it adjusts the entity's position.
     * This is done to prevent the entity from getting stuck on the corners of the object.
     * The method also checks if the new position is not on the border of the game grid and if the path at the new position is empty.
     * If these conditions are met, the entity's position is adjusted and its collisionOn attribute is set to true.
     * If the entity's corners do not intersect with the object's corners, the entity's collisionOn attribute is set to true.
     *
     * @param entity The Entity object whose corners are to be smoothed.
     */
    public void cornerSmoothing(Entity entity) {
        // Entity 4 corner
        Rectangle entityUpperLeft = new Rectangle(entity.nextSolidArea().x + model.tileSize, entity.nextSolidArea().y + model.tileSize, 4, 4);
        Rectangle entityUpperRight = new Rectangle(entity.nextSolidArea().x + model.tileSize + entity.nextSolidArea().width - 4, entity.nextSolidArea().y + model.tileSize, 4, 4);
        Rectangle entityBottomLeft = new Rectangle(entity.nextSolidArea().x + model.tileSize, entity.nextSolidArea().y + model.tileSize + entity.nextSolidArea().height - 4, 4, 4);
        Rectangle entityBottomRight = new Rectangle(entity.nextSolidArea().x + model.tileSize + entity.nextSolidArea().width - 4, entity.nextSolidArea().y + model.tileSize + entity.nextSolidArea().height - 4, 4, 4);

        // Object 4 corner
        Rectangle objectUpperLeft = new Rectangle((int) x + solidArea.x + model.tileSize, (int) y + solidArea.y + model.tileSize, 4, 4);
        Rectangle objectUpperRight = new Rectangle((int) x + solidArea.x + model.tileSize + solidArea.width - 4, (int) y + solidArea.y + model.tileSize, 4, 4);
        Rectangle objectBottomLeft = new Rectangle((int) x + solidArea.x + model.tileSize, (int) y + solidArea.y + model.tileSize + solidArea.height - 4, 4, 4);
        Rectangle objectBottomRight = new Rectangle((int) x + solidArea.x + model.tileSize + solidArea.width - 4, (int) y + solidArea.y + model.tileSize + solidArea.height - 4, 4, 4);

        // Object index
        int objectRowIndex = (int) y / model.tileSize;
        int objectColIndex = (int) x / model.tileSize;

        // Up left corner
        if (entityUpperRight.intersects(objectUpperLeft) || entityBottomRight.intersects(objectUpperLeft) || entityBottomLeft.intersects(objectUpperLeft)) {
            // Comes down and goes into the bottom right -> down right
            if (entity.direction == Direction.DOWN && entityBottomRight.intersects(objectUpperLeft)) {
                if (isNotBorder(objectRowIndex, objectColIndex) &&
                        checkEmptyPath(objectRowIndex, objectColIndex - 1) &&
                        checkEmptyPath(objectRowIndex - 1, objectColIndex - 1)) {
                    entity.y += entity.getOriginalSpeed() * 0.4;
                    entity.x -= entity.getOriginalSpeed() * 0.6;
                    entity.collisionOn = true;

                    return;
                }
            }

            // Goes right and goes into the bottom left -> right up
            if (entity.direction == Direction.RIGHT && entityBottomRight.intersects(objectUpperLeft)) {
                if (isNotBorder(objectRowIndex, objectColIndex) &&
                        checkEmptyPath(objectRowIndex - 1, objectColIndex) &&
                        checkEmptyPath(objectRowIndex - 1, objectColIndex - 1)) {
                    entity.x += entity.getOriginalSpeed() * 0.4;
                    entity.y -= entity.getOriginalSpeed() * 0.6;
                    entity.collisionOn = true;

                    return;
                }
            }
        }

        // Up right corner
        if (entityUpperLeft.intersects(objectUpperRight) || entityBottomLeft.intersects(objectUpperRight) || entityBottomRight.intersects(objectUpperRight)) {
            // Comes down and goes into the bottom left -> down right
            if (entity.direction == Direction.DOWN && entityBottomLeft.intersects(objectUpperRight)) {
                if (isNotBorder(objectRowIndex, objectColIndex) &&
                        checkEmptyPath(objectRowIndex, objectColIndex + 1) &&
                        checkEmptyPath(objectRowIndex - 1, objectColIndex + 1)) {
                    entity.y += entity.getOriginalSpeed() * 0.4;
                    entity.x += entity.getOriginalSpeed() * 0.6;
                    entity.collisionOn = true;

                    return;
                }
            }

            // Comes left and goes into the bottom right -> left up
            if (entity.direction == Direction.LEFT && entityBottomLeft.intersects(objectUpperRight)) {
                if (isNotBorder(objectRowIndex, objectColIndex) &&
                        checkEmptyPath(objectRowIndex - 1, objectColIndex) &&
                        checkEmptyPath(objectRowIndex - 1, objectColIndex + 1)) {
                    entity.x -= entity.getOriginalSpeed() * 0.4;
                    entity.y -= entity.getOriginalSpeed() * 0.6;
                    entity.collisionOn = true;

                    return;
                }
            }
        }

        // Down left corner
        if (entityUpperLeft.intersects(objectBottomLeft) || entityUpperRight.intersects(objectBottomLeft) || entityBottomRight.intersects(objectBottomLeft)) {
            // Comes right and goes into the bottom right -> right down
            if (entity.direction == Direction.RIGHT && entityUpperRight.intersects(objectBottomLeft)) {
                if (isNotBorder(objectRowIndex, objectColIndex) &&
                        checkEmptyPath(objectRowIndex + 1, objectColIndex) &&
                        checkEmptyPath(objectRowIndex + 1, objectColIndex - 1)) {
                    entity.x += entity.getOriginalSpeed() * 0.4;
                    entity.y += entity.getOriginalSpeed() * 0.6;
                    entity.collisionOn = true;

                    return;
                }
            }

            // Comes up and goes into the upper right -> left up
            if (entity.direction == Direction.UP && entityUpperRight.intersects(objectBottomLeft)) {
                if (isNotBorder(objectRowIndex, objectColIndex) &&
                        checkEmptyPath(objectRowIndex, objectColIndex - 1) &&
                        checkEmptyPath(objectRowIndex + 1, objectColIndex - 1)) {
                    entity.x -= entity.getOriginalSpeed() * 0.6;
                    entity.y -= entity.getOriginalSpeed() * 0.4;
                    entity.collisionOn = true;

                    return;
                }
            }
        }

        // Down right corner
        if (entityUpperRight.intersects(objectBottomRight) || entityUpperLeft.intersects(objectBottomRight) || entityBottomLeft.intersects(objectBottomRight)) {
            // Comes left and goes into the bottom left -> left down
            if (entity.direction == Direction.LEFT && entityUpperLeft.intersects(objectBottomRight)) {
                if (isNotBorder(objectRowIndex, objectColIndex) &&
                        checkEmptyPath(objectRowIndex + 1, objectColIndex) &&
                        checkEmptyPath(objectRowIndex + 1, objectColIndex + 1)) {
                    entity.x -= entity.getOriginalSpeed() * 0.4;
                    entity.y += entity.getOriginalSpeed() * 0.6;
                    entity.collisionOn = true;

                    return;
                }
            }

            // Comes up and goes into the upper left -> right up
            if (entity.direction == Direction.UP && entityUpperLeft.intersects(objectBottomRight)) {
                if (isNotBorder(objectRowIndex, objectColIndex) &&
                        checkEmptyPath(objectRowIndex, objectColIndex + 1) &&
                        checkEmptyPath(objectRowIndex + 1, objectColIndex + 1)) {
                    entity.x += entity.getOriginalSpeed() * 0.6;
                    entity.y -= entity.getOriginalSpeed() * 0.4;
                    entity.collisionOn = true;

                    return;
                }
            }
        }

        entity.collisionOn = true;
    }
}
