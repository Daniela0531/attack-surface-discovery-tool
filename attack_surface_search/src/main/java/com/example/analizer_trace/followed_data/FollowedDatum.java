package com.example.analizer_trace.followed_data;

public interface FollowedDatum {
//     FollowedDatumLocation getLocation();
//     void setLocation(FollowedDatumLocation location);
//     LocationType getLocationType();
     boolean isEquals(FollowedDatum datum);

    String getName();
}

