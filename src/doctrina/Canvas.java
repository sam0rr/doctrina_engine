package Doctrina;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.Stack;

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
        if (paint instanceof Color && ((Color) paint).getAlpha() == 0) {
            Composite originalComposite = graphics.getComposite();
            graphics.setComposite(AlphaComposite.Clear);
            graphics.fill(new Ellipse2D.Float(x - radius, y - radius, radius * 2, radius * 2));
            graphics.setComposite(originalComposite);
        } else {
            graphics.setPaint(paint);
            graphics.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        }
    }

    public void drawRectangle(int x, int y, int width, int height, Paint paint) {
        graphics.setPaint(paint);
        graphics.fillRect(x, y, width, height);
    }

    public void drawRectangle(StaticEntity entity, Paint paint) {
        drawRectangle(entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight(), paint);
    }

    public void drawRectangle(StaticEntity entity) {
        Paint defaultPaint = new Color(0, 0, 255, 100);
        drawRectangle(entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight(), defaultPaint);
    }

    public void drawImage(Image image, int x, int y) {
        graphics.drawImage(image, x, y, null);
    }

    public Graphics2D getGraphics2D() {
        return graphics;
    }
}
