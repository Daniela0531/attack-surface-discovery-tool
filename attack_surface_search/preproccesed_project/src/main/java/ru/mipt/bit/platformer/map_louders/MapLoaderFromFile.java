package ru.mipt.bit.platformer.map_louders;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.LevelMap;
import ru.mipt.bit.platformer.MapLouder;
import ru.mipt.bit.platformer.level_map.MapNode;
import ru.mipt.bit.platformer.level_map.NodeType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

//@Component
public class MapLoaderFromFile implements MapLouder {
    private String filePath;
    private LevelMap levelMap;
    public MapLoaderFromFile(String absoluteFilePath) {
        this.filePath = absoluteFilePath;
    }
    @Override
    public void loadLevelMap() {
        ArrayList<MapNode> obstaclesCoordinates = new ArrayList<>();
        int i = 0;
        int j = 0;
        int leftBound = 0;
        int rightBound = 0;
        int upBound = 0;
        int lowBound = 0;
        MapNode player = new MapNode(new GridPoint2(0, 0), NodeType.TANK);
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (scanner.hasNextLine()) {
            for (Character symbol : scanner.nextLine().toCharArray()) {
                if (symbol == 'X') {
                    player.setCoordinates(new GridPoint2(i, j));
                }
                if (symbol == 'T') {
                    obstaclesCoordinates.add(new MapNode(new GridPoint2(i, j), NodeType.TREE));
                }
                ++i;
            }
            rightBound = i - 1;
            ++j;
            i = 0;
        }
        upBound = j - 1;

        for (MapNode tree : obstaclesCoordinates) {
            tree.setCoordinates(new GridPoint2(tree.getCoordinates().x, upBound - tree.getCoordinates().y));
        }
        player.setCoordinates(new GridPoint2(player.getCoordinates().x, upBound - player.getCoordinates().y));
        scanner.close();
        this.levelMap = new LevelMap(obstaclesCoordinates, player, leftBound, rightBound, lowBound, upBound);
    }
    @Override
    public LevelMap getLevelMap() {
        return levelMap;
    }
}
