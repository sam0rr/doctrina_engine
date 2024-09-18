import java.awt.*;

public abstract class Game {


    private boolean playing = true;
    private RenderingEngine renderingEngine;
    private GameTime gameTime;

    protected abstract void initialize();
    protected abstract void update();
    protected abstract void draw(Canvas bufferEngine);

    public Game(){
        renderingEngine = new RenderingEngine();
    }

    public final void start() {
        initialize();
        run();
    }

    private void run() {
        renderingEngine.start();
        gameTime = new GameTime();
        while(playing) {
            update();
            draw(renderingEngine.buildCanvas());
            renderingEngine.drawBufferOnScreen();
            gameTime.sleep();
        }
    }


}
