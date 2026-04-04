package org.example.structure;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

public class StructureEntity {
    private String type;
    private Path path;
    private String name;
    private ArrayList<StructureEntity> children;
    private StructureEntity parent;
    private int level;

    public StructureEntity() {
        this.path = null;
        this.children = new ArrayList<>();
        this.parent = null;
        this.type = null;
        this.level = 0;
        this.name = null;
    }
    public StructureEntity(String type, Path path, int level) {
        this.path = path;
        this.children = new ArrayList<>();
        this.parent = null;
        this.type = type;
        this.level = level;
        this.name = null;
    }

//    public StructureEntity(String type, Path path, int level, StructureEntity parent) {
//        this.path = path;
//        this.children = new ArrayList<>();
//        this.type = type;
//        this.parent = parent;
//        this.level = level;
//        this.name = null;
//    }
//
//    public StructureEntity(String type, Path path, String name, int level) {
//        this.path = path;
//        this.children = new ArrayList<>();
//        this.parent = null;
//        this.type = type;
//        this.level = level;
//        this.name = name;
//    }
//
//    public StructureEntity(String type, Path path, String name, int level, StructureEntity parent) {
//        this.path = path;
//        this.children = new ArrayList<>();
//        this.type = type;
//        this.parent = parent;
//        this.level = level;
//        this.name = name;
//    }

    public void addChild(StructureEntity child) {
        children.add(child);
    }

    public ArrayList<StructureEntity> getChildren() {
        return children;
    }
    public void setPath(Path path) {
        this.path = path;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setParent(StructureEntity parent) {
        this.parent = parent;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    public void print() {
//        System.out.println("\n");
        String tabs = "   ".repeat(this.level);
        System.out.println(tabs + type + "::");
        System.out.println(tabs + name);
        System.out.println(tabs + path);

//        if (!children.isEmpty()) {
//            System.out.println(tabs + "::children::");
//        }
        for (StructureEntity child : children) {
            child.print();
        }
//        for (StructureEntity child : children) {
//            if (Objects.equals(child.getType(), "PACKAGE"))
//                child.print();
//        }
//        for (StructureEntity child : children) {
//            if (Objects.equals(child.getType(), "FILE"))
//                child.print();
//        }
//        for (StructureEntity child : children) {
//            if (Objects.equals(child.getType(), "CLASS"))
//                child.print();
//        }
//        for (StructureEntity child : children) {
//            if (Objects.equals(child.getType(), "METHOD"))
//                child.print();
//        }
//        for (StructureEntity child : children) {
//            if (Objects.equals(child.getType(), "ARGUMENT"))
//                child.print();
//        }
//        System.out.println("\n");
    }

    public String getType() {
        return this.type;
    }

    public Path getPath() {
        return this.path;
    }

    public void setChildren(ArrayList<StructureEntity> children) {
        this.children = children;
    }

    public int getLevel() {
        return  level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public StructureEntity getParent() {
        return parent;
    }
}
