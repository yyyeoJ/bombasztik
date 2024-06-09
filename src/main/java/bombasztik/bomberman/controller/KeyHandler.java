package bombasztik.bomberman.controller;

import bombasztik.bomberman.entity.Direction;
import bombasztik.bomberman.entity.Player;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class KeyHandler {
    private Player player1;
    private Player player2;
    private Player player3;
    private final GameController controller;

    public KeyHandler(GameController controller) {
        this.controller = controller;
    }

    /**
     * This method initializes the KeyHandler.
     * It sets up the players and their key bindings.
     * Then first assigns the first two players from the game model to player1 and player2.
     * If there are three players in the game model, it also assigns the third player to player3.
     * Then it sets up the key bindings for player1 and player2.
     * If the game is in player3 mode, it also sets up the key bindings for player3.
     */
    public void init() {
        this.player1 = controller.getModel().players.get(0);
        this.player2 = controller.getModel().players.get(1);

        if (controller.getModel().players.size() == 3) {
            this.player3 = controller.getModel().players.get(2);
        }

        // Set up key bindings for player 1
        InputMap inputMap1 = controller.getView().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap1 = controller.getView().getActionMap();
        setupKeyBindings(inputMap1, actionMap1, KeyEvent.VK_W, "player1_up", true);
        setupKeyBindings(inputMap1, actionMap1, KeyEvent.VK_A, "player1_left", true);
        setupKeyBindings(inputMap1, actionMap1, KeyEvent.VK_S, "player1_down", true);
        setupKeyBindings(inputMap1, actionMap1, KeyEvent.VK_D, "player1_right", true);
        setupKeyBindings(inputMap1, actionMap1, KeyEvent.VK_W, "player1_up_release", false);
        setupKeyBindings(inputMap1, actionMap1, KeyEvent.VK_A, "player1_left_release", false);
        setupKeyBindings(inputMap1, actionMap1, KeyEvent.VK_S, "player1_down_release", false);
        setupKeyBindings(inputMap1, actionMap1, KeyEvent.VK_D, "player1_right_release", false);
        setupKeyBindings(inputMap1, actionMap1, KeyEvent.VK_E, "player1_place_bomb", true);
        setupKeyBindings(inputMap1, actionMap1, KeyEvent.VK_Q, "player1_place_box", true);
        setupKeyBindings(inputMap1, actionMap1, KeyEvent.VK_ESCAPE, "pause_game", true);

        // Set up key bindings for player 2
        InputMap inputMap2 = controller.getView().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap2 = controller.getView().getActionMap();
        setupKeyBindings(inputMap2, actionMap2, KeyEvent.VK_UP, "player2_up", true);
        setupKeyBindings(inputMap2, actionMap2, KeyEvent.VK_LEFT, "player2_left", true);
        setupKeyBindings(inputMap2, actionMap2, KeyEvent.VK_DOWN, "player2_down", true);
        setupKeyBindings(inputMap2, actionMap2, KeyEvent.VK_RIGHT, "player2_right", true);
        setupKeyBindings(inputMap2, actionMap2, KeyEvent.VK_UP, "player2_up_release", false);
        setupKeyBindings(inputMap2, actionMap2, KeyEvent.VK_LEFT, "player2_left_release", false);
        setupKeyBindings(inputMap2, actionMap2, KeyEvent.VK_DOWN, "player2_down_release", false);
        setupKeyBindings(inputMap2, actionMap2, KeyEvent.VK_RIGHT, "player2_right_release", false);
        setupKeyBindings(inputMap2, actionMap2, KeyEvent.VK_MINUS, "player2_place_bomb", true);
        setupKeyBindings(inputMap2, actionMap2, KeyEvent.VK_PERIOD, "player2_place_box", true);

        if (controller.getModel().player3mode) {
            // Set up key bindings for player 1
            InputMap inputMap3 = controller.getView().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            ActionMap actionMap3 = controller.getView().getActionMap();
            setupKeyBindings(inputMap3, actionMap3, KeyEvent.VK_I, "player3_up", true);
            setupKeyBindings(inputMap3, actionMap3, KeyEvent.VK_J, "player3_left", true);
            setupKeyBindings(inputMap3, actionMap3, KeyEvent.VK_K, "player3_down", true);
            setupKeyBindings(inputMap3, actionMap3, KeyEvent.VK_L, "player3_right", true);
            setupKeyBindings(inputMap3, actionMap3, KeyEvent.VK_I, "player3_up_release", false);
            setupKeyBindings(inputMap3, actionMap3, KeyEvent.VK_J, "player3_left_release", false);
            setupKeyBindings(inputMap3, actionMap3, KeyEvent.VK_K, "player3_down_release", false);
            setupKeyBindings(inputMap3, actionMap3, KeyEvent.VK_L, "player3_right_release", false);
            setupKeyBindings(inputMap3, actionMap3, KeyEvent.VK_O, "player3_place_bomb", true);
            setupKeyBindings(inputMap3, actionMap3, KeyEvent.VK_U, "player3_place_box", true);
        }
    }

    /**
     * Sets up the key bindings for the players.
     * This method determine if the key is pressed.
     * It maps the keystroke to the actionKey in the inputMap and actionMap.
     * It also defines the actions to be performed when the keys are pressed or released.
     *
     * @param inputMap  the InputMap object where the keystroke is mapped
     * @param actionMap the ActionMap object where the action is mapped
     * @param keyCode   the key code of the keystroke
     * @param actionKey the key of the action to be performed
     * @param pressed   a boolean value that determines if the key is pressed or not
     */
    private void setupKeyBindings(InputMap inputMap, ActionMap actionMap, int keyCode, String actionKey, boolean pressed) {
        inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, !pressed), actionKey);
        actionMap.put(actionKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (actionKey) {
                    case "player1_up" -> {
                        player1.setUpPressed(pressed);
                        player1.direction = Direction.UP;
                    }
                    case "player1_left" -> {
                        player1.setLeftPressed(pressed);
                        player1.direction = Direction.LEFT;
                    }
                    case "player1_down" -> {
                        player1.setDownPressed(pressed);
                        player1.direction = Direction.DOWN;
                    }
                    case "player1_right" -> {
                        player1.setRightPressed(pressed);
                        player1.direction = Direction.RIGHT;
                    }
                    case "player1_up_release" -> player1.setUpPressed(false);
                    case "player1_left_release" -> player1.setLeftPressed(false);
                    case "player1_down_release" -> player1.setDownPressed(false);
                    case "player1_right_release" -> player1.setRightPressed(false);
                    case "player1_place_bomb" -> player1.setPlaceBombPressed(pressed);
                    case "player1_place_box" -> player1.setPlaceBoxPressed(pressed);
                    case "pause_game" -> controller.pauseGame();

                    case "player2_up" -> {
                        player2.setUpPressed(pressed);
                        player2.direction = Direction.UP;
                    }
                    case "player2_left" -> {
                        player2.setLeftPressed(pressed);
                        player2.direction = Direction.LEFT;
                    }
                    case "player2_down" -> {
                        player2.setDownPressed(pressed);
                        player2.direction = Direction.DOWN;
                    }
                    case "player2_right" -> {
                        player2.setRightPressed(pressed);
                        player2.direction = Direction.RIGHT;
                    }
                    case "player2_up_release" -> player2.setUpPressed(false);
                    case "player2_left_release" -> player2.setLeftPressed(false);
                    case "player2_down_release" -> player2.setDownPressed(false);
                    case "player2_right_release" -> player2.setRightPressed(false);
                    case "player2_place_bomb" -> player2.setPlaceBombPressed(pressed);
                    case "player2_place_box" -> player2.setPlaceBoxPressed(pressed);

                    case "player3_up" -> {
                        player3.setUpPressed(pressed);
                        player3.direction = Direction.UP;
                    }
                    case "player3_left" -> {
                        player3.setLeftPressed(pressed);
                        player3.direction = Direction.LEFT;
                    }
                    case "player3_down" -> {
                        player3.setDownPressed(pressed);
                        player3.direction = Direction.DOWN;
                    }
                    case "player3_right" -> {
                        player3.setRightPressed(pressed);
                        player3.direction = Direction.RIGHT;
                    }
                    case "player3_up_release" -> player3.setUpPressed(false);
                    case "player3_left_release" -> player3.setLeftPressed(false);
                    case "player3_down_release" -> player3.setDownPressed(false);
                    case "player3_right_release" -> player3.setRightPressed(false);
                    case "player3_place_bomb" -> player3.setPlaceBombPressed(pressed);
                    case "player3_place_box" -> player3.setPlaceBoxPressed(pressed);
                }
            }
        });
    }

    /**
     * Sets the Player1 object.
     *
     * @param p the Player object to be set as Player1
     */
    public void setPlayer1(Player p) {
        this.player1 = p;
    }

    /**
     * Sets the Player2 object.
     *
     * @param p the Player object to be set as Player2
     */
    public void setPlayer2(Player p) {
        this.player2 = p;
    }

    /**
     * Sets the Player3 object.
     *
     * @param p the Player object to be set as Player3
     */
    public void setPlayer3(Player p) {
        this.player3 = p;
    }
}
