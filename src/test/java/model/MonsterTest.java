package model;

import bombasztik.bomberman.entity.Direction;
import bombasztik.bomberman.entity.Monster;
import bombasztik.bomberman.model.CollisionHandler;
import bombasztik.bomberman.model.GameModel;
import bombasztik.bomberman.model.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Nested
class MonsterTest {
    private Monster testMonster;

    @BeforeEach
    void setUp() throws IOException {
        GameModel testModel = new GameModel();
        testModel.collH = new CollisionHandler(testModel);
        testModel.setMap(new Map(testModel));
        testMonster = new Monster(testModel, 20, 20, 1);
        testModel.entities.add(testMonster);
    }

    @Test
    void updateTest() {
        testMonster.dieSpriteCounter = 30;
        testMonster.startDeathAnimation = true;
        testMonster.update();
        assertFalse(testMonster.alive);
    }

    @Test
    void moveTest() {
        double initialX = testMonster.x;
        double initialY = testMonster.y;
        testMonster.move();

        assertEquals(testMonster.x == initialX && testMonster.y == initialY, testMonster.collisionOn);
    }

    @Test
    void dieTest() {
        testMonster.die();
        assertEquals(-1, testMonster.x);
        assertEquals(-1, testMonster.y);
        assertFalse(testMonster.alive);
    }

    @Test
    public void monsterShouldDieWhenDieSpriteCounterIsGreaterThan30() {
        testMonster.startDeathAnimation = true;
        testMonster.dieSpriteCounter = 30;
        testMonster.update();
        assertFalse(testMonster.alive);
    }
}