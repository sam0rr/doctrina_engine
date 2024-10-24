package BattleRoyal;

import Doctrina.Canvas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class World {
    private static final String MAP_PATH = "images/demo.png";
    private Image background;
    private int width;  // Store the width of the world
    private int height; // Store the height of the world

    public void load() {
        try {
            background = ImageIO.read(
                    this.getClass().getClassLoader().getResourceAsStream(MAP_PATH)
            );
            width = background.getWidth(null);  // Get the width of the background
            height = background.getHeight(null);  // Get the height of the background
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            //System.out.println("TOUJOURS EXECUTER");
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawImage(background, 0, -64);  // Draw the world background
    }

    // Get the width of the world
    public int getWidth() {
        return width;
    }

    // Get the height of the world
    public int getHeight() {
        return height;
    }
}
