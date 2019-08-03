package com.taxidriver.app.ApiResponse.Summary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SummaryResponse {
    @SerializedName("completed_rides")
    @Expose
    private Integer completedRides;
    @SerializedName("revenue")
    @Expose
    private Float revenue;
    @SerializedName("cancel_rides")
    @Expose
    private Integer cancelRides;
    @SerializedName("scheduled_rides")
    @Expose
    private Integer scheduledRides;

    public Integer getCompletedRides() {
        return completedRides;
    }

    public void setCompletedRides(Integer completedRides) {
        this.completedRides = completedRides;
    }

    public Float getRevenue() {
        return revenue;
    }

    public void setRevenue(Float revenue) {
        this.revenue = revenue;
    }

    public Integer getCancelRides() {
        return cancelRides;
    }

    public void setCancelRides(Integer cancelRides) {
        this.cancelRides = cancelRides;
    }

    public Integer getScheduledRides() {
        return scheduledRides;
    }

    public void setScheduledRides(Integer scheduledRides) {
        this.scheduledRides = scheduledRides;
    }

}