package org.example.structure;

import java.nio.file.Path;
import java.util.ArrayList;

public interface StructureEntityInterface {

//    public void addChild(StructureEntityInterface child);

//    public ArrayList<StructureEntityInterface> getChildren();
    public void setPath(Path path);
    public void setType(String type);
//    public void setParent(StructureEntityInterface parent);
    public void setName(String name);
    public String getName();

    public void print();

    public String getType();

    public Path getPath();

//    public void setChildren(ArrayList<StructureEntityInterface> children);

    public int getLevel();

    public void setLevel(int level);

//    public StructureEntityInterface getParent();
}
