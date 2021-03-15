package com.lee.weatherforecast.FetchData;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.lee.weatherforecast.R;
import com.lee.weatherforecast.data.WeatherData;
import com.lee.weatherforecast.data.WeatherRepository;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherManager {
    String Tag="WeatherManager";
    Double lat = 35.0;
    Double lon = 139.0;
    Context ctx;
    public WeatherManager(Context ctx) {
       /* this.lat = lat;
        this.lon = lon;*/
        this.ctx=ctx;
    }

    public WeatherManager() {
    }
    public LatLng getLatLon(String location)
    {
        if(Geocoder.isPresent()){
            try {

                Geocoder gc = new Geocoder(ctx);
                List<Address> addresses= gc.getFromLocationName(location, 1); // get the found Address Objects

               // List<LatLng> ll = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available

                for(Address a : addresses){
                    if(a.hasLatitude() && a.hasLongitude()){
                        lat=a.getLatitude();
                        lon=a.getLongitude();
                        return new LatLng(a.getLatitude(), a.getLongitude());
                        //ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
                    }
                }
            } catch (IOException e) {
                lat=null;
                lon=null;
                Log.i(Tag,"lat lon fail "+e.getMessage());
                return null;
                // handle the exception
            }
        }
        return null;
    }
   // LiveData<Current> currentWeatherForLocation;
    public static String BaseUrl = "https://api.openweathermap.org/";
    /*public void getCurrentLocalData(WeatherRepository repository, String location) {
        // .baseUrl(ctx.getString(R.string.base_url))
        getLatLon(location);
        if (lat != null) {
            // search in local database
            // LiveData<Current> currentWeatherForLocation=


            *//*if(currentWeatherForLocation!=null) {
                Log.i(Tag, "found in database " + currentWeatherForLocation.getValue().getTemp());

            }
            else

            {*//*
        }
    }*/
    public void getCurrentData(WeatherRepository repository) {

        //not found in local database
        if (lat != null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BaseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                DataInterface dataInterface = retrofit.create(DataInterface.class);
                Call<WeatherData> call = dataInterface.getCurrentWeatherData(lat.toString(), lon.toString(), "daily,minutely","metric", ctx.getString(R.string.api_key));
                call.enqueue(new Callback<WeatherData>() {
                    @Override
                    public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                        if (response.code() == 200) {
                            WeatherData weatherData = response.body();
                            assert weatherData != null;
                            Log.i(Tag, "got it " + weatherData.getTimezone());
                            //Log.i(Tag," temp "+weatherData.getCurrent().getTemp()+" Sunrise "+formattedDate);
                            repository.insert(weatherData);
                            //Log.i(Tag," temp "+weatherData.getCurrent().getTemp()+" Sunrise "+formattedDate);

                        }

                    }

                    @Override
                    public void onFailure(Call<WeatherData> call, Throwable t) {
                        Log.i(Tag, "fail " + t.getMessage());

                    }
                });
            }
        }
    }


