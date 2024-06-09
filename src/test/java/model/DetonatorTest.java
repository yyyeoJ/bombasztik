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
public class DetonatorTest {
    private Bomb detonator;
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
        detonator = testModel.bombs.getFirst();
    }

    @Test
    public void bombIsDetonated() {
        assertTrue(detonator.isDetonated);
    }

    @Test
    public void detonatorExplodesWhenOwnerIsDead() {
        testPlayer.alive = false;
        detonator.explode();
        assertEquals(0, testPlayer.bombsPlaced);
    }

    @Test
    public void detonatorGetNotLightingBomb() {
        detonator.spriteCounter = 0;
        assertEquals(3, detonator.getSpriteNum());
    }

    @Test
    public void detonatorGetLightingBomb() {
        detonator.spriteCounter = 15;
        assertEquals(4, detonator.getSpriteNum());
    }

    @Test
    public void detonatorExplodesWhenPlayerPressPlaceBombAgain() {
        testPlayer.setPlaceBombPressed(true);
        testPlayer.placeBomb();
        assertEquals(0, testPlayer.bombsPlaced);
    }
}