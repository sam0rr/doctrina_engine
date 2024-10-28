package Viking;

import Doctrina.Blockade;
import Doctrina.Canvas;
import Doctrina.Game;
import Doctrina.StaticEntity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Tree extends StaticEntity {
    private static final String SPRITE_PATH = "images/tree.png";
    private Image image;
    private Blockade blockade;

    public Tree(int x, int y) {
        teleport(x, y);
        blockade = new Blockade(x + 20, y + 20, 40, 40); // Example adjustment
        load();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawImage(image, x, y);
        if (GameConfig.isDebugEnabled()) {
            blockade.draw(canvas); // Draw blockade for visualization if needed
        }
    }

    public void blockadeFromTop() {
        blockade.teleport(x, y + image.getHeight(null) - blockade.getHeight()); // Adjust for top edge
    }

    public void blockadeFromBottom() {
        blockade.teleport(x, y + blockade.getHeight()); // Adjust for bottom edge
    }

    private void load() {
        try {
            image = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(SPRITE_PATH));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
