package ru.mipt.bit.platformer.logic_objects;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.logic_objects.properties.Direction;

public interface MoveModel extends Model {
//    GridPoint2 getCoordinates();
//    public float getRotation();
    void setMovingStatus(boolean b);
    void finishMovement();
    boolean isMoving();
//    void updateProgress(float deltaTime);
    GridPoint2 getDestination();
    void setProgress(float v);
    Direction getDirection();
    void setRotation(float rotation);
    float getProgress();
    void setDirection(Direction direction);
}
