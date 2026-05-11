package org.example.analizer.followed_data.location;

import org.example.analizer.followed_data.LocationType;

public interface FollowedDatumLocation {
    LocationType getType();
    void print(int i);

    String getJavaClass();
    String getJavaPackage();
}
