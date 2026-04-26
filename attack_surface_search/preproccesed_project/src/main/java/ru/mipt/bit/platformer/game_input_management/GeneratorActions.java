package ru.mipt.bit.platformer.game_input_management;

import com.badlogic.gdx.math.GridPoint2;
import org.springframework.stereotype.Component;
import ru.mipt.bit.platformer.actions.impl_action.MoveAction;
import ru.mipt.bit.platformer.actions.impl_action.ShootAction;
import ru.mipt.bit.platformer.button_commands.ButtonCommand;
import ru.mipt.bit.platformer.level.Level;
import ru.mipt.bit.platformer.logic_objects.bullet.BulletMoveModel;
import ru.mipt.bit.platformer.logic_objects.properties.Direction;
import ru.mipt.bit.platformer.logic_objects.tank.TankMoveModel;

import java.util.Random;

@Component
public class GeneratorActions {
    private final Level level;
    private final CommandQueue commandQueueHandler;

    public GeneratorActions(Level level, CommandQueue commandQueueHandler) {
        this.level = level;
        this.commandQueueHandler = commandQueueHandler;
    }

    public void getCommand() {
        if (level.getTanks().isEmpty()) {
            return;
        }
        Random random = new Random();
        int randomNumber = random.nextInt(50);
        int i = random.nextInt(level.moveNodesSize())%level.moveNodesSize();
        int k = 0;
        TankMoveModel tank = null;
        for (TankMoveModel tankMoveModel : level.getTanks().keySet()) {
            if (k == i) {
                if (!level.isPlayerKilled() && tankMoveModel.getCoordinates() == level.getPlayerTank().getCoordinates()) {
                    return;
                }
                tank = tankMoveModel;
                break;
            }
            ++k;
        }
        if (randomNumber < 10) {
            MoveAction moveAction = new MoveAction(
                    tank,
                    new Direction(ButtonCommand.UP)
            );
            commandQueueHandler.addAction(moveAction);
        } else if (randomNumber < 20) {
            MoveAction moveAction = new MoveAction(
                    tank,
                    new Direction(ButtonCommand.LEFT)
            );
            commandQueueHandler.addAction(moveAction);
        } else if (randomNumber < 30) {
            MoveAction moveAction = new MoveAction(
                    tank,
                    new Direction(ButtonCommand.DOWN)
            );
            commandQueueHandler.addAction(moveAction);
        } else if (randomNumber < 40) {
            MoveAction moveAction = new MoveAction(
                    tank,
                    new Direction(ButtonCommand.RIGHT)
            );
            commandQueueHandler.addAction(moveAction);
        } else if (randomNumber == 41) {
            Direction direction = new Direction(
                    tank.getRotation());
            GridPoint2 coord = new GridPoint2(
                    tank.getCoordinates().cpy().x + direction.getVector().x,
                    tank.getCoordinates().cpy().y + direction.getVector().y);
            BulletMoveModel bulletMoveModel = level.putBulletInLevel(coord, direction);

            ShootAction shootAction = new ShootAction(
                    tank, bulletMoveModel, direction);
            commandQueueHandler.addAction(shootAction);
        }
//        else {
//            SwitchHealthBarAction switchHealthBarAction = new SwitchHealthBarAction(tank);
//            receivedCommands.addAction(switchHealthBarAction);
//        }
    }
}
