package model;

import bombasztik.bomberman.entity.Direction;
import bombasztik.bomberman.entity.Monster;
import bombasztik.bomberman.entity.Player;
import bombasztik.bomberman.model.CollisionHandler;
import bombasztik.bomberman.model.GameModel;
import bombasztik.bomberman.model.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Nested
public class PlayerMonsterTest {
    private Player testPlayer;
    private Monster testMonster;

    @BeforeEach
    void setUp() throws IOException {
        GameModel testModel = new GameModel();
        testPlayer = new Player(testModel, 20, 20, 1, Direction.DOWN);
        testMonster = new Monster(testModel, 20, 40, 11);
        testMonster.direction = Direction.UP;

        testModel.entities.add(testPlayer);
        testModel.entities.add(testMonster);
        testModel.collH = new CollisionHandler(testModel);
        testModel.setMap(new Map(testModel));
    }

    @Test
    void playerDieWhenMonsterCollides() throws IOException {
        testMonster.y = 20;
        testPlayer.update();
        assertTrue(testPlayer.startDeathAnimation);
    }

    @Test
    void playerNotDieWhenInvincibleAndCollidesWithMonster() throws IOException {
        testPlayer.invincible = true;
        testMonster.y = 20;
        testPlayer.update();
        assertFalse(testPlayer.startDeathAnimation);
    }
}
