package Doctrina;
import java.awt.*;

public abstract class MovableEntity extends StaticEntity {

    private int speed = 1;
    private boolean moved = false;
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    private Direction direction = Direction.UP;

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    private int lastX = Integer.MIN_VALUE;
    private int lastY = Integer.MIN_VALUE;
    private Collision collision;

    public void update() {
        moved = false;
    }

    public MovableEntity() {
        collision = new Collision(this);
    }



    public void move() {
        int allowedSpeed = collision.getAllowedSpeed();
        x += direction.calculateVelocityX(allowedSpeed);
        y += direction.calculateVelocityY(allowedSpeed);

        moved = (x != lastX || y != lastY);
        lastX = x;
        lastY = y;
    }

    public void move(Direction direction) {
        this.direction = direction;
        move();
    }

    public Rectangle getHitBox() {
        return switch (direction) {
            case UP -> getUpperHitBox();
            case DOWN -> getLowerHitBox();
            case LEFT -> getLeftHitBox();
            case RIGHT -> getRightHitBox();
            default -> getBounds();
        };
    }

    private Rectangle getUpperHitBox() {
        return new Rectangle(x, y - speed, width, speed);
    }

    private Rectangle getLowerHitBox() {
        return new Rectangle(x, y + height, width, speed);
    }

    private Rectangle getLeftHitBox() {
        return new Rectangle(x - speed, y , speed, height);
    }

    private Rectangle getRightHitBox() {
        return new Rectangle(x + width , y, speed, height);
    }

    public boolean hitBoxIntersectWith(StaticEntity other) {
        if (other == null) {
            return false;
        }
        return getHitBox().intersects(other.getBounds());
    }

    public void drawHitBox(Canvas canvas) {
        Rectangle rect = getHitBox();
        canvas.drawRectangle(rect.x, rect.y, rect.width, rect.height, Color.BLUE);
    }

    public void moveUp() {
        move(Direction.UP);
    }

    public void moveDown() {
        move(Direction.DOWN);
    }

    public void moveLeft() {
        move(Direction.LEFT);
    }

    public void moveRight() {
        move(Direction.RIGHT);
    }

    public boolean hasMoved() {
        return moved;
    }

}
