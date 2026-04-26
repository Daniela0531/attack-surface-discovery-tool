package org.example.analizer.followed_data;

public interface FollowedDataLocation {
    LocationType getType();
    void print();

    String getJavaClass();
    String getJavaPackage();
}
