package BattleRoyal;

import Doctrina.MovementController;

import java.awt.event.KeyEvent;

public class GamePad extends MovementController {

    private int quitKey = KeyEvent.VK_ESCAPE;

    private int fireKey = KeyEvent.VK_SPACE;

    public GamePad() {
        useWasdKeys();
        bindKey(quitKey);
        bindKey(fireKey);
    }

    public boolean isFirePressed() {
        return isKeyPressed(fireKey);
    }

    public boolean isQuitPressed() {
        return isKeyPressed(quitKey);
    }

}