package org.example.analizer.followed_data;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.analizer.Method;

public class MethodArgumentLocation implements FollowedDataLocation {
    @JsonProperty("package")
    private String javaPackage = "";
    @JsonProperty("class")
    private String javaClass = "";
    @JsonProperty("positionInMethod")
    private int positionInMethod;
    @JsonProperty("method")
    private Method method;
    private LocationType type = LocationType.METHOD;

    public MethodArgumentLocation() {
        this.method = new Method();
        this.positionInMethod = -1;
    }
    public MethodArgumentLocation(String javaPackage, String javaClass, Method method, int positionInMethod) {
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
    public void print() {
        System.out.println("type :: argument of method");
        System.out.println("package = " + javaPackage);
        System.out.println("class = " + javaClass);
        System.out.println("method = " + method.getName());
        System.out.println("positionInMethod = " + positionInMethod);
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

    public Method getMethod() {
        return method;
    }
}
