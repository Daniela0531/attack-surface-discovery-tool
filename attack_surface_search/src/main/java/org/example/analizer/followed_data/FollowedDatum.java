package org.example.analizer.followed_data;

import org.example.analizer.followed_data.location.FollowedDatumLocation;

public class FollowedDatum {
    private FollowedDatumLocation location;
    private boolean neededToAnalyze;

    public FollowedDatum() {
    }
    public FollowedDatum(FollowedDatumLocation location) {
        this.location = location;
        location.print(0);
    }

    public String getJavaPackage() {
        return location.getJavaPackage();
    }
    public String getJavaClass() {
        return location.getJavaClass();
    }
    public FollowedDatumLocation getLocation() {
        return location;
    }
    public void setLocation(FollowedDatumLocation location) {
        this.location = location;
    }
    public LocationType getLocationType() {
        return location.getType();
    }
//    public void print(int i) {
//        System.out.println("datum location :::");
//        location.print(i);
//    }

    public void setNeededToAnalyze(boolean flag) {
        neededToAnalyze = flag;
    }
    public boolean getNeededToAnalyze() {
        return neededToAnalyze;
    }
}

