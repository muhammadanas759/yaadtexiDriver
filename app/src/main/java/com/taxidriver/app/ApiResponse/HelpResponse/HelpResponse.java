package com.taxidriver.app.ApiResponse.HelpResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HelpResponse {

    @SerializedName("contact_number")
    @Expose
    private String contactNumber;
    @SerializedName("contact_email")
    @Expose
    private String contactEmail;

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

}
