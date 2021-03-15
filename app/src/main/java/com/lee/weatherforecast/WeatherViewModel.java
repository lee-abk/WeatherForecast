package com.lee.weatherforecast;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.android.gms.maps.model.LatLng;
import com.lee.weatherforecast.FetchData.WeatherManager;
import com.lee.weatherforecast.data.Current;
import com.lee.weatherforecast.data.Hourly;
import com.lee.weatherforecast.data.WeatherData;
import com.lee.weatherforecast.data.WeatherRepository;

import java.util.List;

public class WeatherViewModel extends AndroidViewModel {
    private WeatherRepository repository;
    private LiveData<List<WeatherData>> allWeather;
    private LiveData<List<Current>> allCurrent;
    private LiveData<List<Hourly>> latestHourly;
    Application application;
    private LiveData<List<Long>> weather_id;
    WeatherManager weatherManager;
    public WeatherViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
        repository=new WeatherRepository(application);
        weatherManager=new WeatherManager(application.getApplicationContext());
        allWeather=repository.getAllWeatherData();
        allCurrent=repository.getAllCurrent();
        latestHourly=repository.getLatestHourly();

        Log.i("Viewmodel","model contructed ");
    }
    public LiveData<List<Long>> getWeatherforLocation(String location)
    {
        LatLng latlon=weatherManager.getLatLon(location);
        Log.i("Viewmodel","lat lon  "+Double.parseDouble(String.format("%.4f",latlon.latitude))+"lon "+Double.parseDouble(String.format("%.4f",latlon.longitude)) );
        weather_id=repository.getCurrentWeatherForLocation(Double.parseDouble(String.format("%.4f",latlon.latitude)),Double.parseDouble(String.format("%.4f",latlon.longitude)));

        return weather_id;
    }
    private LiveData<List<Hourly>> hourly;
    public LiveData<List<Hourly>> getHourlyForLocation(long weatherId)
    {
        hourly=repository.getHourlyForLocation(weatherId);
        return hourly;
    }
    private LiveData<Current> current;
    public LiveData<Current> getCurrentForLocation(long weatherId)
    {
        current=repository.getCurrentForLocation(weatherId);
        return current;

    }
    public void getCurrentWeather()
    {
        Log.i("Viewmodel","getCurrentWeather ");

        weatherManager.getCurrentData(repository);
    }
    public LiveData<List<WeatherData>> getAllWeather() {
        return allWeather;
    }
    public LiveData<List<Current>> getAllCurrent() {
        return allCurrent;
    }
    public LiveData<List<Hourly>> getLatestHourly() {
        return latestHourly;
    }


}
