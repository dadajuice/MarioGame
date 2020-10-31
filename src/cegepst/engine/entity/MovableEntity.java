package cegepst.engine.entity;

import cegepst.GameSettings;
import cegepst.engine.Buffer;
import cegepst.engine.controls.Direction;

import java.awt.*;

public abstract class MovableEntity extends UpdatableEntity {

    private final Collision collision;
    private Direction direction = Direction.UP;
    protected int speed = 1;
    private boolean moved;
    private int lastX;
    private int lastY;
    private double gravity = 1; // falling speed;
    private int jumpMaxHeight = 18; // jumping max
    private int currentJumpMeter = 0;
    private boolean jumping = false;
    private boolean falling = false;

    public MovableEntity() {
        collision = new Collision(this);
    }

    @Override
    public void update() {

        // Are we jumping?
        if (jumping) {
            move(Direction.UP);
            currentJumpMeter++;
            if (currentJumpMeter == jumpMaxHeight) {
                jumping = false;
                currentJumpMeter = 0;
            }
        } else {
            // Are we falling?
            if (collision.getAllowedSpeed(Direction.DOWN) > 0) {
                fall();
            } else {
                falling = false;
                gravity = 1;
            }
        }

        moved = (x != lastX || y != lastY);
        lastX = x;
        lastY = y;
    }

    public void fall() {
        falling = true;
        move(Direction.DOWN);
        gravity += 0.15; // Acceleration constant (custom to game)
    }

    public void jump() { // Must be called only once!
        if (falling) {
            System.out.println("Cant because falling");
            return; // prevent double jumps
        }
        if (collision.getAllowedSpeed(Direction.DOWN) > 0) { // should keep in memory since its called often
            System.out.println("Cant because in midair");
            return; // prevent continous jump midair
        }
        System.out.println("jump!");
        jumping = true;
    }

    public void moveLeft() {
        move(Direction.LEFT);
    }

    public void moveRight() {
        move(Direction.RIGHT);
    }

    public void moveUp() {
        // Force to use semantic methods jump
        if (GameSettings.GRAVITY_ENABLED) {
            return;
        }
        move(Direction.UP);
    }

    public void moveDown() {
        if (GameSettings.GRAVITY_ENABLED) {
            return;
        }
        move(Direction.DOWN);
    }

    public void move(Direction direction) {
        this.direction = direction;
        if (jumping && direction == Direction.UP) {
            collision.setSpeed(3); // Speed of jump acceleration
        }
        if (direction == Direction.DOWN && falling) {
            collision.setSpeed((int) gravity);
        } else {
            if (falling) {
                collision.setSpeed(2); // Limit movement in midair
            }
        }

        int allowedSpeed = collision.getAllowedSpeed(direction);
        x += direction.getVelocityX(allowedSpeed);
        y += direction.getVelocityY(allowedSpeed);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean hasMoved() {
        return moved;
    }

    public void drawHitBox(Buffer buffer) {
        if (hasMoved()) {
            Rectangle hitBox = getCollisionBound();
            buffer.drawRectangle(hitBox.x, hitBox.y, hitBox.width, hitBox.height, new Color(255, 0, 0, 200));
        }
    }

    public boolean collisionBoundIntersectWith(StaticEntity other) {
        if (other == null) {
            return false;
        }
        return getCollisionBound().intersects(other.getBounds());
    }

    public Rectangle getCollisionBound() {
        switch (direction) {
            case UP: return getCollisionUpperBound();
            case DOWN: return getCollisionLowerBound();
            case LEFT: return getCollisionLeftBound();
            case RIGHT: return getCollisionRightBound();
        }
        return getBounds();
    }

    private Rectangle getCollisionUpperBound() {
        return new Rectangle(x, y - speed, width, speed);
    }

    private Rectangle getCollisionLowerBound() {
        return new Rectangle(x, y + height, width, speed);
    }

    private Rectangle getCollisionLeftBound() {
        return new Rectangle(x - speed, y, speed, height);
    }

    private Rectangle getCollisionRightBound() {
        return new Rectangle(x + width, y, speed, height);
    }
}
