package ru.mipt.bit.platformer.logic_objects.properties;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.button_commands.ButtonCommand;

//@Entity
//@Table(name = "stories")
//@Component
public class Direction {

    private GridPoint2 vector = new GridPoint2(0, 0);

    private float rotation = 0f;

    public Direction(ButtonCommand command) {
        if (command == ButtonCommand.UP) {
            this.vector = new GridPoint2(0, 1);
            this.rotation = 90f;
        }
        if (command == ButtonCommand.LEFT) {
            this.vector = new GridPoint2(-1, 0);
            this.rotation = 180f;
        }
        if (command == ButtonCommand.DOWN) {
            this.vector = new GridPoint2(0, -1);
            this.rotation = 270f;
        }
        if (command == ButtonCommand.RIGHT) {
            this.vector = new GridPoint2(1, 0);
            this.rotation = 0f;
        }
        if (command == ButtonCommand.NONE) {
            this.vector = new GridPoint2(0, 0);
            this.rotation = 0f;
        }
    }

    public Direction(float rotation) {
        this.rotation = rotation;
        if (rotation == 90f) {
            this.vector = new GridPoint2(0, 1);
        }
        if (rotation == 180f) {
            this.vector = new GridPoint2(-1, 0);
        }
        if (rotation == 270f) {
            this.vector = new GridPoint2(0, -1);
        }
        if (rotation == 0f) {
            this.vector = new GridPoint2(1, 0);
        }
    }
    public Direction(GridPoint2 vector, float rotation) {
        this.vector = vector;
        this.rotation = rotation;
    }

    public GridPoint2 getVector() {
        return vector;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getRotation() {
        return rotation;
    }

    public void setVector(GridPoint2 vector) {
        this.vector = vector;
    }
}
