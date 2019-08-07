package com.taxidriver.app.ApiResponse.ForgotPassword;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ForgotResponse implements Serializable {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("provider")
    @Expose
    private Provider provider;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

}
