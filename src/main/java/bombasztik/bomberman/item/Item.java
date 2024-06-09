package bombasztik.bomberman.item;

import bombasztik.bomberman.entity.Player;
import bombasztik.bomberman.view.GuiManager;
import bombasztik.bomberman.model.GameModel;

import java.awt.*;
import java.io.IOException;

public abstract class Item {
    protected GameModel model;
    public Player player;
    private final Rectangle solidArea;

    public double x, y;
    public int timer = 0;

    protected boolean toShow = true;
    public boolean makeEffect = false;
    protected int effectTime;

    public Item(double x, double y, GameModel model) {
        this.x = x;
        this.y = y;
        this.model = model;

        effectTime = model.FPS * 8;
        solidArea = new Rectangle(3, 3, 14, 14);
    }

    /**
     * Draws the item on the game screen.
     * This is an abstract method that must be implemented by subclasses of Item.
     *
     * @param g2 the Graphics2D object used for drawing
     * @param gm the GuiManager object used for managing the GUI elements
     */
    public abstract void draw(Graphics2D g2, GuiManager gm);

    /**
     * Handles the interaction between the player and the item.
     * This is an abstract method that must be implemented by subclasses of Item.
     *
     * @param player the Player object that might intersect with the item
     */
    public abstract void playerIntersects(Player player);

    /**
     * Updates the state of the item.
     * This is an abstract method that must be implemented by subclasses of Item.
     * The method is responsible for updating the state of the item in the game.
     *
     * @throws IOException if an input or output exception occurred
     */
    public abstract void update() throws IOException;

    /**
     * Gets the solid area of the item.
     * This method calculates the solid area of the item based on its current position (x, y) and the dimensions of the solidArea.
     * The solid area is represented as a Rectangle object.
     * The x and y coordinates of the Rectangle are calculated by adding the x and y coordinates of the item to the x and y coordinates of the solidArea.
     * The width and height of the Rectangle are the same as the width and height of the solidArea.
     *
     * @return a Rectangle object representing the solid area of the item
     */
    public Rectangle getSolidArea() {
        double itemX = x + solidArea.x;
        double itemY = y + solidArea.y;
        double itemWidth = solidArea.width;
        double itemHeight = solidArea.height;
        return new Rectangle((int) itemX, (int) itemY, (int) itemWidth, (int) itemHeight);
    }

    /**
     * Checks if the item needs to be updated.
     * This method returns a boolean value indicating whether the item needs to be updated.
     *
     * @return a boolean value indicating whether the item needs to be updated
     */
    public boolean toUpdate() {
        return (this.makeEffect || this.toShow);
    }

    /**
     * Sets the visibility of the item.
     * This method takes a boolean value as a parameter and sets the visibility of the item accordingly.
     * If the parameter is true, the item will be shown.
     * If the parameter is false, the item will not be shown.
     *
     * @param b the boolean value indicating whether the item should be shown
     */
    public void setToShow(boolean b) {
        this.toShow = b;
    }
}
