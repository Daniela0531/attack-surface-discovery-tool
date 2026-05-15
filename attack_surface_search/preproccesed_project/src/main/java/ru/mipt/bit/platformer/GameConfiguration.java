package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.mipt.bit.platformer.graphics_objects.HealthBarDrower;
import ru.mipt.bit.platformer.level_properties.GraphicProperties;
import ru.mipt.bit.platformer.level_properties.LogicProperties;
import ru.mipt.bit.platformer.map_louders.MapGenerator;
import ru.mipt.bit.platformer.map_louders.MapLoaderFromFile;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

// здесь должны быть входные параметры игры


@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
public class GameConfiguration {
//    private MapLoaderFromFile fileLoader = new MapLoaderFromFile();
    @Bean
    public LevelMap levelMap(
            @Value("${map_louder_type}") String mapLouderType,
            @Value("${file_map_louder_path}") String absoluteFilePath) {
        if (mapLouderType.equalsIgnoreCase("generate")) {
            MapGenerator mapGenerator = new MapGenerator();
            mapGenerator.loadLevelMap();
            return mapGenerator.getLevelMap();
        }
        if (mapLouderType.equalsIgnoreCase("file")) {
            MapLoaderFromFile mapLoaderFromFile = new MapLoaderFromFile(absoluteFilePath);
            mapLoaderFromFile.loadLevelMap();
            return mapLoaderFromFile.getLevelMap();
        }
        return null;
    }
    @Bean
    public MapLouder mapLouder(@Value("${map_louder_type}") String mapLouderType,
                               @Value("${file_map_louder.path}") String absoluteFilePath) {
        if (mapLouderType.equals("generate")) {
            MapGenerator mapGenerator = new MapGenerator();
            mapGenerator.loadLevelMap();
            return mapGenerator;
        }
        if (mapLouderType.equals("file")) {
            MapLoaderFromFile mapLoaderFromFile = new MapLoaderFromFile(absoluteFilePath);
            mapLoaderFromFile.loadLevelMap();
            return mapLoaderFromFile;
        }
        return null;
    }

    @Bean
    public GraphicProperties graphicProperties(@Value("${tiled_map}") String tiledMap,
                                               @Value("${tank.texture}") String tankTexture,
                                               @Value("${tree.texture}") String treeTexture,
                                               @Value("${bullet.texture}") String bulletTexture,
                                               @Value("${tank.max_health}") int tankMaxHealth,
                                               @Value("${health_bar_width}") int healthBarWidth,
                                               @Value("${health_bar_height}") int healthBarHeight,
                                               @Value("${wasted_health_color}") int wastedHealth,
                                               @Value("${existing_health_color}") int existingHealth) {
        Color wastedHealthColor = new Color(wastedHealth);
        Color existingHealthColor = new Color(existingHealth);
        return new GraphicProperties(tiledMap, tankTexture, treeTexture, bulletTexture, new HealthBarDrower(healthBarWidth, healthBarHeight, wastedHealthColor, existingHealthColor));
    }

    @Bean
    public LogicProperties logicProperties(@Value("${tank.speed}") float tankSpeed,
                                             @Value("${tank.max_health}") int tankMaxHealth,
                                             @Value("${bullet.speed}") float bulletSpeed,
                                             @Value("${bullet.damage}") int bulletDamage) {
        return new LogicProperties(tankMaxHealth, tankSpeed, bulletDamage, bulletSpeed);
    }

    @Bean
    public HealthBarDrower healthBarDecorator(@Value("${health_bar_width}") int healthBarWidth,
                                              @Value("${health_bar_height}") int healthBarHeight,
                                              @Value("${wasted_health_color}") int wastedHealth,
                                              @Value("${existing_health_color}") int existingHealth) {
        Color wastedHealthColor = new Color(wastedHealth);
        Color existingHealthColor = new Color(existingHealth);
//        Color.
        return new HealthBarDrower(healthBarWidth, healthBarHeight, wastedHealthColor, existingHealthColor);
    }

    @Bean
    public Batch batch() {
        return new SpriteBatch();
    }

    @Bean
    public TiledMap tiledMap() {
        return new TiledMap();
    }

    @Bean
    public TiledMapTileLayer tiledMapTileLayer() {
        TiledMap tiledMap = new TmxMapLoader().load("level.tmx");
        return getSingleLayer(tiledMap);
    }

    @Bean
    public TileMovement tileMovement() {
        TiledMap tiledMap = new TmxMapLoader().load("level.tmx");
        TiledMapTileLayer tiledMapTileLayer = getSingleLayer(tiledMap);
        return new TileMovement(tiledMapTileLayer, Interpolation.smooth);
    }
}
