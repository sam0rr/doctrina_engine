package Tank;

import Doctrina.Canvas;
import Doctrina.Game;

import java.util.ArrayList;

public class TankGame extends Game {
    private GamePad gamePad;
    private Tank tank;
    private ArrayList<Missile> missiles;
    private ArrayList<Brick> bricks;

    @Override
    protected void initialize() {
        gamePad = new GamePad();
        tank = new Tank(gamePad);
        missiles = new ArrayList<>();

        bricks = new ArrayList<>();
        bricks.add(new Brick(4,4));
        bricks.add(new Brick(400,500));
        bricks.add(new Brick(400,600));
        bricks.add(new Brick(600,400));

    }

    @Override
    protected void update() {

        if (gamePad.isQuitPressed()) {
            stop();
        }

        if (gamePad.isFiredPressed() && tank.canFire()) {
            missiles.add(tank.fire());
        }

        tank.update();

        for (Missile missile : missiles) {
            missile.update();
        }
    }

    @Override
    protected void draw(Canvas canvas) {
        tank.draw(canvas);
        for (Missile missile : missiles) {
            missile.draw(canvas);
        }
        for (Brick brick : bricks) {
            brick.draw(canvas);
        }

    }

}
