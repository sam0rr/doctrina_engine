package Doctrina;

import java.awt.*;

public class Canvas {

    private Graphics2D graphics;

    public Canvas(Graphics2D graphics) {
        this.graphics = graphics;
    }

    public void drawString(String text, int x, int y, Paint paint) {
        graphics.setPaint(paint);
        graphics.drawString(text, x, y);
    }

    public void drawCircle(int x, int y, int radius, Paint paint) {
        System.out.println("Drawing circle at (" + x + ", " + y + ") with radius " + radius);  // Debugging output
        graphics.setPaint(paint);
        graphics.fillOval(x - radius, y - radius, radius * 2, radius * 2);  // Ensure proper positioning
    }

    public void drawRectangle(StaticEntity entity, Paint paint) {
        drawRectangle(entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight(), paint);
    }

    public void drawRectangle(int x, int y, int width, int height, Paint paint) {
        graphics.setPaint(paint);
        graphics.fillRect(x, y, width, height);
    }

    public void drawImage(Image image, int x, int y) {
        graphics.drawImage(image, x, y, null);
    }

    // Getter for Graphics2D object
    public Graphics2D getGraphics2D() {
        return graphics;
    }
}
