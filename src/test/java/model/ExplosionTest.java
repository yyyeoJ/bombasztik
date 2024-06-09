package model;

import bombasztik.bomberman.entity.Direction;
import bombasztik.bomberman.entity.Player;
import bombasztik.bomberman.object.Explosion;
import bombasztik.bomberman.object.ExplosionType;
import bombasztik.bomberman.model.GameModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ExplosionTest {
    private Explosion explosion;
    private Player testPlayer;

    @BeforeEach
    public void setUp() throws IOException {
        GameModel testModel = new GameModel();
        explosion = new Explosion(testModel, 20, 20, ExplosionType.CENTER);
        testPlayer = new Player(testModel, 20, 20, 1, Direction.DOWN);
    }

    @Test
    public void explosionAnimationShouldStartBackwardsWhenSpriteNumIsThree() {
        explosion.spriteNum = 3;
        explosion.update();
        assertTrue(explosion.animateBackwards);
    }

    @Test
    public void explosionShouldNotShowWhenSpriteNumIsNegativeOne() {
        explosion.spriteNum = -1;
        explosion.update();
        assertFalse(explosion.toShow);
    }

    @Test
    public void explosionShouldStartDeathAnimationWhenCollidesWithPlayer() {
        explosion.spriteNum = 0;
        explosion.collidesWithEntity(testPlayer);
        assertTrue(testPlayer.startDeathAnimation);
    }

    @Test
    public void explosionShouldNotStartDeathAnimationWhenPlayerIsInvincible() {
        testPlayer.invincible = true;
        explosion.spriteNum = 0;
        explosion.collidesWithEntity(testPlayer);
        assertFalse(testPlayer.startDeathAnimation);
    }
}