package ru.mipt.bit.platformer.actions.impl_action;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.actions.Action;
import ru.mipt.bit.platformer.level.Level;
import ru.mipt.bit.platformer.logic_objects.DamageDealerModel;
import ru.mipt.bit.platformer.logic_objects.ShootableModel;
import ru.mipt.bit.platformer.logic_objects.properties.Direction;
import ru.mipt.bit.platformer.logic_objects.tank.TankMoveModel;
import ru.mipt.bit.platformer.logic_objects.tree.TreeMoveModel;

public class ShootAction implements Action {
    private final Direction direction;
    private final ShootableModel shootableModel;
    private final DamageDealerModel damageDealerModel;
    private boolean isFinished = false;
    private boolean isStarted = false;

    public ShootAction(ShootableModel shootableModel,
                       DamageDealerModel damageDealerModel, Direction direction) {
        this.direction = direction;
        this.shootableModel = shootableModel;
        this.damageDealerModel = damageDealerModel;
    }
    @Override
    public boolean isFinished() {
        return isFinished;
    }
    @Override
    public void execute(Level level) {
        if (!shootableModel.mayShoot() && !isStarted) {
            finished();
            return;
        }
        if (!isStarted) {
            isStarted = true;
            damageDealerModel.setMovingStatus(true);
        }

        damageDealerModel.setMovingStatus(true);
        shootableModel.updateFireProgress();
        executeBulletMovement(level);
    }
    private void finished() {
        this.isFinished = true;
    }
    private void executeBulletMovement(Level level) {
        GridPoint2 newCoordinates = damageDealerModel.getCoordinates().cpy();
        if (!(newCoordinates.x < level.getLeftBound() ||
                newCoordinates.x > level.getRightBound() ||
                newCoordinates.y < level.getLowBound() ||
                newCoordinates.y > level.getUpBound())) {
            for(TreeMoveModel obstacle : level.getTrees().keySet()) {
                if (obstacle.getCoordinates().equals(newCoordinates)) {
                    finishShootAction();
                    return;
                }
            }
            for(TankMoveModel tank : level.getTanks().keySet()) {
                if (tank.getCoordinates().equals(newCoordinates)) {
                    tank.damage(damageDealerModel.getDamage());
                    finishShootAction();
                    return;
                }
            }
            if (!level.isPlayerKilled() && level.getPlayerTank().getCoordinates().equals(newCoordinates)) {
                level.getPlayerTank().damage(damageDealerModel.getDamage());
                finishShootAction();
                return;
            }
            damageDealerModel.finishMovement();
        } else {
            finishShootAction();
        }
    }
    private void finishShootAction() {
        damageDealerModel.setProgress(0f);
        damageDealerModel.setMovingStatus(false);
        shootableModel.finishShooting();
        finished();
    }
}
