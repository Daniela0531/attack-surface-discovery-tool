//package com.example.analizer.project_structure;
//
//import java.util.ArrayDeque;
//import java.util.Queue;
//
//public class ProjectStructureGraph {
//    // формально это дерево - оставить так?
//    private ProjectStructureNode mainParent;
//
//    public ProjectStructureGraph() {
//        this.mainParent = null;
//    }
//    public ProjectStructureGraph(ProjectStructureNode parent) {
//        this.mainParent = parent;
//    }
//
//    public ProjectStructureNode getStructure() {
//        return mainParent;
//    }
//    public ProjectStructureNode getMethod(String packageName, String className, String methodName) {
//        Queue<ProjectStructureNode> queue = new ArrayDeque<>();
////        ArrayList<ProjectStructureNode> isVisited = new ArrayList<>();
//        queue.add(mainParent);
//        boolean packageIsFound = false;
//        boolean classIsFound = false;
//        boolean methodIsFound = false;
//        ProjectStructureNode result = null;
//
//
//        while (!queue.isEmpty() && !packageIsFound) {
//            // извлекает и удаляет
//            ProjectStructureNode curNode = queue.poll();
//
//            if (curNode.getName().equals(packageName)) {
//                result = curNode;
//                packageIsFound = true;
//            } else {
//                queue.addAll(curNode.getChildren());
//            }
//        }
//
//        if (!packageIsFound) {
//            System.out.println("Нет такого пакета ::: " + packageName);
//            return null;
//        } else  {
//            System.out.println("Найден пакет ::: " + packageName);
//        }
//
//        queue = new ArrayDeque<>();
//        queue.add(result);
//        while (!queue.isEmpty() && !classIsFound) {
//            // извлекает и удаляет
//            ProjectStructureNode curNode = queue.poll();
//
//            if (curNode.getName().equals(className)) {
//                result = curNode;
//                classIsFound = true;
//            } else {
//                queue.addAll(curNode.getChildren());
//            }
//        }
//
//        if (!classIsFound) {
//            System.out.println("Нет такого класса ::: " + className);
//            return null;
//        } else {
//            System.out.println("Найден класс ::: " + className);
//        }
//
//        queue = new ArrayDeque<>();
//        queue.add(result);
//        while (!queue.isEmpty() && !methodIsFound) {
//            // извлекает и удаляет
//            ProjectStructureNode curNode = queue.poll();
//
//            if (curNode.getName().equals(methodName)) {
//                result = curNode;
//                methodIsFound = true;
//            } else {
//                queue.addAll(curNode.getChildren());
//            }
//        }
//
////        for (ProjectStructureNode child : mainParent.getChildren()) {
////            if (Objects.equals(child.getName(), packageName)) {
////                for (ProjectStructureNode packageChild : child.getChildren()) {
////                    if (Objects.equals(packageChild.getName(), className)) {
////                        for (ProjectStructureNode classChild : packageChild.getChildren()) {
////                            if (Objects.equals(classChild.getName(), methodName)) {
////                                return classChild;
////                            }
////                        }
////                    }
////                }
////            }
////        }
//        if (!methodIsFound) {
//            System.out.println("Нет такого метода ::: " + methodName);
//            return null;
//        } else {
//            System.out.println("Найден метод ::: " + methodName);
//        }
////        result.print(0);
////        System.out.println("------------------------------------");
//        return result;
//    }
//}
