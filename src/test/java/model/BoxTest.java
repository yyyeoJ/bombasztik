package model;

import bombasztik.bomberman.entity.Direction;
import bombasztik.bomberman.entity.Player;
import bombasztik.bomberman.model.CollisionHandler;
import bombasztik.bomberman.model.GameModel;
import bombasztik.bomberman.model.Map;
import bombasztik.bomberman.object.Box;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class BoxTest {
    private Box testBox;
    private Player testPlayer;

    @BeforeEach
    public void setUp() throws IOException {
        GameModel testModel = new GameModel();
        testPlayer = new Player(testModel, 20, 20, 1, Direction.DOWN);
        testModel.entities.add(testPlayer);
        testModel.collH = new CollisionHandler(testModel);
        testModel.setMap(new Map(testModel));
        testPlayer.boxLimit = 1;
        testPlayer.setPlaceBoxPressed(true);
        testPlayer.placeBox();
        testBox = testModel.boxes.stream().filter(box -> box.owner == testPlayer).findFirst().orElse(null);
    }

    @Test
    public void boxDisappearsAfterAnimation() {
        testBox.startAnimation = true;

        for (int i = 0; i < 6 * 6; i++) {
            testBox.update();
        }

        assertFalse(testBox.toShow);
    }

    @Test
    public void boxDoesNotDisappearWithoutAnimation() {
        testBox.startAnimation = false;

        for (int i = 0; i < 6 * 6; i++) {
            testBox.update();
        }

        assertTrue(testBox.toShow);
    }

    @Test
    public void increasesOwnerBoxLimitAfterDisappearing() {
        testBox.startAnimation = true;

        for (int i = 0; i < 6 * 6; i++) {
            testBox.update();
        }

        assertEquals(1, testPlayer.boxLimit);
    }

    @Test
    public void notIncreaseOwnerBoxLimitWithoutDisappearing() {
        testBox.startAnimation = false;

        for (int i = 0; i < 6 * 6; i++) {
            testBox.update();
        }

        assertEquals(0, testPlayer.boxLimit);
    }

    @Test
    public void boxCollisionIsFalseWhenIntersectingWithEntity() {
        testBox.update();

        assertFalse(testBox.collision);
    }

    @Test
    public void boxCollisionIsTrueWhenNotIntersectingWithEntity() {
        testPlayer.x = 40;
        testPlayer.y = 0;

        testBox.update();

        assertTrue(testBox.collision);
    }
}