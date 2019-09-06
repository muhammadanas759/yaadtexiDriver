package com.taxidriver.app.Model;

public class DriverModel {
    private String req_id;
    private String deviceToken;
    private String fullName;
    private String status;

    public DriverModel() {
    }

    public DriverModel(String req_id, String deviceToken, String fullName, String status) {
        this.req_id = req_id;
        this.deviceToken = deviceToken;
        this.fullName = fullName;
        this.status = status;
    }


    public String getReq_id() {
        return req_id;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public String getFullName() {
        return fullName;
    }

    public String getStatus() {
        return status;
    }
}
