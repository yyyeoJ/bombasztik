package model;

import bombasztik.bomberman.entity.Direction;
import bombasztik.bomberman.entity.Player;
import bombasztik.bomberman.model.CollisionHandler;
import bombasztik.bomberman.model.GameModel;
import bombasztik.bomberman.model.Map;
import bombasztik.bomberman.object.Bomb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Nested
public class BombTest {
    private Bomb bomb;
    private Player testPlayer;

    @BeforeEach
    public void setUp() throws IOException {
        GameModel testModel = new GameModel();
        testPlayer = new Player(testModel, 20, 20, 1, Direction.DOWN);
        testModel.entities.add(testPlayer);
        testModel.collH = new CollisionHandler(testModel);
        testModel.setMap(new Map(testModel));
        testPlayer.hasDetonator = true;
        testPlayer.setPlaceBombPressed(true);
        testPlayer.placeBomb();
        bomb = testModel.bombs.getFirst();
    }

    @Test
    public void bombIsDetonated() {
        assertTrue(bomb.isDetonated);
    }

    @Test
    public void bombExplodesWhenOwnerIsDead() {
        testPlayer.alive = false;
        bomb.explode();
        assertEquals(0, testPlayer.bombsPlaced);
    }

    @Test
    public void playerDieWhenBombExplodes() throws IOException {
        testPlayer.y = 40;
        bomb.explode();
        testPlayer.update();
        assertTrue(testPlayer.startDeathAnimation);
    }

    @Test
    public void bombExplodesWhenSpriteNumIsThree() {
        bomb.spriteNum = 3;
        bomb.update();
        assertFalse(bomb.toShow);
    }

    @Test
    public void bombDoesNotExplodeWhenSpriteNumIsNotThree() {
        for (int i = 0; i < 2; i++) {
            bomb.spriteNum = i + 1;
            bomb.update();
            assertTrue(bomb.toShow);
        }
    }

    @Test
    public void bombTypeIsZeroWhenRangeIsLessThanOrEqualToTwo() {
        bomb.range = 2;
        assertEquals(0, bomb.getBombType());
    }

    @Test
    public void bombTypeIsOneWhenRangeIsThree() {
        bomb.range = 3;
        assertEquals(1, bomb.getBombType());
    }

    @Test
    public void bombTypeIsTwoWhenRangeIsGreaterThanThree() {
        bomb.range = 4;
        assertEquals(2, bomb.getBombType());
    }
}