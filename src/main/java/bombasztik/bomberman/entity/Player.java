package bombasztik.bomberman.entity;

import bombasztik.bomberman.model.GameModel;
import bombasztik.bomberman.view.GuiManager;
import bombasztik.bomberman.object.Bomb;
import bombasztik.bomberman.object.Box;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    // Movement variables
    private boolean upPressed, downPressed, leftPressed, rightPressed, placeBombPressed, placeBoxPressed;

    public int roundsWon = 0;
    public int dieSpriteNum = 0;

    // Powerup variables
    public int bombRange = 2;
    public int bombLimit = 1;
    public int bombsPlaced = 0;
    public int boxLimit = 0;

    public boolean blink = false;
    public int blinkCounter = 0;

    public boolean hasPlusSpeed = false;
    public boolean hasSlowness = false;
    public boolean hasDetonator = false;
    public boolean hasNoBombs = false;
    public boolean hasPlaceAllBombs = false;
    public boolean hasLessRange = false;

    // Speed variables
    private final double fastSpeed = getOriginalSpeed() * 1.2;
    private final double slowSpeed = getOriginalSpeed() * 0.5;

    public Player(GameModel model, double x, double y, int id, Direction d) throws IOException {
        super(model, x, y, id);
        setSolidArea(new Rectangle(2, 7, 16, 8));
        direction = d;
        actualSpeed = getOriginalSpeed();
    }

    /**
     * This method checks if the tile at the given row and column index is blocked.
     * A tile is considered blocked if it has a collision or a box in all four directions (up, down, left, right).
     * The method uses the game model to access the map layout and checks the tiles around the given tile.
     *
     * @param tileRowIndex the row index of the tile
     * @param tileColIndex the column index of the tile
     * @return boolean - true if the tile is blocked, false otherwise
     */
    private boolean isBlocked(int tileRowIndex, int tileColIndex) {
        return (getModel().getMap().layout[tileRowIndex + 1][tileColIndex].collision || getModel().getMap().layout[tileRowIndex + 1][tileColIndex].box != null)
                && (getModel().getMap().layout[tileRowIndex - 1][tileColIndex].collision || getModel().getMap().layout[tileRowIndex - 1][tileColIndex].box != null)
                && (getModel().getMap().layout[tileRowIndex][tileColIndex + 1].collision || getModel().getMap().layout[tileRowIndex][tileColIndex + 1].box != null)
                && (getModel().getMap().layout[tileRowIndex][tileColIndex - 1].collision || getModel().getMap().layout[tileRowIndex][tileColIndex - 1].box != null);
    }

    public void placeBomb() {
        if (placeBombPressed && !hasNoBombs) {
            if (bombLimit > bombsPlaced) {
                // Get the tile index where the bomb is placed
                int tileRowIndex = getRowIndex();
                int tileColIndex = getColIndex();

                // Get the coordinates to draw the bomb
                int drawX = tileColIndex * getModel().tileSize;
                int drawY = tileRowIndex * getModel().tileSize;

                if (!getModel().getMap().layout[tileRowIndex][tileColIndex].hasBomb && getModel().getMap().layout[tileRowIndex][tileColIndex].box == null && !getModel().getMap().layout[tileRowIndex][tileColIndex].collision) {
                    Bomb bomb = new Bomb(drawX, drawY, getModel(), this);

                    // If player has detonator power up, set bomb to detonate immediately to not run explode() method
                    if (hasDetonator) {
                        bomb.isDetonated = true;
                    }

                    getModel().bombs.add(bomb);
                    getModel().objects.add(bomb);
                    bombsPlaced += 1;

                    getModel().getMap().layout[tileRowIndex][tileColIndex].hasBomb = true;

                    // Check if player can move off the bomb
                    if (isBlocked(tileRowIndex, tileColIndex) && !ghost && hasDetonator) {
                        bomb.explode();
                    }
                }
            } else if (hasDetonator && bombsPlaced == bombLimit) {
                for (Bomb bomb : getModel().bombs) {
                    if (bomb.owner == this) {
                        bomb.explode();
                    }
                }
            }
        }

        placeBombPressed = false;
    }

    /**
     * This method is used to place a box in the game.
     * The method checks if the placeBoxPressed variable is true and if the boxLimit is greater than 0.
     * If these conditions are met, the method calculates the tile index
     * where the box is to be placed and the coordinates to draw the box.
     * The method then checks if the player can move off the box.
     * If the player cannot move off the box and is not a ghost, the startDeathAnimation variable is set to true.
     * If the tile where the box is to be placed does not already have a box or a bomb,
     * a new Box object is created and added to the game model's list of boxes and objects.
     * The boxLimit is then decreased by 1.
     * Finally, the placeBoxPressed variable is set to false.
     */
    public void placeBox() {
        if (placeBoxPressed && boxLimit > 0) {
            // Get the tile index where the box is placed
            int tileRowIndex = getRowIndex();
            int tileColIndex = getColIndex();

            // Get the coordinates to draw the box
            int drawX = tileColIndex * getModel().tileSize;
            int drawY = tileRowIndex * getModel().tileSize;

            // Check if player can move off the box
            if (isBlocked(tileRowIndex, tileColIndex) && !ghost) {
                startDeathAnimation = true;
            }

            if (getModel().getMap().layout[tileRowIndex][tileColIndex].box == null && !getModel().getMap().layout[tileRowIndex][tileColIndex].hasBomb) {
                Box box = new Box(drawX, drawY, getModel());
                box.collision = true;
                box.owner = this;

                getModel().getMap().layout[tileRowIndex][tileColIndex].box = box;
                getModel().boxes.add(box);
                getModel().objects.add(box);

                boxLimit -= 1;
            }
        }

        placeBoxPressed = false;
    }

    /**
     * This method is used to update the sprite of the player.
     * The spriteCounter is incremented each time this method is called.
     * When the spriteCounter exceeds 10, the spriteNum is toggled between 0 and 1, and the spriteCounter is reset to 0.
     * This results in changing the player's sprite every 10 frames, creating an animation effect.
     */
    private void updateSprite() {
        spriteCounter++;

        // Change player sprite every 10 frames
        if (spriteCounter > 10) {
            spriteNum = spriteNum == 0 ? 1 : 0;
            spriteCounter = 0;
        }
    }

    /**
     * This method is used to move the player.
     * The method checks if any of the movement keys are pressed.
     * If a movement key is pressed and there is no collision,
     * the player's position is updated based on the direction of movement and the player's speed.
     * The player's sprite also updated to create an animation effect.
     * If no movement key pressed, the player's sprite set to the standing sprite.
     */
    public void move() {
        if (upPressed || downPressed || leftPressed || rightPressed) {
            if (!collisionOn) {
                // Move player
                switch (direction) {
                    case UP -> y -= actualSpeed;
                    case DOWN -> y += actualSpeed;
                    case LEFT -> x -= actualSpeed;
                    case RIGHT -> x += actualSpeed;
                }
            }

            updateSprite();
        } else {
            // When player is not moving, display standing sprite
            spriteNum = 0;
        }
    }

    /**
     * This method is used to update the sprite of the player when the player is dying.
     * The dieSpriteCounter is incremented each time this method is called.
     * When the dieSpriteCounter exceeds 5, the dieSpriteNum is incremented by 1, and the dieSpriteCounter is reset to 0.
     * This results in changing the player's dying sprite every 5 frames, creating a dying animation effect.
     */
    private void updateDieSprite() {
        dieSpriteCounter++;

        // Change die sprite every 5 frames
        if (dieSpriteCounter > 5) {
            dieSpriteNum++;
            dieSpriteCounter = 0;
        }
    }

    /**
     * This method is used to update the state of the player.
     * If the player is alive, the method checks if the startDeathAnimation variable is true.
     * If startDeathAnimation is true and dieSpriteNum is less than 6, the updateDieSprite method is called.
     * If dieSpriteNum is not less than 6, the die method is called.
     * If startDeathAnimation is not true, the method checks for collisions, and calls the move, placeBomb and placeBox methods.
     * The method also checks if the blinkCounter is greater than 10.
     * If blinkCounter is greater than 10 and ghostAlert is true, the blink variable is toggled.
     * Finally, the blinkCounter is incremented.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void update() throws IOException {
        if (alive) {
            if (startDeathAnimation) {
                if (dieSpriteNum < 6) updateDieSprite();
                else die();
            } else {
                // Check collision
                collisionOn = false;
                getModel().collH.checkEntityCollision(this);
                getModel().collH.checkEntityObjectCollision(this);
                getModel().collH.checkItemCollision(this);

                move();
                placeBomb();
                placeBox();

                if (blinkCounter > 10) {
                    blinkCounter = 0;

                    if (ghostAlert) {
                        blink = !blink;
                    }
                }

                blinkCounter++;
            }
        }
    }

    /**
     * This method is used to create a transparent version of the provided image.
     * It creates a new BufferedImage with the same width and height as the provided image, but with an ARGB color model to support transparency.
     * A Graphics2D object is created from the new image, and the composite is set to an AlphaComposite with a transparency level of 0.4.
     * The provided image is then drawn onto the Graphics2D object, effectively applying the transparency to the image.
     * The Graphics2D object is disposed to free up system resources.
     * Finally, the new transparent image returned.
     *
     * @param image the original image to make transparent
     * @return a new transparent version of the provided image
     */
    private BufferedImage createTransparentImage(BufferedImage image) {
        BufferedImage transparentImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = transparentImage.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.4));
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return transparentImage;
    }

    /**
     * This method is used to get the active image of the player.
     * If the player is in the process of dying (startDeathAnimation is true),
     * the method retrieves the appropriate dying sprite from the asset loader.
     * If the player is not dying, the method determines the sprite index
     * based on whether the player is invincible and whether an invincibility alert is active.
     * The appropriate player sprite is then retrieved from the asset loader.
     * If the player is a ghost and not blinking, the player's sprite is made transparent by calling the createTransparentImage method.
     * The method returns the active image of the player.
     *
     * @param gm the GuiManager object used to access the asset loader
     * @return the active image of the player
     */
    private BufferedImage getActiveImage(GuiManager gm) {
        BufferedImage activeImage;

        if (startDeathAnimation) {
            activeImage = gm.assetLoader.playersDieImages[getId() - 1][dieSpriteNum];
        } else {
            int spriteIndex;
            if (!invincible) {
                spriteIndex = 0;
            } else if (invincibleAlert) {
                spriteIndex = 2;
            } else {
                spriteIndex = 1;
            }

            activeImage = gm.assetLoader.players[getId() - 1][spriteIndex][direction.ordinal()][spriteNum];
        }

        if (ghost && !blink) {
            activeImage = createTransparentImage(activeImage);
        }

        return activeImage;
    }

    /**
     * This method is used to draw the player on the game screen.
     * The method first checks if the player is alive.
     * If the player is alive, the method retrieves the active image of the player by calling the getActiveImage method.
     * The active image is then drawn on the game screen at the player's current position, scaled according to the GuiManager's scale factor.
     * The size of the drawn image is also scaled according to the GuiManager's scale factor.
     *
     * @param g2 the Graphics2D object used for drawing
     * @param gm the GuiManager object used to access the scale factor and the asset loader
     */
    @Override
    public void draw(Graphics2D g2, GuiManager gm) {
        if (alive) {
            activeImage = getActiveImage(gm);
            g2.drawImage(activeImage, (int) ((x + getModel().tileSize) * gm.scale), (int) ((y + getModel().tileSize) * gm.scale), gm.view.scaledTileSize, gm.view.scaledTileSize, null);
        }
    }

    /**
     * This method is used to get the fast speed of the player.
     * The fast speed is a variable that is set to 1.2 times the original speed of the player.
     * This method returns the current value of the fast speed.
     *
     * @return the fast speed of the player
     */
    public double getFastSpeed() {
        return this.fastSpeed;
    }

    /**
     * This method is used to get the slow speed of the player.
     * The slow speed is a variable that is set to 0.5 times the original speed of the player.
     * This method returns the current value of the slow speed.
     *
     * @return the slow speed of the player
     */
    public double getSlowSpeed() {
        return this.slowSpeed;
    }

    /**
     * This method is used to set the state of the upPressed variable.
     * The upPressed variable is used to track whether the up movement key is currently being pressed.
     * If the up movement key is pressed, the player will move upwards.
     *
     * @param b the new state of the upPressed variable
     */
    public void setUpPressed(boolean b) {
        this.upPressed = b;
    }

    /**
     * This method is used to set the state of the downPressed variable.
     * The downPressed variable is used to track whether the down movement key is currently being pressed.
     * If the down movement key is pressed, the player will move downwards.
     *
     * @param b the new state of the downPressed variable
     */
    public void setDownPressed(boolean b) {
        this.downPressed = b;
    }

    /**
     * This method is used to set the state of the leftPressed variable.
     * The leftPressed variable is used to track whether the left movement key is currently being pressed.
     * If the left movement key is pressed, the player will move to the left.
     *
     * @param b the new state of the leftPressed variable
     */
    public void setLeftPressed(boolean b) {
        this.leftPressed = b;
    }

    /**
     * This method is used to set the state of the rightPressed variable.
     * The rightPressed variable is used to track whether the right movement key is currently being pressed.
     * If the right movement key is pressed, the player will move to the right.
     *
     * @param b the new state of the rightPressed variable
     */
    public void setRightPressed(boolean b) {
        this.rightPressed = b;
    }

    /**
     * This method is used to set the state of the placeBombPressed variable.
     * The placeBombPressed variable is used to track whether the bomb placement key is currently being pressed.
     * If the bomb placement key is pressed, the player will place a bomb.
     *
     * @param b the new state of the placeBombPressed variable
     */
    public void setPlaceBombPressed(boolean b) {
        this.placeBombPressed = b;
    }

    /**
     * This method is used to set the state of the placeBoxPressed variable.
     * The placeBoxPressed variable is used to track whether the box placement key is currently being pressed.
     * If the box placement key is pressed, the player will place a box.
     *
     * @param b the new state of the placeBoxPressed variable
     */
    public void setPlaceBoxPressed(boolean b) {
        this.placeBoxPressed = b;
    }
}
