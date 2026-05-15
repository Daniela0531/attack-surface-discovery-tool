package com.example.analizer.followed_data.location;

import com.example.analizer.Method;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.analizer.followed_data.LocationType;

public class EquatingLocation implements FollowedDatumLocation {
    @JsonProperty("package")
    private String javaPackage = "";
    @JsonProperty("class")
    private String javaClass = "";
    @JsonProperty("method")
    private Method method;
    private LocationType type = LocationType.EQUATING;

    public EquatingLocation() {
        this.method = new Method();
    }
    public EquatingLocation(String javaPackage, String javaClass, Method method) {
        this.javaPackage = javaPackage;
        this.javaClass = javaClass;
        this.method = method;
    }
    @Override
    public LocationType getType() {
        return type;
    }

    @Override
    public void print(int i) {
        System.out.println("type :: argument of method");
        System.out.println("package = " + javaPackage);
        System.out.println("class = " + javaClass);
        System.out.println("method = " + method.getName());
    }

    public String getJavaClass() {
        return javaClass;
    }

    @Override
    public String getJavaPackage() {
        return javaPackage;
    }

    @Override
    public boolean isEquels(FollowedDatumLocation followedDatumLocation) {
        if (!(followedDatumLocation instanceof EquatingLocation)) {
            return false;
        }
        EquatingLocation anotherLocation = (EquatingLocation) followedDatumLocation;
        return javaPackage.equals(anotherLocation.javaPackage) &&
                javaClass.equals(anotherLocation.javaClass) &&
                method.getName().equals(anotherLocation.getMethod().getName()) &&
                method.getNumberOfArguments() == anotherLocation.getMethod().getNumberOfArguments();
    }

    public Method getMethod() {
        return method;
    }
}
