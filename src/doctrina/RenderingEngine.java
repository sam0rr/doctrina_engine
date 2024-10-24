package Doctrina;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class RenderingEngine {

    private JFrame frame;
    private JPanel panel;
    private BufferedImage bufferedImage;
    private Graphics2D bufferEngine;

    private static RenderingEngine instance;

    private RenderingEngine(){
        initializeFrame();
        initializePanel();
    }

    public static RenderingEngine getInstance() {
        if (instance == null){
            instance = new RenderingEngine();
        }
        return instance;
    }

    public void start(){
        frame.setVisible(true);
    }

    public void stop(){
        frame.setVisible(false);
        frame.dispose();
    }

    public Canvas buildCanvas(){
        bufferedImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        bufferEngine = bufferedImage.createGraphics();
        bufferEngine.setRenderingHints(buildRenderingHints());
        return new Canvas(bufferEngine);
    }

    public void drawBufferOnScreen(){
        Graphics2D graphics = (Graphics2D) panel.getGraphics();
        graphics.drawImage(bufferedImage, 0, 0, panel);
        Toolkit.getDefaultToolkit().sync();
        graphics.dispose();
    }
    public void addKeyListener(KeyListener keyListener){
        panel.addKeyListener(keyListener);
    }

    private void initializePanel() {
        panel = new JPanel();
        panel.setBackground(Color.BLUE);
        panel.setFocusable(true);
        panel.setDoubleBuffered(true);
        frame.add(panel);
    }

    private void initializeFrame() {
        frame = new JFrame();
        frame.setSize(800, 550);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setTitle("Bouncing Balls");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setState(JFrame.NORMAL);
        frame.setUndecorated(true);
    }

    private RenderingHints buildRenderingHints() {
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        return hints;
    }
}
