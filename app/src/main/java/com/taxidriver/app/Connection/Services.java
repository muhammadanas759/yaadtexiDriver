package com.taxidriver.app.Connection;



import com.taxidriver.app.Activities.UpdateProfile;
import com.taxidriver.app.ApiResponse.HelpResponse.HelpResponse;
import com.taxidriver.app.ApiResponse.Logout.LogoutResponse;
import com.taxidriver.app.ApiResponse.PasswordResponse.PasswordResponse;
import com.taxidriver.app.ApiResponse.SignupResponse.SignUpResponse;
import com.taxidriver.app.ApiResponse.Status.StatusResponse;
import com.taxidriver.app.ApiResponse.Summary.SummaryResponse;
import com.taxidriver.app.ApiResponse.UpdateProfile.UpdateProfileResponse;
import com.taxidriver.app.ApiResponse.login.User;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Services {
@POST("api/provider/yaad/register")
    Call<SignUpResponse> signup(
                                @Query("device_id")  String device_id,
                                @Query("device_type")  String device_type,
                                @Query("device_token")  String device_token,
                                @Query("first_name")  String first_name,
                                @Query("last_name")  String last_name,
                                @Query("email")  String email,
                                @Query("mobile")  String mobile,
                                @Query("password")  String password,
                                @Query("password_confirmation")  String password_confirmation
                                );
    @POST("api/provider/yaad/login")
    Call<User> Login(@Query("device_id")  String device_id,
                     @Query("device_type")  String device_type,
                     @Query("device_token")  String device_token,
                     @Query("email") String email,
                     @Query("password") String password);

    @POST("api/provider/yaad/logout")
    Call<LogoutResponse> Logout(@Header("Authorization") String token);


 @GET("api/provider/yaad/summary")
    Call<SummaryResponse> Summary(@Header("Authorization") String token);



    @GET("api/provider/yaad/help")
    Call<HelpResponse> Help(@Header("Authorization") String token);

    @POST("api/provider/yaad/forgot/password")
    Call<User> ForgetPassword(@Header("Authorization") String token,
                     @Query("mobile")  String mobile);

    @POST("api/provider/yaad/reset/password")
    Call<User> ResetPassword(@Header("Authorization") String token,
                     @Query("mobile")  String mobile,
                     @Query("password_confirmation")  String password_confirmation,
                     @Query("password") String password,
                     @Query("otp") String otp);

    @POST("api/provider/yaad/location")
    Call<LogoutResponse> UpdateLocation(@Header("Authorization") String token,
                                        @Query("latitude") Double latitude,
                                        @Query("longitude") Double longitude);

    @POST("api/provider/yaad/available")
    Call<StatusResponse> StatusUpdate(@Header("Authorization") String token,
                                      @Query("service_status")  String service_status );

    //

    //    @GET("api/provider/yaad/services")
//    Call<List<Service>> Services(@Header("Authorization") String token);
//
//    @GET("api/provider/yaad/estimated/fare")
//    Call<EstimatedPrice> EstimatedPrices(@Header("Authorization") String token,
//                                         @Query("s_latitude") Double s_latitude,
//                                         @Query("s_longitude") Double s_longitude,
//                                         @Query("d_latitude") Double d_latitude,
//                                         @Query("d_longitude") Double d_longitude,
//                                         @Query("service_type") Integer service_type);
//
//
//    @POST("api/provider/yaad/send/request")
//    Call<RideRequest> RequestRide(@Header("Authorization") String token,
//                                  @Query("s_latitude") Double s_latitude,
//                                  @Query("s_longitude") Double s_longitude,
//                                  @Query("d_latitude") Double d_latitude,
//                                  @Query("d_longitude") Double d_longitude,
//                                  @Query("service_type") Integer service_type,
//                                  @Query("distance") Double distance,
//                                  @Query("payment_mode") String payment_mode,
//                                  @Query("no_of_bags") Integer no_of_bags,
//                                  @Query("passengers") Integer passengers
//    );
//
//    @POST("api/provider/yaad/cancel/request")
//    Call<CancelRide> CancelRide(@Header("Authorization") String token,
//                                @Query("request_id") Double request_id,
//                                @Query("cancel_reason") String cancel_reason);
//
//    @GET("api/provider/yaad/show/providers")
//    Call<List<Providers>> GetProviders(@Header("Authorization") String token,
//                                       @Query("latitude") Double latitude,
//                                       @Query("longitude") Double longitude,
//                                       @Query("service") Integer service);
//
//
//    @GET("api/provider/yaad/details")
//    Call<provider> GetproviderDetails(@Header("Authorization") String token,
//                              @Query("device_type") String device_type,
//                              @Query("device_token") String device_token,
//                              @Query("device_id") String device_id);
//
//
//    //
////    @POST("api/provider/yaad/update/profile")
////    Call<UpdateResponse> UpdateProfile(@Header("Authorization") String token,
////                                       @Query("first_name") String first_name,
////                                       @Query("last_name") String last_name,
////                                       @Query("email") String email,
////                                       @Query("mobile") String mobile,
////
////                                      @Query MultipartBody.Part picture);
    @Multipart
    @POST("api/provider/yaad/profile")
    Call<UpdateProfileResponse> UpdateProfile(@Header("Authorization") String token,
                                              @Part("first_name") RequestBody first_name,
                                              @Part("last_name") RequestBody last_name,
                                              @Part("email") RequestBody email,
                                              @Part("mobile") RequestBody mobile,
                                              @Part MultipartBody.Part avatar );
//
@GET("api/provider/yaad/profile")
Call<UpdateProfileResponse> getProfile(@Header("Authorization") String token
                                           );
    //
//
    @POST("api/provider/yaad/password")
    Call<PasswordResponse> UpdatePassword(@Header("Authorization") String token,
                                          @Query("password") String password,
                                          @Query("password_confirmation") String password_confirmation,
                                          @Query("password_old") String password_old);

//    @POST("api/provider/yaad/update/location")
//    Call<UpdateLocation> UpdateLocation(@Header("Authorization") String token,
//                                        @Query("latitude") Double latitude,
//                                        @Query("longitude") Double longitude);
//
//    @GET("api/provider/yaad/trips")
//    Call<List<TripResponse>> Trip(@Header("Authorization") String token);
//    @GET("api/provider/yaad/upcoming/trips")
//    Call<List<UpcomingResponse>> Upcoming(@Header("Authorization") String token);
//    @GET("api/provider/yaad/add/money")
//    Call<WalletproviderResponse> AddMoney(@Header("Authorization") String token,
//                                      @Query("amount") String amount,
//                                      @Query("card_id") String card_id);

}