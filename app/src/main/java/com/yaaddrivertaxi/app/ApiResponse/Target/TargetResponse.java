package com.yaaddrivertaxi.app.ApiResponse.Target;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TargetResponse {
    @SerializedName("rides")
    @Expose
    private List<Ride> rides = null;
    @SerializedName("rides_count")
    @Expose
    private Integer ridesCount;
    @SerializedName("target")
    @Expose
    private String target;

    public List<Ride> getRides() {
        return rides;
    }

    public void setRides(List<Ride> rides) {
        this.rides = rides;
    }

    public Integer getRidesCount() {
        return ridesCount;
    }

    public void setRidesCount(Integer ridesCount) {
        this.ridesCount = ridesCount;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

}

