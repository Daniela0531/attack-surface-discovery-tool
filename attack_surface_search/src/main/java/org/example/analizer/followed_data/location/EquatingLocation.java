package org.example.analizer.followed_data.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.analizer.Method;
import org.example.analizer.followed_data.LocationType;
import org.example.analizer.followed_data.location.FollowedDatumLocation;

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

    public Method getMethod() {
        return method;
    }
}
