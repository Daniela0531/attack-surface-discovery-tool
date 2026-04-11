package org.example.structure;

import java.nio.file.Path;
import java.util.ArrayList;

public class StructureClassEntity implements StructureEntityInterface {
    private String type;
    private Path path;
    private String name;
    private ArrayList<StructureMethodEntity> methods;
    private StructurePackageEntity parent;
    private int level;

    public StructureClassEntity() {
        this.path = null;
        this.methods = new ArrayList<>();
        this.parent = null;
        this.type = null;
        this.level = 0;
        this.name = null;
    }
    public StructureClassEntity(String type, Path path, int level) {
        this.path = path;
        this.methods = new ArrayList<>();
        this.parent = null;
        this.type = type;
        this.level = level;
        this.name = null;
    }

    public void addChild(StructureMethodEntity child) {
        methods.add(child);
    }

    public ArrayList<StructureMethodEntity> getMethods() {
        return methods;
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

        for (StructureMethodEntity child : methods) {
            child.print();
        }
    }

    public String getType() {
        return this.type;
    }

    public Path getPath() {
        return this.path;
    }

    public void setMethods(ArrayList<StructureMethodEntity> methods) {
        this.methods = methods;
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
