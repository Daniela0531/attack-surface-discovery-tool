package org.example.structure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.structure.archive.ClassStructure;
import org.example.structure.archive.PackageStructure;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

public class ProjectStructure {
//    private ArrayList<PackageStructure> packages;
//    private ArrayList<ClassStructure> classes;
//
//
//    private ArrayList<StructureEntity> entities;
    private StructureEntity mainParent;

//    public ProjectStructure() {
//        this.entities = new ArrayList<>();
//    }
//
//    public void setEntities(ArrayList<StructureEntity> entities) {
//        this.entities = entities;
//    }

    public void setMainParent(StructureEntity parent) {
        this.mainParent = parent;
    }

    public void print() {
        System.out.println("\n::Project structure::\n");
        mainParent.print();
        System.out.println("\n");
    }

}
