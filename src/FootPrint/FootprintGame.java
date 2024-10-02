package FootPrint;

import Doctrina.Canvas;
import Doctrina.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class FootprintGame extends Game {
    private GamePad gamePad1;
    private GamePad gamePad2;
    private Player player1;
    private Player player2;
    private ArrayList<Footprint> footprints;
    @Override
    protected void initialize() {
        gamePad1 = new GamePad();
        player1 = new Player(gamePad1);

        gamePad2 = new GamePad();
        gamePad2.useWasdKeys();
        player2 = new Player(gamePad2);

        footprints = new ArrayList<>();
    }

    @Override
    protected void update() {
        if (gamePad1.isQuitPressed()){
            stop();
        }
        player1.update();
        player2.update();
        if (gamePad1.isMoving()){
            footprints.add(player1.layFootprint());
        }
        if (gamePad2.isMoving()){
            footprints.add(player2.layFootprint());
        }
    }

    @Override
    protected void draw(Canvas canvas) {
        canvas.drawRectangle(0, 0, 800, 600, Color.BLUE);
        for (Footprint footprint:footprints) {
            footprint.draw(canvas);
        }
        player1.draw(canvas);
        player2.draw(canvas);
    }
}
