package ru.mipt.bit.platformer.level_properties;

//@Component
public class LogicProperties {
    private final int tankMaxHealth;
    private final float tankSpeed;
    private final int bulletDamage;
    private final float bulletSpeed;

    public LogicProperties(int tankMaxHealth, float tankSpeed, int bulletDamage, float bulletSpeed) {
        this.tankMaxHealth = tankMaxHealth;
        this.tankSpeed = tankSpeed;
        this.bulletDamage = bulletDamage;
        this.bulletSpeed = bulletSpeed;
    }

    public float getBulletSpeed() {
        return bulletSpeed;
    }

    public float getTankSpeed() {
        return tankSpeed;
    }

    public int getBulletDamage() {
        return bulletDamage;
    }

    public int getTankMaxHealth() {
        return tankMaxHealth;
    }
}
