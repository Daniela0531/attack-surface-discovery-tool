package ru.mipt.bit.platformer.graphics_objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class HealthBarDrower {
//    private final int maxHealth;
    private final int height;
    private final int width;
    private TextureRegion healthBarTextureRegion;
    private Texture texture;
    private Color existingHealth;
    private Color wastedHealth;
    private Rectangle rectangle;
//    private int shiftFromModel = 0;

    public HealthBarDrower(int width, int height, Color existingHealth, Color wastedHealth) {
        this.width = width;
        this.height = height;
        this.existingHealth = existingHealth;
        this.wastedHealth = wastedHealth;
    }

    public void drawHealthBar(Batch batch, TextureRegion livableModelRectangle, int maxHealth, int health) {
        this.healthBarTextureRegion = getHealthBarTexture(maxHealth, health);
        this.rectangle = createBoundingRectangle(livableModelRectangle);

        drawTextureRegionUnscaled(batch, healthBarTextureRegion, rectangle, 0f);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    private TextureRegion getHealthBarTexture(int maxHealth, int health) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
//        pixmap.setColor(Color.RED);
//        pixmap.fillRectangle(0, 0, maxHealth, height);
//        pixmap.setColor(Color.GREEN);
//        pixmap.fillRectangle(0, 0, health, height);
        int oneHealth = width / maxHealth;
        pixmap.setColor(wastedHealth);
        pixmap.fillRectangle(0, 0, width, height);
        pixmap.setColor(existingHealth);
        pixmap.fillRectangle(0, 0, health * oneHealth, height);

        this.texture = new Texture(pixmap);
        pixmap.dispose();

        return new TextureRegion(texture);
    }

    public void dispose() {
        texture.dispose();
    }
}
