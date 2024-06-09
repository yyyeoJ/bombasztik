package bombasztik.bomberman.model;

import bombasztik.bomberman.entity.Entity;
import bombasztik.bomberman.item.powerup.*;
import bombasztik.bomberman.item.sabotage.*;
import bombasztik.bomberman.object.Box;
import bombasztik.bomberman.object.Path;
import bombasztik.bomberman.object.SuperObject;
import bombasztik.bomberman.object.Wall;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class Map {
    GameModel model;
    public SuperObject[][] layout;
    private final Random random = new Random();

    public Map(GameModel model) throws IOException {
        this.model = model;

        layout = new SuperObject[model.cols][model.rows];
        loadMap("/maps/map" + model.selectedMap + ".txt");
    }

    /**
     * Retrieves the tiles obstructed by entities.
     * The method iterates over all entities in the game model.
     * For each entity, it calculates the tile row and column indices.
     * It adds the tile occupied by the entity and its surrounding tiles to the entityTiles list.
     *
     * @return a list of Points representing the tiles obstructed by entities
     */
    private ArrayList<Point> getEntityTiles() {
        // Tiles obstructed by entities
        ArrayList<Point> entityTiles = new ArrayList<>();

        for (Entity entity : model.entities) {
            int tileRowIndex = entity.getRowIndex();
            int tileColIndex = entity.getColIndex();

            entityTiles.add(new Point(tileRowIndex, tileColIndex));
            entityTiles.add(new Point(tileRowIndex - 1, tileColIndex));
            entityTiles.add(new Point(tileRowIndex + 1, tileColIndex));

            entityTiles.add(new Point(tileRowIndex - 1, tileColIndex - 1));
            entityTiles.add(new Point(tileRowIndex + 1, tileColIndex - 1));
            entityTiles.add(new Point(tileRowIndex, tileColIndex - 1));

            entityTiles.add(new Point(tileRowIndex - 1, tileColIndex + 1));
            entityTiles.add(new Point(tileRowIndex + 1, tileColIndex + 1));
            entityTiles.add(new Point(tileRowIndex, tileColIndex + 1));
        }

        return entityTiles;
    }

    /**
     * Creates a wall at the specified location.
     * This method creates a new Wall object at the specified drawX and drawY coordinates,
     * and adds it to the game model's layout and lists of objects and walls.
     * The wall is placed at the specified rowIndex and colIndex in the layout array.
     *
     * @param rowIndex the row index in the layout array where the wall is to be placed
     * @param colIndex the column index in the layout array where the wall is to be placed
     * @param drawX the x-coordinate at which the wall is to be drawn
     * @param drawY the y-coordinate at which the wall is to be drawn
     */
    private void createWall(int rowIndex, int colIndex, int drawX, int drawY) {
        Wall wall = new Wall(drawX, drawY, model);
        layout[rowIndex][colIndex] = wall;
        model.objects.add(wall);
        model.walls.add(wall);
    }

    /**
     * Creates a power-up item at the specified location.
     * This method generates a random integer between 0 and 6, and based on this integer,
     * it creates a specific type of power-up item.
     * The power-up item is then added to the game model's list of items.
     *
     * @param drawX the x-coordinate at which the power-up item is to be drawn
     * @param drawY the y-coordinate at which the power-up item is to be drawn
     */
    private void createPowerup(int drawX, int drawY) {
        switch (random.nextInt(7)) {
            case 0 -> model.items.add(new Detonator(drawX, drawY, model));
            case 1 -> model.items.add(new Ghost(drawX, drawY, model));
            case 2 -> model.items.add(new Invincible(drawX, drawY, model));
            case 3 -> model.items.add(new PlaceBox(drawX, drawY, model));
            case 4 -> model.items.add(new PlusBomb(drawX, drawY, model));
            case 5 -> model.items.add(new PlusRange(drawX, drawY, model));
            case 6 -> model.items.add(new PlusSpeed(drawX, drawY, model));
        }
    }

    /**
     * Creates a sabotage item at the specified location.
     * This method generates a random integer between 0 and 3, and based on this integer,
     * it creates a specific type of sabotage item.
     * The sabotage item is then added to the game model's list of items.
     * The types of sabotage items that can be created are as follows:
     *
     * @param drawX the x-coordinate at which the sabotage item is to be drawn
     * @param drawY the y-coordinate at which the sabotage item is to be drawn
     */
    private void createSabotage(int drawX, int drawY) {
        switch (random.nextInt(4)) {
            case 0 -> model.items.add(new LessRange(drawX, drawY, model));
            case 1 -> model.items.add(new NoBombs(drawX, drawY, model));
            case 2 -> model.items.add(new PlaceAllBombs(drawX, drawY, model));
            case 3 -> model.items.add(new Slowness(drawX, drawY, model));
        }
    }

    /**
     * Creates a path or a box at the specified location.
     * This method does the following:
     * Creates a new Path object at the specified drawX and drawY coordinates,
     * and adds it to the game model's layout and list of paths.
     * With a 70% chance, it generates a Box object at the same location,
     * provided that the location is not obstructed by an entity.
     * The Box object is added to the game model's layout, and lists of boxes and objects.
     * With a 30% chance, it generates an item under the box.
     * The item can be a power-up item (with an 80% chance) or a sabotage item (with a 20% chance).
     *
     * @param rowIndex the row index in the layout array where the path and possibly the box are to be placed
     * @param colIndex the column index in the layout array where the path and possibly the box are to be placed
     * @param drawX the x-coordinate at which the path and possibly the box are to be drawn
     * @param drawY the y-coordinate at which the path and possibly the box are to be drawn
     * @param entityTiles a list of Points representing the tiles obstructed by entities
     */
    private void createPathAndBox(int rowIndex, int colIndex, int drawX, int drawY, ArrayList<Point> entityTiles) {
        Path path = new Path(drawX, drawY, model);
        layout[rowIndex][colIndex] = path;
        model.paths.add(path);

        // Generate boxes
        if (random.nextDouble() < 0.7) {
            if (!entityTiles.contains(new Point(rowIndex, colIndex))) {
                Box box = new Box(drawX, drawY, model);

                // Set collision to be true by default for the boxes placed by the game
                box.collision = true;
                layout[rowIndex][colIndex].box = box;
                model.boxes.add(box);
                model.objects.add(box);

                // chance of item or nothing under the box
                if (random.nextDouble() < 0.3) {
                    // chance of powerup or sabotage
                    if (random.nextDouble() < 0.8) {
                        // powerups
                        createPowerup(drawX, drawY);
                    } else {
                        // sabotages
                        createSabotage(drawX, drawY);
                    }
                }
            }
        }
    }

    /**
     * Loads the game map from a text file.
     * This method does the following:
     * Reads the map file line by line. Each line represents a row in the game map,
     * and each number in the line represents a tile in the row.
     * For each tile, it determines the type of the tile (path/box or wall) based on the number and creates the corresponding object.
     * The created object is placed at the correct position in the game model's layout array
     * and added to the appropriate list in the game model.
     * The position of the object is calculated based on the tile size and the row and column indices.
     * The method also keeps track of the tiles obstructed by entities to ensure that boxes are not placed on these tiles.
     * After processing a line, it resets the column index and increments the row index to move to the next row.
     * The method continues this process until it has processed all rows and columns in the game map.
     *
     * @param filePath the path of the map file to be loaded
     * @throws IOException if an I/O error occurs when reading the map file
     */
    public void loadMap(String filePath) throws IOException {
        InputStream iS = getClass().getResourceAsStream(filePath);
        assert iS != null;
        BufferedReader bR = new BufferedReader(new InputStreamReader(iS));

        ArrayList<Point> entityTiles = getEntityTiles();

        int rowIndex = 0;
        int colIndex = 0;

        while (rowIndex < model.rows && colIndex < model.cols) {
            String line = bR.readLine();
            String[] numbers = line.split(" ");

            while (colIndex < model.cols) {
                int num = Integer.parseInt(numbers[colIndex]);

                // Draw the tile at the correct position
                int drawX = colIndex * model.tileSize;
                int drawY = rowIndex * model.tileSize;

                switch (num) {
                    case 0 -> createPathAndBox(rowIndex, colIndex, drawX, drawY, entityTiles);
                    case 1 -> createWall(rowIndex, colIndex, drawX, drawY);
                }

                // Increment column index
                colIndex++;
            }

            // Reset column index and increment row index
            if (colIndex == model.cols) {
                colIndex = 0;
                rowIndex++;
            }
        }

        bR.close();
    }
}