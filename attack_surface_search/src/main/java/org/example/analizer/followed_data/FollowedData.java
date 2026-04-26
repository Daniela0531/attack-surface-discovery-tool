package org.example.analizer.followed_data;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.analizer.Method;

import java.util.ArrayList;

public class FollowedData {
    private FollowedDataLocation location;

    public FollowedData() {
    }

    public String getJavaPackage() {
        return location.getJavaPackage();
    }
    public String getJavaClass() {
        return location.getJavaClass();
    }
    public FollowedDataLocation getLocation() {
        return location;
    }
    public void setLocation(FollowedDataLocation location) {
        this.location = location;
    }
    public LocationType getLocationType() {
        return location.getType();
    }
    public void print() {
        System.out.println("data location :::");
        location.print();
    }
}

