package ru.mipt.bit.platformer.graphics_management;

import com.badlogic.gdx.graphics.g2d.Batch;
import org.springframework.stereotype.Component;
import ru.mipt.bit.platformer.graphics_objects.GraphicsInterface;
import ru.mipt.bit.platformer.level.Level;

@Component
public class BatchGraphicRender {
    private final Batch batch;
    private final Level level;

    public BatchGraphicRender(Batch batch, Level level) {
        this.batch = batch;
        this.level = level;
    }

    public void drow() {
        batch.begin();
//        level.drow(batch);
        for (GraphicsInterface graphicsInterface : level.allGraphicsEntities()) {
            graphicsInterface.draw(batch);
        }
//        public void drow(Batch batch){
//            for (Map.Entry<TreeMoveModel, GraphicsInterface> entry : trees.entrySet()) {
//                entry.getValue().draw(batch, entry.getKey());
//            }
//            for (Map.Entry<TankMoveModel, GraphicsInterface> entry : tanks.entrySet()) {
//                entry.getValue().draw(batch, entry.getKey());
//            }
//            for (Map.Entry<BulletMoveModel, GraphicsInterface> entry : bullets.entrySet()) {
//                entry.getValue().draw(batch, entry.getKey());
//            }
//            if (!playerKilled) {
//                playerGraphics.draw(batch, playerTank);
//            }
//        }
        batch.end();
    }

    public void dispose() {
        level.dispose();
        batch.dispose();
    }
}
