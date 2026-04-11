package org.example.structure;

import java.nio.file.Path;
import java.util.ArrayList;

public class StructurePackageEntity {
    private String type;
    private Path path;
    private String name;
    private ArrayList<StructureClassEntity> classes;
    private StructurePackageEntity parent;
    private int level;

    public StructurePackageEntity() {
        this.path = null;
        this.classes = new ArrayList<>();
        this.parent = null;
        this.type = null;
        this.level = 0;
        this.name = null;
    }
    public StructurePackageEntity(String type, Path path, int level) {
        this.path = path;
        this.classes = new ArrayList<>();
        this.parent = null;
        this.type = type;
        this.level = level;
        this.name = null;
    }

    public void addChild(StructureClassEntity child) {
        classes.add(child);
    }

    public ArrayList<StructureClassEntity> getMethods() {
        return classes;
    }
    public void setPath(Path path) {
        this.path = path;
    }
    public void setType(String type) {
        this.type = type;
    }

    public void setParent(StructurePackageEntity parent) {
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

        for (StructureClassEntity child : classes) {
            child.print();
        }
    }

    public String getType() {
        return this.type;
    }

    public Path getPath() {
        return this.path;
    }

    public void setMethods(ArrayList<StructureClassEntity> methods) {
        this.classes = methods;
    }

    public int getLevel() {
        return  level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public StructurePackageEntity getParent() {
        return parent;
    }
}
