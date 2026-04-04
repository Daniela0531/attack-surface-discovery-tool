package org.example.structure.archive;

import java.util.ArrayList;

public class ClassStructure {
    String name;
//    String returnValue;
    ArrayList<MethodStructure> methods;
    ArrayList<String> fields;
    PackageStructure parentPackageStructure;
    public ClassStructure() {
        this.name = null;
        this.methods = new ArrayList<>();
        this.fields = new ArrayList<>();
        this.parentPackageStructure = new PackageStructure();
    }
    public ClassStructure(String name) {
        this.name = name;
        this.methods = new ArrayList<>();
        this.fields = new ArrayList<>();
        this.parentPackageStructure = new PackageStructure();
    }
    public void addFields(String field) {
        this.fields.add(field);
    }
    public void addMethods(MethodStructure method) {
        this.methods.add(method);
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setParentPackageStructure(PackageStructure packageStructure) {
        this.parentPackageStructure = packageStructure;
    }
    public PackageStructure getParentPackageStructure() {
        return parentPackageStructure;
    }
    public void print() {
        System.out.println(name);
    }
//    public static String name(String str) throws IOException {
//        System.out.println("dfg");
//        return null;
//    }
//    private String name(Integer str) {
//        System.out.println("dfg");
//        return null;
//    }
}
