package bombasztik.bomberman.model;

import bombasztik.bomberman.entity.*;
import bombasztik.bomberman.item.Item;
import bombasztik.bomberman.object.*;
import bombasztik.bomberman.object.Box;
import bombasztik.bomberman.sound.SoundManager;

import java.io.IOException;
import java.util.*;

public class GameModel {
    private Map map;
    public CollisionHandler collH;

    public int FPS = 60;
    public int tileSize = 20;
    public int cols = 15;
    public int rows = 15;

    public int rounds = 3;
    public int currentRound = 1;
    public int selectedMap = 1;

    // -1 -> no winner
    public int winnerId = -1;
    public boolean player3mode;

    public SoundManager soundManager;
    public Player lastAlivePlayer = null;

    public ArrayList<Entity> entities = new ArrayList<>();
    public ArrayList<SuperObject> objects = new ArrayList<>();
    public ArrayList<Item> items = new ArrayList<>();
    public ArrayList<Player> players = new ArrayList<>();
    public ArrayList<Monster> monsters = new ArrayList<>();
    public ArrayList<Bomb> bombs = new ArrayList<>();
    public ArrayList<Explosion> explosions = new ArrayList<>();
    public ArrayList<Box> boxes = new ArrayList<>();
    public ArrayList<Wall> walls = new ArrayList<>();
    public ArrayList<Path> paths = new ArrayList<>();

    public GameModel() throws IOException {
    }

    /**
     * Initializes the game model.
     * It adds two players to the game at specific positions.
     * The first player is facing down and the second player is facing up.
     * If the 3 player mode is active, it adds a third player to the game at a specific position, facing left.
     * Adds four monsters to the game at specific positions. If the 3 player mode is false, it adds a fourth monster.
     * Adds all players and monsters to the entities list.
     * Creates a new Map object and assigns it to the map field.
     * Creates a new CollisionHandler object and assigns it to the collH field.
     *
     * @throws IOException if an I/O error occurs
     */
    public void init() throws IOException {
        players.add(new Player(this, tileSize, tileSize, 1, Direction.DOWN)); // player1
        players.add(new Player(this, 13 * tileSize, 13 * tileSize, 2, Direction.UP)); // player2

        if (player3mode) {
            players.add(new Player(this, 13 * tileSize, tileSize, 3, Direction.LEFT));
        }

        monsters.add(new Monster(this, 7 * tileSize, (4 * tileSize), 11));
        monsters.add(new Monster(this, 7 * tileSize, (10 * tileSize), 12));
        monsters.add(new Monster(this, tileSize, (13 * tileSize), 13));

        if (!player3mode) {
            monsters.add(new Monster(this, (13 * tileSize), tileSize, 14));
        }

        entities.addAll(players);
        entities.addAll(monsters);

        map = new Map(this);
        collH = new CollisionHandler(this);
    }

    /**
     * Updates the game state for each game update cycle.
     * It iterates over all entities in the game model.
     * If an entity is alive, it calls the entity's update method.
     * Sorts the entities list.
     * Iterates over all boxes in the game model.
     * If a box is to be shown, it calls the box's update method.
     * If a box is not to be shown, it removes the box from the boxes list and the objects list.
     * Iterates over all bombs in the game model.
     * If a bomb is to be shown, it calls the bomb's update method.
     * If a bomb is not to be shown, it removes the bomb from the bombs list and the objects list.
     * Iterates over all explosions in the game model.
     * If an explosion is to be shown, it calls the explosion's update method.
     * If an explosion is not to be shown, it removes the explosion from the explosions list and the objects list.
     * Iterates over all items in the game model.
     * If an item is to be updated, it calls the item's update method.
     * If an item is not to be updated, it removes the item from the items list.
     * Calls the CollisionHandler's checkBombChain method to check for bomb chain reactions.
     * Calls the checkWinner method to check if a player has won the game.
     *
     * @throws IOException if an I/O error occurs
     */
    public void update() throws IOException {
        for (Entity entity : entities) {
            if (entity.alive) {
                entity.update();
            }
        }
        Collections.sort(entities);

        Iterator<Box> boxIterator = boxes.iterator();
        while (boxIterator.hasNext()) {
            Box box = boxIterator.next();
            if (box.toShow) {
                box.update();
            } else {
                boxIterator.remove();
                objects.remove(box);
            }
        }

        Iterator<Bomb> bombIterator = bombs.iterator();
        while (bombIterator.hasNext()) {
            Bomb bomb = bombIterator.next();
            if (bomb.toShow) {
                bomb.update();
            } else {
                bombIterator.remove();
                objects.remove(bomb);
            }
        }

        Iterator<Explosion> explosionIterator = explosions.iterator();
        while (explosionIterator.hasNext()) {
            Explosion explosion = explosionIterator.next();
            if (explosion.toShow) {
                explosion.update();
            } else {
                explosionIterator.remove();
                objects.remove(explosion);
            }
        }

        Iterator<Item> itemIterator = items.iterator();
        while (itemIterator.hasNext()) {
            Item item = itemIterator.next();
            if (item.toUpdate()) {
                item.update();
            } else {
                itemIterator.remove();
            }
        }

        collH.checkBombChain();
        checkWinner();
    }

    /**
     * Checks if a player has won the game.
     * The method counts the number of players that are still alive.
     * If there are no bombs left or the last alive player has a detonator,
     * and there are no explosions, it checks the number of players still alive.
     * If there is only one player alive and that player is not in the process of dying,
     * it sets the winnerId to the ID of the last alive player and increments the number of rounds that player has won.
     * If there are no players alive, it sets the winnerId to 0, indicating a draw.
     */
    public void checkWinner() {
        int aliveCount = 0;

        for (Player player : players) {
            if (player.alive) {
                aliveCount++;
                lastAlivePlayer = player;
            }
        }

        if ((bombs.isEmpty() || lastAlivePlayer.hasDetonator) && explosions.isEmpty()) {
            if (aliveCount == 1 && lastAlivePlayer.dieSpriteNum == 0) {
                // If end of round then winner is the last alive player
                winnerId = lastAlivePlayer.getId();
                lastAlivePlayer.roundsWon += 1;
            } else if (aliveCount == 0) {
                // If draw then 0
                winnerId = 0;
            }
        }
    }

    /**
     * Returns the current game map.
     * This method returns the Map object that represents the current game map.
     * The game map contains information about the layout of the game world, including the positions of walls, boxes, and paths.
     *
     * @return the current game map
     */
    public Map getMap() {
        return this.map;
    }

    /**
     * Sets the current game map.
     * This method sets the Map object that represents the current game map.
     * The game map contains information about the layout of the game world, including the positions of walls, boxes, and paths.
     *
     * @param m the new game map
     */
    public void setMap(Map m) {
        this.map = m;
    }
}
