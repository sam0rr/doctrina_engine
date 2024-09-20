package MovingRectangle;

import Doctrina.Canvas;
import Doctrina.Game;
import java.awt.*;

public class MovingRectangleGame extends Game {

    private Player player;
    private Npc npc;
    private GamePad gamePad;
    @Override
    protected void initialize() {
        gamePad = new GamePad();
        super.addKeyListener(gamePad);
        player = new Player(gamePad);
        npc = new Npc();
    }

    @Override
    protected void update() {
        if (gamePad.isQuitPressed()){
            stop();
        }
        player.update();
        npc.update();
    }

    @Override
    protected void draw(Canvas canvas) {
        canvas.drawRectangle(0, 0, 800, 600, Color.BLUE);
        player.draw(canvas);
        npc.draw(canvas);
    }
}
