package ru.mipt.bit.platformer.graphics_objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

//@Component
public interface GraphicsInterface {
     Rectangle getRectangle();

    void dispose();

    void draw(Batch batch);
}
