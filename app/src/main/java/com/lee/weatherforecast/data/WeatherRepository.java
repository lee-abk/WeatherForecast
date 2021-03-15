package com.lee.weatherforecast.data;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class WeatherRepository {
    static String TAG="WeatherRepository";
    private WeatherDao weatherDao;
    private LiveData<List<WeatherData>> allWeather;
    private CurrentDao currentDao;
    private LiveData<List<Current>> allCurrent;
    private HourlyDao hourlyDao;
    private LiveData<List<Hourly>> latestHourly;

    Application application;
    Double lat = 35.0;
    Double lon = 139.0;
    public WeatherRepository(Application application) {
        this.application=application;
        WeatherDatabase database = WeatherDatabase.getInstance(application);
        weatherDao = database.WeatherDao();
        allWeather = weatherDao.getAllWeatherData();
        currentDao = database.currentDao();
        allCurrent = currentDao.getAllCurrent();
        hourlyDao = database.hourlyDao();
        latestHourly=hourlyDao.getLatestHourly();
        weather_id=weatherDao.searchLocation(lat,lon);
        }


    private LiveData<List<Long>> weather_id;
    public LiveData<List<Long>> getCurrentWeatherForLocation(Double lat, Double lon)
    {
        Log.i("WeatherRepository","lat lon  "+lat+"lon "+lon );

        weather_id=weatherDao.searchLocation(lat,lon);
        return weather_id;
                    //CurrentWeatherForLocation = currentDao.getCurrentforLocation(aLong);

    }

    private LiveData<Current> current;
    public LiveData<Current> getCurrentForLocation(long weatherId)
    {
        current=currentDao.getCurrentforLocation(weatherId);
        return current;
    }
    private LiveData<List<Hourly>> hourly;
    public LiveData<List<Hourly>> getHourlyForLocation(long weatherId)
    {
        hourly=hourlyDao.getHourlyforLocation(weatherId);
        return hourly;
    }

    public void insert(WeatherData weatherData) {
        new InsertWeatherDataAsyncTask(weatherDao,currentDao,hourlyDao).execute(weatherData);
    }
    public LiveData<List<WeatherData>> getAllWeatherData() {
        return allWeather;
    }

    public LiveData<List<Current>> getAllCurrent() {
        return allCurrent;
    }

    public LiveData<List<Hourly>> getLatestHourly() {
        return latestHourly;
    }

    private static class InsertWeatherDataAsyncTask extends AsyncTask<WeatherData, Void, Void> {
        private WeatherDao weatherDao;
        private CurrentDao currentDao;
        private HourlyDao hourlyDao;

        private InsertWeatherDataAsyncTask(WeatherDao weatherDao,CurrentDao currentDao,HourlyDao hourlyDao) {
            this.weatherDao = weatherDao;
            this.currentDao=currentDao;
            this.hourlyDao=hourlyDao;
        }
        @Override
        protected Void doInBackground(WeatherData... weatherData) {
            long id=weatherDao.insert(weatherData[0]);
            Log.i(TAG,"after insert 1 weather, weatherid "+id);

            weatherData[0].getCurrent().setCurrentWeatherdata_id(id);
            Log.i(TAG,"after assign weather, weatherid "+weatherData[0].getCurrent().getCurrentWeatherdata_id());
            // Assuming one row of current data for each weather call
            currentDao.insert(weatherData[0].getCurrent());
            Log.i(TAG,"hourly size "+weatherData[0].getHourly().size());
            for(Hourly hourly:weatherData[0].getHourly())
            {
                hourly.setHourlyWeatherdata_id(id);
                hourlyDao.insert(hourly);
            }
            return null;
        }
    }
}
