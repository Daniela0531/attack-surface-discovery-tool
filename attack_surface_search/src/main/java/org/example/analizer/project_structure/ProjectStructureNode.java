package org.example.analizer.project_structure;

import java.nio.file.Path;
import java.util.ArrayList;

public class ProjectStructureNode {
    private String type;
    private Path path;
    private String name;
    private ArrayList<ProjectStructureNode> children;
    private ProjectStructureNode parent;
    private String[] content;
    private int level;

    private boolean isAlreadyAnalized = false;
    public ProjectStructureNode() {
        this.path = null;
        this.children = new ArrayList<>();
        this.parent = null;
        this.type = null;
        this.level = 0;
        this.name = null;
        this.isAlreadyAnalized = false;
        this.content = null;
    }
    public String[] getContent() {
        return this.content;
    }
    public void addChild(ProjectStructureNode child) {
        children.add(child);
    }

    public ArrayList<ProjectStructureNode> getChildren() {
        return children;
    }
    public void setPath(Path path) {
        this.path = path;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setParent(ProjectStructureNode parent) {
        this.parent = parent;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    public void print(int i) {
        String tabs = "   ".repeat(i);
        System.out.println(tabs + type + "::");
        System.out.println(tabs + name);
//        System.out.println(tabs + path);

        for (ProjectStructureNode child : children) {
            child.print(i + 1);
        }
    }

    public String getType() {
        return this.type;
    }

    public Path getPath() {
        return this.path;
    }

    public void setChildren(ArrayList<ProjectStructureNode> children) {
        this.children = children;
    }

    public int getLevel() {
        return  level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ProjectStructureNode getParent() {
        return parent;
    }
}
