package org.example.analizer.structures;

public class ProjectStructureGraph {
    // формально это дерево - оставить так?
    private ProjectStructureNode mainParent;

    public ProjectStructureGraph(ProjectStructureNode parent) {
        this.mainParent = parent;
    }
}
