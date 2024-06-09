package bombasztik.bomberman.model;

import bombasztik.bomberman.entity.*;
import bombasztik.bomberman.item.Item;
import bombasztik.bomberman.object.Bomb;
import bombasztik.bomberman.object.Explosion;
import bombasztik.bomberman.object.SuperObject;

import java.awt.*;

public class CollisionHandler {
    private final GameModel model;

    public CollisionHandler(GameModel model) {
        this.model = model;
    }

    /**
     * Checks for collisions between the given entity and all objects in the game model.
     * This method iterates over all objects in the game model
     * and checks if the given entity's next solid area intersects with the object's solid area.
     * If a collision is detected, the method calls the object's collidesWithEntity method with the entity as an argument.
     *
     * @param entity the Entity object for which to check collisions
     */
    public void checkEntityObjectCollision(Entity entity) {
        for (SuperObject object : model.objects) {
            boolean collides = entity.nextSolidArea() != null && entity.nextSolidArea().intersects(object.getSolidArea());

            if (collides) {
                object.collidesWithEntity(entity);
            }
        }
    }

    /**
     * Checks for collisions between the given entity and all other entities in the game model.
     * This method iterates over all entities in the game model and
     * checks if the given entity's next solid area intersects with the other entity's solid area.
     * If a collision is detected, the method handles the collision based on the types of the entities involved.
     *
     * @param entity the Entity object for which to check collisions
     */
    public void checkEntityCollision(Entity entity) {
        Rectangle entityNextSolidArea = new Rectangle();
        Rectangle otherEntitySolidArea = new Rectangle();

        for (Entity otherEntity : model.entities) {
            // Check if examined player is not current player
            if (entity.getId() != otherEntity.getId()) {
                // examined player hitbox data
                otherEntitySolidArea = otherEntity.getSolidArea();

                entityNextSolidArea = entity.nextSolidArea();
            }

            if (entityNextSolidArea.intersects(otherEntitySolidArea)) {
                // Monster intersects
                if (entity.getId() > 10) {
                    if (otherEntity.getId() < 10) {
                        // Monster intersects player
                        if (!otherEntity.ghost) {
                            entity.collisionOn = true;
                        }
                        if (!otherEntity.invincible && !otherEntity.startDeathAnimation) {
                            if (model.soundManager != null) {
                                model.soundManager.deathSound.playSoundEffect();
                            }
                            otherEntity.startDeathAnimation = true;
                        }
                    } else {
                        // Monster intersects Monster
                        if (otherEntity.getId() > 10 && entity.getId() != otherEntity.getId()) {
                            entity.collisionOn = true;
                        }
                    }
                }
                // Player intersects
                else {
                    // Player intersects Player
                    if (otherEntity.getId() < 10) {
                        if (!entity.ghost && !otherEntity.ghost) {
                            entity.collisionOn = true;
                        }
                    }
                    // Player intersects Monster
                    else {
                        if (!entity.ghost) {
                            entity.collisionOn = true;
                        }
                        if (!entity.invincible && !entity.startDeathAnimation) {

                            if (model.soundManager != null) {
                                model.soundManager.deathSound.playSoundEffect();
                            }
                            entity.startDeathAnimation = true;
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks for collisions between the given player and all items in the game model.
     * This method iterates over all items in the game model and
     * checks if the player's solid area intersects with the item's solid area.
     * If a collision is detected and the item is not inside a box, the method does the following:
     * Calls the item's playerIntersects method with the player as an argument.
     * Sets the item's x and y coordinates to -1, effectively removing it from the game screen.
     * Plays the powerup sound effect.
     *
     * @param player the Player object for which to check item collisions
     */
    public void checkItemCollision(Player player) {
        Rectangle playerSolidArea = player.getSolidArea();

        for (Item item : model.items) {
            Rectangle itemSolidArea = item.getSolidArea();

            if (playerSolidArea.intersects(itemSolidArea) && (model.getMap().layout[itemSolidArea.y / model.tileSize][itemSolidArea.x / model.tileSize]).box == null) {
                item.playerIntersects(player);
                item.x = -1;
                item.y = -1;

                model.soundManager.powerupSound.playSoundEffect();
            }
        }
    }

    /**
     * Checks for chain reactions between bombs and explosions.
     * This method iterates over all bombs in the game model and
     * checks if the bomb's solid area intersects with the solid area of any explosion.
     * If a bomb-explosion intersection is detected and the explosion's sprite number is 0, the method does the following:
     * Sets the bomb's sprite number to 2 (bomb is about to explode).
     * Sets the bomb's sprite counter to 20 (starting the bomb's explosion animation).
     * Sets the bomb's isDetonated state to false (the bomb has not yet detonated).
     */
    public void checkBombChain() {
        for (Bomb b : model.bombs) {
            Rectangle bombSolidArea = b.getSolidArea();

            for (Explosion e : model.explosions) {
                Rectangle explosionSolidArea = e.getSolidArea();

                // Check intersection
                if (bombSolidArea.intersects(explosionSolidArea) && e.spriteNum == 0) {
                    b.spriteNum = 2;
                    b.spriteCounter = 20;
                    b.isDetonated = false;
                    break;
                }
            }
        }
    }
}