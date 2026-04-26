package ru.mipt.bit.platformer.map_louders;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.LevelMap;
import ru.mipt.bit.platformer.MapLouder;
import ru.mipt.bit.platformer.level_map.MapNode;
import ru.mipt.bit.platformer.level_map.NodeType;

import java.util.ArrayList;
import java.util.Random;

//@Component
public class MapGenerator implements MapLouder {
    private LevelMap levelMap;

    @Override
    public void loadLevelMap() {
        ArrayList<MapNode> obstacles = new ArrayList<>();

        // идеологически границы должны загружаться/создаваться загрузчиком карты (из файла с картой например)
        // карта плиток и границы должны уровня должны быть связаны (карта плиток генериться в соответствии с размерами?)
        // но не понятно как управлять level.tmx (TiledMap)
        // хард код
        int leftBound = 0;
        int rightBound = 9;
        int upBound = 5;
        int lowBound = 0;

        Random random = new Random();
        MapNode player = null;
        boolean playerIsSet = false;
        for (int i = 0; i < rightBound; ++i) {
            for (int j = 0; j < upBound; ++j) {
                int randomNumber = random.nextInt(rightBound * upBound);
                if (randomNumber%4 == random.nextInt(4)) {
                    // клетка пуста
                    continue;
                } else if (randomNumber%4 == random.nextInt(4)) {
                    obstacles.add(new MapNode(new GridPoint2(i, j), NodeType.TREE));
                } else if (randomNumber%4 == random.nextInt(4)) {
                    obstacles.add(new MapNode(new GridPoint2(i, j), NodeType.TANK));
                } else if (randomNumber%4 == random.nextInt(4) && !playerIsSet) {
                    playerIsSet = true;
                    player = new MapNode(new GridPoint2(i, j), NodeType.TANK);
                }
            }
        }

        boolean placeIsFree = true;
        if (!playerIsSet) {
            for (int i = 0; i < rightBound; ++i) {
                placeIsFree = true;
                for (int j = 0; j < upBound; ++j) {
                    for (MapNode mapNode : obstacles) {
                        if (mapNode.getCoordinates().equals(new GridPoint2(i, j))) {
                            placeIsFree = false;
                            break;
                        }
                    }
                    if (placeIsFree) {
                        player = new MapNode(new GridPoint2(i, j), NodeType.TANK);
                        playerIsSet = true;
                        break;
                    }
                }
                if (playerIsSet) {
                    break;
                }
            }
        }

        this.levelMap = new LevelMap(obstacles, player, leftBound, rightBound, lowBound, upBound);
    }
    @Override
    public LevelMap getLevelMap() {
        return levelMap;
    }

}
