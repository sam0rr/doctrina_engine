package BattleRoyal;

public class Camera {
    private int x;
    private int y;
    private final int width;
    private final int height;
    private final World world;

    public Camera(int width, int height, World world) {
        this.width = width;
        this.height = height;
        this.world = world;
        this.x = 0;
        this.y = 0;
    }

    public void update(Player player) {
        // Center the camera on the player’s position
        this.x = player.getX() - width / 2;
        this.y = player.getY() - height / 2;

        // Constrain camera within world bounds
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x + width > world.getWidth()) x = world.getWidth() - width;
        if (y + height > world.getHeight()) y = world.getHeight() - height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
