package model;

import bombasztik.bomberman.entity.Direction;
import bombasztik.bomberman.entity.Player;
import bombasztik.bomberman.item.powerup.*;
import bombasztik.bomberman.item.sabotage.LessRange;
import bombasztik.bomberman.item.sabotage.NoBombs;
import bombasztik.bomberman.item.sabotage.PlaceAllBombs;
import bombasztik.bomberman.item.sabotage.Slowness;
import bombasztik.bomberman.model.GameModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Nested
class PowerUpTest {
    private GameModel testModel;
    private Player testPlayer;

    @BeforeEach
    void setUp() throws IOException {
        testModel = new GameModel();
        testPlayer = new Player(testModel, 0, 0, 1, Direction.UP);
        testModel.players.add(testPlayer);
        testModel.entities.add(testPlayer);
    }

    @Test
    void plusBombTest() {
        PlusBomb testItem = new PlusBomb(0, 0, testModel);
        testModel.items.add(testItem);

        testItem.playerIntersects(testPlayer);
        testItem.update();

        assertEquals(2, testPlayer.bombLimit);
    }

    @Test
    void plusRangeTest() {
        PlusRange testItem = new PlusRange(0, 0, testModel);
        testModel.items.add(testItem);

        testItem.playerIntersects(testPlayer);
        testItem.update();

        assertEquals(3, testPlayer.bombRange);
    }

    @Test
    void plusSpeedTest() {
        PlusSpeed testItem = new PlusSpeed(0, 0, testModel);
        testModel.items.add(testItem);

        testItem.playerIntersects(testPlayer);
        testItem.update();

        assertEquals(testPlayer.getFastSpeed(), testPlayer.actualSpeed);
    }

    @Test
    void placeBoxTest() throws IOException {
        PlaceBox testItem = new PlaceBox(0, 0, testModel);
        testModel.items.add(testItem);

        testItem.playerIntersects(testPlayer);
        testItem.update();

        assertEquals(3, testPlayer.boxLimit);

        PlaceBox testItem2 = new PlaceBox(0, 0, testModel);
        testModel.items.add(testItem2);

        testItem2.playerIntersects(testPlayer);
        testItem2.update();

        assertEquals(6, testPlayer.boxLimit);
    }

    @Test
    void invincibleTest() {
        Invincible testItem = new Invincible(0, 0, testModel);
        testModel.items.add(testItem);

        testItem.playerIntersects(testPlayer);
        testItem.update();

        assertTrue(testPlayer.invincible);
    }

    @Test
    void ghostTest() {
        Ghost testItem = new Ghost(0, 0, testModel);
        testModel.items.add(testItem);

        testItem.playerIntersects(testPlayer);
        testItem.update();

        assertTrue(testPlayer.ghost);
    }

    @Test
    void detonatorTest() throws IOException {
        Detonator testItem = new Detonator(0, 0, testModel);
        testModel.items.add(testItem);

        testItem.playerIntersects(testPlayer);
        testItem.update();

        assertTrue(testPlayer.hasDetonator);
    }

    @Test
    void lessRangeTest() {
        LessRange testItem = new LessRange(0, 0, testModel);
        testModel.items.add(testItem);

        testItem.playerIntersects(testPlayer);
        testItem.update();

        assertTrue(testPlayer.hasLessRange);
    }

    @Test
    void noBombsTest() {
        NoBombs testItem = new NoBombs(0, 0, testModel);
        testModel.items.add(testItem);

        testItem.playerIntersects(testPlayer);
        testItem.update();

        assertTrue(testPlayer.hasNoBombs);
    }

    @Test
    void placeAllBombsTest() {
        PlaceAllBombs testItem = new PlaceAllBombs(0, 0, testModel);
        testModel.items.add(testItem);

        testItem.playerIntersects(testPlayer);
        testItem.update();

        assertTrue(testPlayer.hasPlaceAllBombs);
    }

    @Test
    void slownessTest() {
        Slowness testItem = new Slowness(0, 0, testModel);
        testModel.items.add(testItem);

        testItem.playerIntersects(testPlayer);
        testItem.update();

        assertEquals(testPlayer.getSlowSpeed(), testPlayer.actualSpeed);
    }
}
