package com.example.analizer.followed_data.location;

import com.example.analizer.Method;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.analizer.followed_data.LocationType;
import spoon.reflect.declaration.CtMethod;

public class MethodLocation implements FollowedDatumLocation {
    @JsonProperty("package")
    private String javaPackage = "";
    @JsonProperty("class")
    private String javaClass = "";
    @JsonProperty("positionInMethod")
    private int positionInMethod;
    @JsonProperty("method")
    private Method method;
    private LocationType type = LocationType.METHOD;
    private CtMethod<?> ctMethod;

    public MethodLocation() {
        this.method = new Method();
        this.positionInMethod = -1;
    }
    public MethodLocation(String javaPackage, String javaClass, Method method, int positionInMethod) {
        this.javaPackage = javaPackage;
        this.javaClass = javaClass;
        this.method = method;
        this.positionInMethod = positionInMethod;
    }
    @Override
    public LocationType getType() {
        return type;
    }

    @Override
    public void print(int i) {
        String tabs = "   ".repeat(i);
//        System.out.println(tabs + i + ":::\n" + tabs + "type :: argument of method");
//        System.out.println(tabs + "package = " + javaPackage);
//        System.out.println(tabs + "class = " + javaClass);
//        System.out.println(tabs + "method = " + method.getName());
//        System.out.println(tabs + "positionInMethod = " + positionInMethod);
        System.out.println(tabs + i + ":::\n" + tabs + "type :: argument of method");
        System.out.println(tabs + "package = " + javaPackage);
        System.out.println(tabs + "class = " + javaClass);
        System.out.println(tabs + "method = " + method.getName());
        System.out.println(tabs + "positionInMethod = " + positionInMethod);
    }

    public int getPositionInMethod() {
        return positionInMethod;
    }
//    public void setPositionInMethod(int p) {
//        this.positionInMethod = p;
//    }

    public String getJavaClass() {
        return javaClass;
    }

    @Override
    public String getJavaPackage() {
        return javaPackage;
    }

    @Override
    public boolean isEquels(FollowedDatumLocation followedDatumLocation) {
        if (!(followedDatumLocation instanceof MethodLocation)) {
            return false;
        }
        MethodLocation anotherLocation = (MethodLocation) followedDatumLocation;
        return javaPackage.equals(anotherLocation.javaPackage) &&
                javaClass.equals(anotherLocation.javaClass) &&
                positionInMethod == anotherLocation.getPositionInMethod() &&
                method.getName().equals(anotherLocation.getMethod().getName()) &&
                method.getNumberOfArguments() == anotherLocation.getMethod().getNumberOfArguments();
    }

    public Method getMethod() {
        return method;
    }

    public CtMethod<?> getCtMethod() {
        return ctMethod;
    }
}
