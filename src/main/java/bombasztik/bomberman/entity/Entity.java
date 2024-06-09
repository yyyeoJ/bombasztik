package bombasztik.bomberman.entity;

import bombasztik.bomberman.view.GuiManager;
import bombasztik.bomberman.model.GameModel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class Entity implements Comparable<Entity> {
    private final GameModel model;

    // Entity variables
    private final int id;
    public double x, y;
    public Direction direction;
    public boolean alive = true;
    public boolean startDeathAnimation = false;
    public boolean collisionOn = false;
    public BufferedImage activeImage;

    // Sprite variables
    public int spriteCounter = 0;
    public int spriteNum = 0;
    public int dieSpriteCounter = 0;

    // Hitbox variable
    private Rectangle solidArea;

    // Powerup variables
    public boolean invincibleAlert = false;
    public boolean ghostAlert = false;

    public double actualSpeed;
    public boolean invincible = false;
    public boolean ghost = false;

    public Entity(GameModel model, double x, double y, int id) {
        this.model = model;
        this.id = id;
        this.x = x;
        this.y = y;
    }

    /**
     * This is an abstract method that updates the state of an entity.
     *
     * @throws IOException if there is an issue with reading files
     */
    public abstract void update() throws IOException;

    /**
     * This is an abstract method that draws the entity on the screen.
     *
     * @param g2 Graphics2D object used to draw the entity
     * @param gm GuiManager object used to draw the entity
     */
    public abstract void draw(Graphics2D g2, GuiManager gm);

    /**
     * This method is used to simulate the death of the entity.
     * It sets the entity's x and y coordinates to -1, effectively moving it off the game map.
     * It also sets the entity's alive status to false.
     */
    public void die() {
        x = -1;
        y = -1;
        alive = false;
    }

    /**
     * This method calculates and returns the column index of the entity on the game map.
     * The calculation is based on the entity's x-coordinate, the x-coordinate of the entity's solid area (hitbox),
     * and half the width of the entity's solid area.
     * The result is then divided by the size of a tile in the game model.
     *
     * @return int - the column index of the entity on the game map
     */
    public int getColIndex() {
        return (int) ((x + solidArea.x + (solidArea.width / 2)) / model.tileSize);
    }

    /**
     * This method calculates and returns the row index of the entity on the game map.
     * The calculation is based on the entity's y-coordinate, the y-coordinate of the entity's solid area (hitbox),
     * and half the height of the entity's solid area.
     * The result is then divided by the size of a tile in the game model.
     *
     * @return int - the row index of the entity on the game map
     */
    public int getRowIndex() {
        return (int) ((y + solidArea.y + (solidArea.height / 2)) / model.tileSize);
    }

    /**
     * This method is used to compare two entities based on their y-coordinates.
     *
     * @param otherEntity the other entity to compare this entity with
     * @return int
     */
    @Override
    public int compareTo(Entity otherEntity) {
        return Double.compare(this.y, otherEntity.y);
    }

    /**
     * This method calculates and returns the solid area of the entity as a Rectangle object.
     * The calculation is based on the entity's x and y coordinates and the x and y coordinates of the entity's solid area (hitbox).
     * The width and height of the solid area are also taken from the entity's solid area.
     * The calculated values are used to create a new Rectangle object which represents the solid area of the entity.
     *
     * @return Rectangle - the solid area of the entity
     */
    public Rectangle getSolidArea() {
        double playerX = x + solidArea.x;
        double playerY = y + solidArea.y;
        double playerWidth = solidArea.width;
        double playerHeight = solidArea.height;
        return new Rectangle((int) playerX, (int) playerY, (int) playerWidth, (int) playerHeight);
    }

    /**
     * This method is used to set the solid area of the entity.
     * The solid area is represented as a Rectangle object.
     * The method takes a Rectangle object as a parameter and assigns it to the entity's solid area.
     *
     * @param r Rectangle object representing the new solid area of the entity
     */
    public void setSolidArea(Rectangle r) {
        this.solidArea = r;
    }

    /**
     * This method calculates and returns the next solid area of the entity as a Rectangle object.
     * The calculation is based on the entity's current direction and speed.
     * The method uses a switch statement to determine the direction of the entity
     * and calculates the new x and y coordinates of the solid area accordingly.
     * The new x and y coordinates are then used to create a new Rectangle object
     * which represents the next solid area of the entity.
     *
     * @return Rectangle - the next solid area of the entity
     */
    public Rectangle nextSolidArea() {
        double solidAreaX = 0;
        double solidAreaY = 0;

        switch (direction) {
            case UP -> {
                solidAreaX = x + solidArea.x;
                solidAreaY = y + solidArea.y - actualSpeed;
            }
            case DOWN -> {
                solidAreaX = x + solidArea.x;
                solidAreaY = y + solidArea.y + actualSpeed;
            }
            case LEFT -> {
                solidAreaX = x + solidArea.x - actualSpeed;
                solidAreaY = y + solidArea.y;
            }
            case RIGHT -> {
                solidAreaX = x + solidArea.x + actualSpeed;
                solidAreaY = y + solidArea.y;
            }
        }

        return new Rectangle((int) solidAreaX, (int) solidAreaY, solidArea.width, solidArea.height);
    }

    /**
     * This method is used to get the ID of the entity.
     * The ID is a unique identifier for each entity in the game.
     *
     * @return int - the ID of the entity
     */
    public int getId() {
        return this.id;
    }

    /**
     * This method is used to get the game model associated with the entity.
     *
     * @return GameModel - the game model associated with the entity
     */
    public GameModel getModel() {
        return this.model;
    }

    /**
     * This method is used to get the original speed of the entity.
     * The original speed is a constant value that represents the base speed of the entity.
     *
     * @return double - the original speed of the entity
     */
    public double getOriginalSpeed() {
        return 1.33;
    }
}
