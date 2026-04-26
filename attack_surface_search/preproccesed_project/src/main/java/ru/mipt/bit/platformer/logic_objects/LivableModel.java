package ru.mipt.bit.platformer.logic_objects;

public interface LivableModel extends Model {
    int getCurrentHealth();
    void damage(int damage);
    void switchHealthBar();

    boolean isHealthBarRaise();

    int getMaxHealth();
}
