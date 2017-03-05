package com.cynthiar.dancingday.distance.matrix;

/**
 * Created by CynthiaR on 3/4/2017.
 */

public class DistanceQuery {
    private String[] mOrigins;
    private String[] mDestinations;
    private String mMode = "driving"; // Only support driving
    private String mLanguage = "en";
    private String mUnit = "metrics";
    private String mDepartureTime = "now";
    private String mTrafficModel = "best_guess";

    public DistanceQuery(String origin, String destination){
        mOrigins = new String[] { origin };
        mDestinations = new String[] { destination };
    }

    public String[] getOrigins() {
        return mOrigins;
    }

    public String[] getDestinations() {
        return mDestinations;
    }

    public String getMode() {
        return mMode;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public String getUnit() {
        return mUnit;
    }

    public String getDepartureTime() {
        return mDepartureTime;
    }

    public String getTrafficModel() {
        return mTrafficModel;
    }
}
