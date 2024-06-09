package bombasztik.bomberman.controller;

import bombasztik.bomberman.entity.Direction;
import bombasztik.bomberman.entity.Monster;
import bombasztik.bomberman.entity.Player;
import bombasztik.bomberman.sound.SoundManager;
import bombasztik.bomberman.view.GamePanel;
import bombasztik.bomberman.model.GameModel;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class GameController implements Runnable {
    private final GamePanel view;
    private final GameModel model;
    private final KeyHandler keyH;
    private Thread gameThread;

    private boolean running;

    public GameController(GamePanel view, GameModel model) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        this.view = view;
        this.model = model;
        this.model.soundManager = new SoundManager();
        keyH = new KeyHandler(this);
    }

    /**
     * This method is used to start the game thread.
     * It initializes a new Thread with this GameController as the Runnable object.
     * The thread is then started and the running flag is set to true, indicating that the game is now running.
     */
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
        running = true;
    }

    /**
     * This method is the main game loop, which is run in a separate thread.
     * It calculates the time delta between each frame and updates the game state accordingly.
     * The game state is updated and the view is repainted at a rate determined by the model's FPS.
     * The game loop continues running until the gameThread is null or the running flag is set to false.
     * The game state is updated based on the winnerId:
     *
     * @throws RuntimeException if there is an IOException during the game update.
     */
    @Override
    public void run() {
        double drawInterval = (double) 1000000000 / model.FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null && running) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                try {
                    switch (model.winnerId) {
                        case -1:
                            model.update();
                            view.repaint();

                            break;
                        case 0:
                            running = false;
                            view.gm.roundOverPanel.winner = null;
                            view.gm.roundOverPanel.repaint();
                            view.gm.cardLayout.show(view.gm.cardPanel, "roundOver");
                            view.model.soundManager.gameSound.stopMusic();

                            if (!view.model.soundManager.roundOverSound.clip.isRunning()) {
                                view.model.soundManager.roundOverSound.playMusic();
                            }

                            break;
                        default:
                            view.gm.roundOverPanel.repaint();
                            running = false;
                            view.gm.roundOverPanel.winner = model.lastAlivePlayer;
                            view.gm.cardLayout.show(view.gm.cardPanel, "roundOver");
                            view.model.soundManager.gameSound.stopMusic();

                            if (model.lastAlivePlayer.roundsWon == model.rounds && !view.model.soundManager.gameOverSound.clip.isRunning()) {
                                view.model.soundManager.gameOverSound.playMusic();
                            } else {
                                if (!view.model.soundManager.roundOverSound.clip.isRunning()) {
                                    view.model.soundManager.roundOverSound.playMusic();
                                }
                            }

                            break;
                    }

                    delta--;
                    // updatesPerSecond++;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * This method is used to clear all the arrays in the model.
     * It clears the entities, objects, items, monsters, bombs, explosions, boxes, walls, and paths arrays.
     */
    private void clearModelArrays() {
        model.entities.clear();
        model.objects.clear();
        model.items.clear();
        model.monsters.clear();
        model.bombs.clear();
        model.explosions.clear();
        model.boxes.clear();
        model.walls.clear();
        model.paths.clear();
    }

    /**
     * This method is used to reset the players in the game.
     * It creates new Player objects for each player in the game, preserving their roundsWon from the previous state.
     * The players are then added to the model's players list and entities list.
     * If the game is in 3 player mode, a third player is also created and added to the lists.
     * The KeyHandler is updated with the new Player objects.
     *
     * @throws IOException if there is an error creating the Player objects
     */
    private void resetPlayers() throws IOException {
        Player tempPlayer1 = new Player(model, model.tileSize, model.tileSize, 1, Direction.DOWN);
        tempPlayer1.roundsWon = model.players.get(0).roundsWon;

        Player tempPlayer2 = new Player(model, 13 * model.tileSize, 13 * model.tileSize, 2, Direction.UP);
        tempPlayer2.roundsWon = model.players.get(1).roundsWon;

        Player tempPlayer3 = new Player(model, 13 * model.tileSize, model.tileSize, 3, Direction.LEFT);
        if (model.player3mode) {
            tempPlayer3.roundsWon = model.players.get(2).roundsWon;
        }

        model.players.clear();
        model.players.add(tempPlayer1);
        model.players.add(tempPlayer2);

        if (model.player3mode) {
            model.players.add(tempPlayer3);
            keyH.setPlayer3(tempPlayer3);
        }
        keyH.setPlayer1(tempPlayer1);
        keyH.setPlayer2(tempPlayer2);
        model.entities.addAll(model.players);
    }

    /**
     * This method is used to reset the monsters in the game.
     * It creates new Monster objects and adds them to the model's monsters list.
     * The monsters are then added to the model's entities list.
     * The monsters are created at specific locations on the map.
     * If the game is not in 3 player mode, an additional monster is created and added to the lists.
     */
    private void resetMonsters() {
        model.monsters.add(new Monster(model, 7 * model.tileSize, (4 * model.tileSize), 11));
        model.monsters.add(new Monster(model, 7 * model.tileSize, (10 * model.tileSize), 12));
        model.monsters.add(new Monster(model, model.tileSize, (13 * model.tileSize), 13));
        if (!model.player3mode) {
            model.monsters.add(new Monster(model, (13 * model.tileSize), model.tileSize, 14));
        }
        model.entities.addAll(model.monsters);
    }

    /**
     * This method is used to reset the game model to its initial state.
     * It sets the winnerId to -1, indicating that there is currently no winner.
     * It then calls the methods to clear the model arrays and reset the players and monsters.
     * The map is loaded based on the selectedMap property of the model.
     * The running flag is set to true and the game thread is started, indicating that the game is now running.
     * The currentRound property of the model is incremented by 1.
     * The view updated to show the game panel.
     *
     * @throws IOException if there is an error loading the map or resetting the players
     */
    public void resetModel() throws IOException {
        model.winnerId = -1;

        clearModelArrays();
        resetPlayers();
        resetMonsters();

        model.getMap().loadMap("/maps/map" + model.selectedMap + ".txt");

        running = true;
        startGameThread();
        model.currentRound += 1;
        view.gm.cardLayout.show(view.gm.cardPanel, "game");
    }

    /**
     * This method is used to pause or resume the game.
     * It toggles the running flag.
     * If the game is resumed, it checks if the game sound is not currently playing and if so, starts the game sound.
     * It also calls the startGameThread() method to start the game thread.
     */
    public void pauseGame() {
        running = !running;

        if (running) {
            if (model.soundManager != null && !model.soundManager.gameSound.clip.isRunning()) {
                model.soundManager.gameSound.playMusic();
            }

            startGameThread();
        }
    }

    /**
     * This method is used to initialize the KeyHandler.
     * It calls the init() method of the KeyHandler, which sets up the key bindings for the game controls.
     */
    public void initKeyHandler() {
        keyH.init();
    }

    /**
     * This method is used to get the current game model.
     * It returns the model object of this GameController.
     *
     * @return the current GameModel object
     */
    public GameModel getModel() {
        return this.model;
    }

    /**
     * This method is used to get the current game view.
     * It returns the view object of this GameController.
     *
     * @return the current GamePanel object
     */
    public GamePanel getView() {
        return this.view;
    }

    /**
     * This method is used to check if the game is currently running.
     * It returns the running flag of this GameController.
     *
     * @return the current state of the game (true if the game is running, false otherwise)
     */
    public boolean isRunning() {
        return running;
    }
}
