package Tank;

import Doctrina.MovementController;

import java.awt.event.KeyEvent;

public class GamePad extends MovementController {
    private final int quitKey = KeyEvent.VK_ESCAPE;
    private final int fireKey = KeyEvent.VK_SPACE;


    public GamePad() {
        useWasdKeys();
        bindKey(quitKey);
        bindKey(fireKey);
    }

    public boolean isQuitPressed() {
        return isKeyPressed(quitKey);
    }

    public boolean isFirePressed() {
        return isKeyPressed(fireKey);
    }
}
