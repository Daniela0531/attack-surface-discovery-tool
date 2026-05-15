package ru.mipt.bit.platformer.logic_objects.bullet;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.logic_objects.DamageDealerModel;
import ru.mipt.bit.platformer.logic_objects.MoveModel;
import ru.mipt.bit.platformer.logic_objects.properties.Direction;

import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class BulletMoveModel implements MoveModel, DamageDealerModel {
    private final float movementSpeed;
    private GridPoint2 coordinates;
    private float progress;
    private boolean isMoving = false;
    private Direction direction;
    private int damage;
    public BulletMoveModel(GridPoint2 coordinates, Direction direction, int damage, float movementSpeed) {
        this.coordinates = coordinates;
        this.progress = 0f;
        this.direction = direction;
        this.damage = damage;
        this.movementSpeed = movementSpeed;
    }

    public GridPoint2 getDestination() {
        return new GridPoint2(coordinates.x + direction.getVector().x, coordinates.y + direction.getVector().y);
    }
    @Override
    public Direction getDirection() {
        return direction;
    }
    public void updateProgress(float deltaTime) {
        progress = continueProgress(progress, deltaTime, movementSpeed);
    }
    @Override
    public void setProgress(float progress) {
        this.progress = progress;
    }
    @Override
    public void finishMovement() {
        if (progress >= 1) {
            coordinates.x += direction.getVector().x;
            coordinates.y += direction.getVector().y;
            progress = 0f;
        }
    }
    @Override
    public boolean isMoving() {
        return isMoving;
    }
    @Override
    public void setRotation(float newRotation) {
        this.direction.setRotation(newRotation);
    }
    @Override
    public GridPoint2 getCoordinates() {
        return coordinates;
    }
    @Override
    public float getProgress() {
        return progress;
    }
    public float getRotation() {
        return direction.getRotation();
    }
    @Override
    public void setMovingStatus(boolean b) {
        this.isMoving = b;
    }
    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    @Override
    public int getDamage() {
        return damage;
    }
    @Override
    public void mainUpdateProgress(float deltaTime) {
            updateProgress(deltaTime);
    }
}

