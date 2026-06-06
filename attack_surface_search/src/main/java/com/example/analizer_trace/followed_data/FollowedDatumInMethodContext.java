package com.example.analizer_trace.followed_data;

import spoon.reflect.declaration.CtExecutable;

public interface FollowedDatumInMethodContext extends FollowedDatum {
     CtExecutable<?> getLocation();
     void setLocation(CtExecutable<?> location);
//     LocationType getLocationType();
     boolean isEquals(FollowedDatum datum);

    String getName();
    FollowedDatumInMethodContext get();
}

