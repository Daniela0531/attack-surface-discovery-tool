package ru.mipt.bit.platformer;

import ru.mipt.bit.platformer.level_map.MapNode;

import java.util.ArrayList;

public class LevelMap {
    private MapNode player;
    private ArrayList<MapNode> nodesCoordinates;
    private final int leftBound;
    private final int rightBound;
    private final int lowBound;
    private final int upBound;

    public LevelMap(ArrayList<MapNode> nodesCoordinates, MapNode player, int leftBound, int rightBound, int lowBound, int upBound) {
        this.nodesCoordinates = nodesCoordinates;
        this.player = player;
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.lowBound = lowBound;
        this.upBound = upBound;
    }

//    public LevelMap(MapLouder mapLoaderFromFile, int leftBound, int rightBound, int lowBound, int upBound) {
//        this.nodesCoordinates = mapLoaderFromFile.getLevelMap().nodesCoordinates;
//        this.player = mapLoaderFromFile.getLevelMap().getPlayer();
//        this.leftBound = leftBound;
//        this.rightBound = rightBound;
//        this.lowBound = lowBound;
//        this.upBound = upBound;
//    }

    public ArrayList<MapNode> getNodes() {
        return nodesCoordinates;
    }
    public void print() {
        for (MapNode mapNode : nodesCoordinates) {
            System.out.println(mapNode.getNodeType() + ": " + mapNode.getCoordinates());
        }
    }

    public MapNode getPlayer() {
        return player;
    }

    public int getLeftBound() {
        return leftBound;
    }

    public int getLowBound() {
        return lowBound;
    }

    public int getUpBound() {
        return upBound;
    }

    public int getRightBound() {
        return rightBound;
    }
}
