package org.example.structure;

import java.nio.file.Path;
import java.util.ArrayList;

public class StructureMethodEntity {
    private String type;
    private Path path;
    private String name;
    private int argumentCount = 0;
    private String[] content;
    private ArrayList<StructureMethodEntity> children;
    private StructureMethodEntity parent;
    private int level;

    public StructureMethodEntity() {
        this.path = null;
        this.children = new ArrayList<>();
        this.parent = null;
        this.type = null;
        this.level = 0;
        this.name = null;
        this.argumentCount = 0;
        this.content = null;
    }
    public StructureMethodEntity(String type, Path path, int level) {
        this.path = path;
        this.children = new ArrayList<>();
        this.parent = null;
        this.type = type;
        this.level = level;
        this.name = null;
        this.argumentCount = 0;
        this.content = null;
    }
    public void setContent(String[] content) {
        this.content = content;
    }
    public String[] getContent() {
        return this.content;
    }

    public int getArgumentCount() {
        return argumentCount;
    }

    public void setArgumentCount(int count) {
        this.argumentCount = count;
    }

    public void addChild(StructureMethodEntity child) {
        children.add(child);
    }

    public ArrayList<StructureMethodEntity> getChildren() {
        return children;
    }
    public void setPath(Path path) {
        this.path = path;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setParent(StructureMethodEntity parent) {
        this.parent = parent;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    public void print() {
        String tabs = "   ".repeat(this.level);
        System.out.println(tabs + type + "::");
        System.out.println(tabs + name);
        System.out.println(tabs + path);

        for (StructureMethodEntity child : children) {
            child.print();
        }
    }

    public String getType() {
        return this.type;
    }

    public Path getPath() {
        return this.path;
    }

    public void setChildren(ArrayList<StructureMethodEntity> children) {
        this.children = children;
    }

    public int getLevel() {
        return  level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public StructureMethodEntity getParent() {
        return parent;
    }
}
