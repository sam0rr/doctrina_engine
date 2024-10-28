package BattleRoyal;

import Doctrina.Canvas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class World {
    private static final String MAP_PATH = "images/demo.png";
    private Image background;
    private int width;  // La largeur totale du monde
    private int height; // La hauteur totale du monde
    private static final int Y_OFFSET = 64; // Décalage pour enlever les 64 pixels du haut

    public void load() {
        try {
            background = ImageIO.read(
                    this.getClass().getClassLoader().getResourceAsStream(MAP_PATH)
            );
            width = 3000;  // Largeur du monde
            height = 3000;  // Hauteur du monde
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void draw(Canvas canvas, int offsetX, int offsetY) {
        // Dessine l'image de fond en ignorant les 64 pixels du haut
        canvas.getGraphics2D().drawImage(
                background,         // Image d'origine
                offsetX, offsetY,   // Position de destination dans le canvas
                offsetX + width,    // Largeur finale dans le canvas
                offsetY + height,   // Hauteur finale dans le canvas
                0, Y_OFFSET,        // Point de départ dans l'image source
                background.getWidth(null), background.getHeight(null), // Largeur et hauteur totales de l'image source
                null
        );
    }

    // Récupère la largeur du monde
    public int getWidth() {
        return width;
    }

    // Récupère la hauteur du monde
    public int getHeight() {
        return height;
    }
}

