package ru.mipt.bit.platformer.logic_objects;

import com.badlogic.gdx.math.GridPoint2;

public interface Model {
    public GridPoint2 getCoordinates();
    void mainUpdateProgress(float deltaTime);

    float getRotation();
}
