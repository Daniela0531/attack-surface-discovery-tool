package org.example.analizer.followed_data;

public interface FollowedDatumLocation {
    LocationType getType();
    void print(int i);

    String getJavaClass();
    String getJavaPackage();
}
