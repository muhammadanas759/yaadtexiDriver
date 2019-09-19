package com.yaaddrivertaxi.app.Connection;



public class Utils {

    public static final String BASE_URL = "http://www.yaadtaxi.com/";
    public static final String BASE_URL2 = "http://yaadtaxi.com/";

    public static Services getApiService(){
        return RetrofitConnection.getClient(BASE_URL).create(Services.class);
    }
    public static Services getApiService2(){
        return RetrofitConnection.getClient(BASE_URL2).create(Services.class);
    }
}
