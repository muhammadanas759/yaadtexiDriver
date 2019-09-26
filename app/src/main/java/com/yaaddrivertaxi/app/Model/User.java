package com.yaaddrivertaxi.app.Model;

import java.io.Serializable;

public class User implements Serializable {
    String mEmail,mFirstName,mLastName,mPassword,mPhoneNumber,mDeviceId,mDeviceToken,mUserAvatart,mAccessToken;

    public User() {
    }

    public User(String mEmail, String mFirstName, String mLastName, String mPassword, String mPhoneNumber, String mDeviceId, String mDeviceToken, String mUserAvatart, String mAccessToken) {
        this.mEmail = mEmail;
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mPassword = mPassword;
        this.mPhoneNumber = mPhoneNumber;
        this.mDeviceId = mDeviceId;
        this.mDeviceToken = mDeviceToken;
        this.mUserAvatart = mUserAvatart;
        this.mAccessToken = mAccessToken;
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public void setmAccessToken(String mAccessToken) {
        this.mAccessToken = mAccessToken;
    }

    public String getmUserAvatart() {
        return mUserAvatart;
    }

    public void setmUserAvatart(String mUserAvatart) {
        this.mUserAvatart = mUserAvatart;
    }

    public String getmDeviceId() {
        return mDeviceId;
    }

    public void setmDeviceId(String mDeviceId) {
        this.mDeviceId = mDeviceId;
    }

    public String getmDeviceToken() {
        return mDeviceToken;
    }

    public void setmDeviceToken(String mDeviceToken) {
        this.mDeviceToken = mDeviceToken;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getmLastName() {
        return mLastName;
    }

    public void setmLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}
