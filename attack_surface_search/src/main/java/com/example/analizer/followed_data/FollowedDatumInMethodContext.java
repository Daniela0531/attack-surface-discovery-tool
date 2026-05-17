package com.example.analizer.followed_data;

import com.example.analizer.followed_data.location.FollowedDatumLocation;
import spoon.reflect.declaration.CtExecutable;

public interface FollowedDatumInMethodContext extends FollowedDatum {
     CtExecutable<?> getLocation();
     void setLocation(CtExecutable<?> location);
//     LocationType getLocationType();
     boolean isEquals(FollowedDatum datum);

    String getName();
}

