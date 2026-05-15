package ru.mipt.bit.platformer.logic_objects.tree;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.logic_objects.Model;

public class TreeMoveModel implements Model {
    private GridPoint2 coordinates;
    private float rotation;
    public TreeMoveModel(GridPoint2 coordinates, float rotation) {
        this.coordinates = coordinates;
        this.rotation = rotation;
    }

    @Override
    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    @Override
    public void mainUpdateProgress(float deltaTime) {
    }

    @Override
    public float getRotation() {
        return rotation;
    }


}
