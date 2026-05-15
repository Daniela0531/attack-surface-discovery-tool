package ru.mipt.bit.platformer.logic_objects.tank;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.logic_objects.LivableModel;
import ru.mipt.bit.platformer.logic_objects.MoveModel;
import ru.mipt.bit.platformer.logic_objects.ShootableModel;
import ru.mipt.bit.platformer.logic_objects.properties.Direction;

import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;


// progress = 0f; - нет действий
// progress = 1f; - действие завершилось


public class TankMoveModel implements MoveModel, LivableModel, ShootableModel {
    private final float movementSpeed;
    private final int fireTimeaot = 50;
    private final int healthBarTimeout = 40;
    private int healthBarCur = 0;
    private GridPoint2 coordinates;
    private float progress;
    private boolean isMoving = false;
    private Direction direction;
    private int health;
//    private int maxHealth = 5;
    private boolean isHealthBarRaise = false;
    private int shootProgress = 0;
    private boolean isShooting = false;
    private boolean waitSwitching = true;
    private final int maxHealth;
    public TankMoveModel(GridPoint2 coordinates, float rotation, int maxHealth, float movementSpeed) {
        this.movementSpeed = movementSpeed;
        this.coordinates = coordinates;
        this.progress = 0f;
        this.direction = new Direction(new GridPoint2(0,0), rotation);
        this.health = maxHealth;
        this.maxHealth = maxHealth;
    }
    @Override
    public boolean isMoving() {
        return isMoving;
    }
    @Override
    public GridPoint2 getDestination() {
        return new GridPoint2(coordinates.x + direction.getVector().x, coordinates.y + direction.getVector().y);
    }
    @Override
    public Direction getDirection() {
        return direction;
    }
//    @Override
    public void updateProgress(float deltaTime) {
        progress = continueProgress(progress, deltaTime, movementSpeed);
    }
    @Override
    public void updateFireProgress() {
        shootProgress += 1;
        if (shootProgress >= fireTimeaot) {
            shootProgress = 0;
            isShooting = false;
        }
    }
    @Override
    public boolean mayShoot() {
        return shootProgress == 0;
    }
    @Override
    public void finishShooting() {
        shootProgress = 0;
        isShooting = false;
    }
    @Override
    public void setProgress(float progress) {
        this.progress = progress;
    }
    @Override
    public void finishMovement() {
        coordinates.x += direction.getVector().x;
        coordinates.y += direction.getVector().y;
        direction.setVector(new GridPoint2(0, 0));
    }
    @Override
    public void setRotation(float newPlayerRotation) {
        this.direction.setRotation(newPlayerRotation);
    }
    @Override
    public GridPoint2 getCoordinates() {
        return coordinates;
    }
//    @Override
//    public boolean equalsTo(Model model) {
//        if (model instanceof TankMoveModel) {
//            return (coordinates.x == ((TankMoveModel) model).getCoordinates().x &&
//                    coordinates.y == ((TankMoveModel) model).getCoordinates().y);
//        }
//        return false;
//    }
    @Override
    public float getProgress() {
        return progress;
    }
    @Override
    public float getRotation() {
        return direction.getRotation();
    }
    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    @Override
    public void setMovingStatus(boolean status) {
        this.isMoving = status;
    }
    @Override
    public int getCurrentHealth() {
        return health;
    }
    @Override
    public void damage(int damage) {
        this.health -= damage;
    }
    @Override
    public void switchHealthBar() {
        if (waitSwitching) {
            waitSwitching = false;
            this.isHealthBarRaise = !isHealthBarRaise;
            this.healthBarCur = 0;
            System.out.println(
                    "switchHealthBar\n" +
                    "    now is rase: " + isHealthBarRaise +
                    "    health bar cur: " + healthBarCur);
        }
    }
//    @Override
//    public boolean isHealthBarRaise() {
//        return isHealthBarRaise;
//    }
//    @Override
//    public float getMaxHealth() {
//        return 0;
//    }

//    @Override
    private void updateHealthBar() {
        if (healthBarCur == healthBarTimeout) {
            waitSwitching = true;
        }
        if (healthBarCur < healthBarTimeout) {
            healthBarCur += 1;
        }
    }
//    @Override
//    public boolean maySwitchHealthBar() {
//        return healthBarCur == 0 || healthBarCur == healthBarTimeout;
//    }

    @Override
    public void mainUpdateProgress(float deltaTime) {
        if (isMoving) {
            updateProgress(deltaTime);
        }
        updateHealthBar();
        if (isShooting) {
            updateFireProgress();
        }
    }
    @Override
    public boolean isHealthBarRaise() {
        return isHealthBarRaise;
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }
}
