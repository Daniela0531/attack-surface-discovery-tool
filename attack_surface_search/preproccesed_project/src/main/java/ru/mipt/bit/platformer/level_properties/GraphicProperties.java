package ru.mipt.bit.platformer.level_properties;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.graphics_objects.HealthBarDrower;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class GraphicProperties {

    private String tankTextureImg;
    private String treeTextureImg;
    private String tiledMapImg;
    private String bulletImg;
    private TiledMapTileLayer tiledMapTileLayer;
    private HealthBarDrower healthBarDecorator;

    public GraphicProperties(String tiledMapImgPath,
                             String tankTextureImgPath,
                             String treeTextureImgPath,
                             String bulletImgPath,
                             HealthBarDrower healthBarDecorator) {
        this.tankTextureImg = tankTextureImgPath;
        this.treeTextureImg = treeTextureImgPath;
        this.bulletImg = bulletImgPath;
        this.tiledMapImg = tiledMapImgPath;
        TiledMap tiledMap = new TmxMapLoader().load(tiledMapImgPath);
        this.tiledMapTileLayer = getSingleLayer(new TmxMapLoader().load(tiledMapImgPath));
        this.tiledMapTileLayer = getSingleLayer(tiledMap);
        this.healthBarDecorator = healthBarDecorator;
    }
    public Texture getBulletTexture() {
        return new Texture(bulletImg);
    }

    public Texture getTankTexture() {
        return new Texture(tankTextureImg);
    }

    public TiledMap getTiledMap() {
        return new TmxMapLoader().load(tiledMapImg);
    }

    public Texture getTreeTexture() {
        return new Texture(treeTextureImg);
    }

    public TiledMapTileLayer getTiledMapTileLayer() {
        return tiledMapTileLayer;
    }

    public TextureRegion getTankTextureRegion() {
        return new TextureRegion(getTankTexture());
    }

    public Rectangle getTankRectangle() {
        return createBoundingRectangle(getTankTextureRegion());
    }

    public Rectangle getTreeRectangle() {
        return createBoundingRectangle(getTreeTextureRegion());
    }

    public TextureRegion getTreeTextureRegion() {
        return new TextureRegion(getTreeTexture());
    }

    public HealthBarDrower getHealthBarDecorator() {
        return healthBarDecorator;
    }
}

