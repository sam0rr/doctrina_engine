package Doctrina;

import java.awt.*;

public class Blockade extends StaticEntity {

    private final int width;
    private final int height;

    public Blockade(int x, int y, int width, int height) {
        super(x, y);
        this.width = width;
        this.height = height;
        CollidableRepository.getInstance().registerEntity(this);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRectangle(this, new Color(255, 0, 0, 100));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
