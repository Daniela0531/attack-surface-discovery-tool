package org.example.structure;

import java.nio.file.Path;
import java.util.ArrayList;

public interface StructureEntityInterface {

//    public void addChild(StructureEntityInterface child);

//    public ArrayList<StructureEntityInterface> getChildren();
//    void setPath(Path path);
    void setType(String type);
//    public void setParent(StructureEntityInterface parent);
//    void setName(String name);
    String getName();

    void print();

    String getType();

//    Path getPath();

//    public void setChildren(ArrayList<StructureEntityInterface> children);

//    int getLevel();
//
//    void setLevel(int level);

//    public StructureEntityInterface getParent();
}
