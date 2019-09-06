package com.taxidriver.app.Model;

import java.io.Serializable;

public class Riderequest implements Serializable
{

    private String name,locationName,destinationName,requestId;
    private String userlocationlat,userlocationlong,destlat,destlng;
    private boolean status,isArrived;

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public boolean isArrived() {
        return isArrived;
    }




    public void setArrived(boolean arrived) {
        isArrived = arrived;
    }


    public String getUserlocationlat() {
        return userlocationlat;
    }

    public void setUserlocationlat(String userlocationlat) {
        this.userlocationlat = userlocationlat;
    }

    public String getUserlocationlong() {
        return userlocationlong;
    }

    public void setUserlocationlong(String userlocationlong) {
        this.userlocationlong = userlocationlong;
    }

    public String getDestlat() {
        return destlat;
    }

    public void setDestlat(String destlat) {
        this.destlat = destlat;
    }

    public String getDestlng() {
        return destlng;
    }

    public void setDestlng(String destlng) {
        this.destlng = destlng;
    }

    public Riderequest(String name, String locationName, String destinationName, String requestId, String userlocationlat, String userlocationlong, String destlat, String destlng) {
        this.name = name;
        this.locationName = locationName;
        this.destinationName = destinationName;
        this.requestId = requestId;
        this.userlocationlat = userlocationlat;
        this.userlocationlong = userlocationlong;
        this.destlat = destlat;
        this.destlng = destlng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
