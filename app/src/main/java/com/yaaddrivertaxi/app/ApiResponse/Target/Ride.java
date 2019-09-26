package com.yaaddrivertaxi.app.ApiResponse.Target;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yaaddrivertaxi.app.ApiResponse.UpdateProfile.ServiceType;

public class Ride {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("booking_id")
    @Expose
    private String bookingId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("provider_id")
    @Expose
    private Integer providerId;
    @SerializedName("current_provider_id")
    @Expose
    private Integer currentProviderId;
    @SerializedName("service_type_id")
    @Expose
    private Integer serviceTypeId;
    @SerializedName("no_of_bags")
    @Expose
    private String noOfBags;
    @SerializedName("passengers")
    @Expose
    private String passengers;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("cancelled_by")
    @Expose
    private String cancelledBy;
    @SerializedName("cancel_reason")
    @Expose
    private Object cancelReason;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("paid")
    @Expose
    private Integer paid;
    @SerializedName("distance")
    @Expose
    private Integer distance;
    @SerializedName("s_address")
    @Expose
    private String sAddress;
    @SerializedName("s_latitude")
    @Expose
    private Float sLatitude;
    @SerializedName("s_longitude")
    @Expose
    private Float sLongitude;
    @SerializedName("d_address")
    @Expose
    private String dAddress;
    @SerializedName("d_latitude")
    @Expose
    private Float dLatitude;
    @SerializedName("d_longitude")
    @Expose
    private Float dLongitude;
    @SerializedName("assigned_at")
    @Expose
    private String assignedAt;
    @SerializedName("schedule_at")
    @Expose
    private String scheduleAt;
    @SerializedName("started_at")
    @Expose
    private Object startedAt;
    @SerializedName("finished_at")
    @Expose
    private Object finishedAt;
    @SerializedName("user_rated")
    @Expose
    private Integer userRated;
    @SerializedName("provider_rated")
    @Expose
    private Integer providerRated;
    @SerializedName("use_wallet")
    @Expose
    private Integer useWallet;
    @SerializedName("surge")
    @Expose
    private Integer surge;
    @SerializedName("route_key")
    @Expose
    private String routeKey;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("payment")
    @Expose
    private Object payment;
    @SerializedName("service_type")
    @Expose
    private ServiceType serviceType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public Integer getCurrentProviderId() {
        return currentProviderId;
    }

    public void setCurrentProviderId(Integer currentProviderId) {
        this.currentProviderId = currentProviderId;
    }

    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getNoOfBags() {
        return noOfBags;
    }

    public void setNoOfBags(String noOfBags) {
        this.noOfBags = noOfBags;
    }

    public String getPassengers() {
        return passengers;
    }

    public void setPassengers(String passengers) {
        this.passengers = passengers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public Object getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(Object cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Integer getPaid() {
        return paid;
    }

    public void setPaid(Integer paid) {
        this.paid = paid;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public String getSAddress() {
        return sAddress;
    }

    public void setSAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    public Float getSLatitude() {
        return sLatitude;
    }

    public void setSLatitude(Float sLatitude) {
        this.sLatitude = sLatitude;
    }

    public Float getSLongitude() {
        return sLongitude;
    }

    public void setSLongitude(Float sLongitude) {
        this.sLongitude = sLongitude;
    }

    public String getDAddress() {
        return dAddress;
    }

    public void setDAddress(String dAddress) {
        this.dAddress = dAddress;
    }

    public Float getDLatitude() {
        return dLatitude;
    }

    public void setDLatitude(Float dLatitude) {
        this.dLatitude = dLatitude;
    }

    public Float getDLongitude() {
        return dLongitude;
    }

    public void setDLongitude(Float dLongitude) {
        this.dLongitude = dLongitude;
    }

    public String getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(String assignedAt) {
        this.assignedAt = assignedAt;
    }

    public String getScheduleAt() {
        return scheduleAt;
    }

    public void setScheduleAt(String scheduleAt) {
        this.scheduleAt = scheduleAt;
    }

    public Object getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Object startedAt) {
        this.startedAt = startedAt;
    }

    public Object getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Object finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Integer getUserRated() {
        return userRated;
    }

    public void setUserRated(Integer userRated) {
        this.userRated = userRated;
    }

    public Integer getProviderRated() {
        return providerRated;
    }

    public void setProviderRated(Integer providerRated) {
        this.providerRated = providerRated;
    }

    public Integer getUseWallet() {
        return useWallet;
    }

    public void setUseWallet(Integer useWallet) {
        this.useWallet = useWallet;
    }

    public Integer getSurge() {
        return surge;
    }

    public void setSurge(Integer surge) {
        this.surge = surge;
    }

    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getPayment() {
        return payment;
    }

    public void setPayment(Object payment) {
        this.payment = payment;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

}