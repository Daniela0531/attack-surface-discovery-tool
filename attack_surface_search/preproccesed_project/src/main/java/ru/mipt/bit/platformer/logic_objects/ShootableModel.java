package ru.mipt.bit.platformer.logic_objects;

public interface ShootableModel {
    void updateFireProgress();
    boolean mayShoot();
    void finishShooting();
}
