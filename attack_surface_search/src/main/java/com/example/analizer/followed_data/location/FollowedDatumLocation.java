package com.example.analizer.followed_data.location;

import com.example.analizer.followed_data.LocationType;

public interface FollowedDatumLocation {
    LocationType getType();
    void print(int i);

    String getJavaClass();
    String getJavaPackage();

    boolean isEquels(FollowedDatumLocation followedDatumLocation);
}
