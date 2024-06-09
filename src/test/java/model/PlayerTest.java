package model;

import bombasztik.bomberman.entity.Direction;
import bombasztik.bomberman.entity.Player;
import bombasztik.bomberman.model.CollisionHandler;
import bombasztik.bomberman.model.GameModel;
import bombasztik.bomberman.model.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Nested
public class PlayerTest {
    private Player testPlayer;

    @BeforeEach
    void setUp() throws IOException {
        GameModel testModel = new GameModel();
        testPlayer = new Player(testModel, 20, 20, 1, Direction.DOWN);
        testModel.entities.add(testPlayer);
        testModel.collH = new CollisionHandler(testModel);
        testModel.setMap(new Map(testModel));
    }

    @Test
    void moveRightTest() {
        double initialX = testPlayer.x;
        double initialY = testPlayer.y;

        testPlayer.setRightPressed(true);
        testPlayer.direction = Direction.RIGHT;
        testPlayer.move();

        assertTrue(testPlayer.x != initialX && testPlayer.y == initialY);
    }

    @Test
    void moveLeftTest() {
        double initialX = testPlayer.x;
        double initialY = testPlayer.y;

        testPlayer.setLeftPressed(true);
        testPlayer.direction = Direction.LEFT;
        testPlayer.move();

        assertTrue(testPlayer.x != initialX && testPlayer.y == initialY);
    }

    @Test
    void moveUpTest() {
        double initialX = testPlayer.x;
        double initialY = testPlayer.y;

        testPlayer.setUpPressed(true);
        testPlayer.direction = Direction.UP;
        testPlayer.move();

        assertTrue(testPlayer.x == initialX && testPlayer.y != initialY);
    }

    @Test
    void moveDownTest() {
        double initialX = testPlayer.x;
        double initialY = testPlayer.y;

        testPlayer.setDownPressed(true);
        testPlayer.direction = Direction.DOWN;
        testPlayer.move();

        assertTrue(testPlayer.x == initialX && testPlayer.y != initialY);
    }

    @Test
    void updateTest() throws IOException {
        testPlayer.startDeathAnimation = true;
        testPlayer.dieSpriteNum = 6;
        testPlayer.update();
        assertFalse(testPlayer.alive);
    }

    @Test
    void placeBombWhenLimitNotReached() {
        testPlayer.setPlaceBombPressed(true);
        testPlayer.placeBomb();
        assertEquals(1, testPlayer.bombsPlaced);
    }

    @Test
    void placeBombWhenLimitReached() {
        testPlayer.bombsPlaced = testPlayer.bombLimit;
        testPlayer.setPlaceBombPressed(true);
        testPlayer.placeBomb();
        assertEquals(testPlayer.bombLimit, testPlayer.bombsPlaced);
    }

    @Test
    void placeBoxWhenLimitNotReached() {
        testPlayer.boxLimit = 1;
        testPlayer.setPlaceBoxPressed(true);
        testPlayer.placeBox();
        assertEquals(0, testPlayer.boxLimit);
    }

    @Test
    void placeBoxWhenLimitReached() {
        testPlayer.boxLimit = 0;
        testPlayer.setPlaceBoxPressed(true);
        testPlayer.placeBox();
        assertEquals(0, testPlayer.boxLimit);
    }

    @Test
    void dieTest() {
        testPlayer.die();
        assertEquals(-1, testPlayer.x);
        assertEquals(-1, testPlayer.y);
        assertFalse(testPlayer.alive);
    }
}
