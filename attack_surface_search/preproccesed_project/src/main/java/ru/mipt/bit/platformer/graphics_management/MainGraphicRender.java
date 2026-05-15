package ru.mipt.bit.platformer.graphics_management;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.math.Interpolation;
import org.springframework.stereotype.Component;
import ru.mipt.bit.platformer.graphics_objects.GraphicsInterface;
import ru.mipt.bit.platformer.level.Level;
import ru.mipt.bit.platformer.level_properties.GraphicProperties;
import ru.mipt.bit.platformer.logic_objects.tree.TreeMoveModel;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.Map;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

@Component
public class MainGraphicRender {
    private MapRenderer mapRenderer;
    private BatchGraphicRender batchGraphicRender;
    private MovementGraphicRender movementGraphicRender;

    public MainGraphicRender(GraphicProperties graphicProperties, Level level) {
        Batch batch = new SpriteBatch();
        TileMovement tileMovement = new TileMovement(graphicProperties.getTiledMapTileLayer(), Interpolation.smooth);
        this.mapRenderer = createSingleLayerMapRenderer(level.getTiledMap(), batch);
        for(Map.Entry<TreeMoveModel, GraphicsInterface> entry : level.getTrees().entrySet()) {
            moveRectangleAtTileCenter(tileMovement.getTileLayer(), entry.getValue().getRectangle(), entry.getKey().getCoordinates());
        }
        this.batchGraphicRender = new BatchGraphicRender(batch, level);
        this.movementGraphicRender = new MovementGraphicRender(tileMovement, level);
    }

    public void render(Level level) {
        clear();
        mapRenderer.render();
        batchGraphicRender.drow();
        movementGraphicRender.drow();
    }

    private void clear() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }
    public void dispose() {
        batchGraphicRender.dispose();
    }
}
