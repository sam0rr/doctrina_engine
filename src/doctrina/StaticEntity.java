package Doctrina;

import java.awt.*;

public abstract class StaticEntity {
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    // Default constructor
    public StaticEntity() {}

    // Constructor to initialize x and y
    public StaticEntity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void draw(Canvas canvas);

    public void teleport(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setDimensions(int width, int height){
        this.width = width;
        this.height = height;
    }

    public boolean intersectWidth(StaticEntity entity) {
        return getBounds().intersects(entity.getBounds());
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
