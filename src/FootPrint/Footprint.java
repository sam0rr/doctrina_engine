package FootPrint;

import Doctrina.Canvas;
import Doctrina.StaticEntity;

import java.awt.*;
import java.util.Random;

public class Footprint extends StaticEntity {

    public Footprint(int x, int y) {
        teleport(x, y);
        setDimensions(5, 5);
    }

    @Override
    public void draw(Canvas canvas) {
        Color color = new Color(getRandomValue(), getRandomValue(), getRandomValue());
        canvas.drawRectangle(this, color);
    }
    private int getRandomValue() {
        return new Random().nextInt(256);
    }

}
