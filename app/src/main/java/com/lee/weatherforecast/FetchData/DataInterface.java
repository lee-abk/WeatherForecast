package com.lee.weatherforecast.FetchData;

import com.lee.weatherforecast.data.WeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DataInterface {
    @GET("data/2.5/onecall?")
    Call<WeatherData> getCurrentWeatherData(@Query("lat") String lat, @Query("lon") String lon,@Query("exclude") String exclude,@Query("units") String units, @Query("APPID") String app_id);
}
