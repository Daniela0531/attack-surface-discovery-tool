package org.example.structure.archive;

import java.util.ArrayList;

public class PackageStructure {
    private String name;
    private ArrayList<ClassStructure> classes;
    // у самого родительского пакета родитель = null
    private PackageStructure parentPackage;
    private ArrayList<PackageStructure> childPackages;

    public PackageStructure() {
        this.name = null;
        this.classes = new ArrayList<>();
        this.childPackages = new ArrayList<>();
        this.parentPackage = null;
    }
    public PackageStructure(String name) {
        this.name = name;
        this.classes = new ArrayList<>();
        this.childPackages = new ArrayList<>();
        this.parentPackage = null;
    }

    public PackageStructure(String name, PackageStructure parentPackage) {
        this.name = name;
        this.classes = new ArrayList<>();
        this.childPackages = new ArrayList<>();
        this.parentPackage = parentPackage;
    }

    public void addChildPackage(PackageStructure packageStructure) {
        childPackages.add(packageStructure);
    }
    public void addClass(ClassStructure classStructure) {
        classes.add(classStructure);
    }

    public ArrayList<ClassStructure> getClasses() {
        return classes;
    }
    public ArrayList<PackageStructure> getChildPackages() {
        return childPackages;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setParentPackage(PackageStructure packageStructure) {
        this.parentPackage = packageStructure;
    }

    public void print() {
        System.out.println("\n");
        System.out.println(name);
        for (ClassStructure classStructure : classes) {
            classStructure.print();
        }
        for (PackageStructure packageStructure : childPackages) {
            packageStructure.print();
        }
        System.out.println("\n");
    }
}
