package ru.mipt.bit.platformer.level_map;

import com.badlogic.gdx.math.GridPoint2;

public class MapNode {
    private GridPoint2 coordinates;
    private NodeType nodeType;

    public MapNode(GridPoint2 coordinates, NodeType nodeType) {
        this.coordinates = coordinates;
        this.nodeType = nodeType;
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setCoordinates(GridPoint2 coordinates) {
        this.coordinates = coordinates;
    }
}
