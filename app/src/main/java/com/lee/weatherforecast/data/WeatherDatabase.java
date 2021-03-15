package com.lee.weatherforecast.data;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {WeatherData.class, Current.class,Hourly.class}, version = 3)
public abstract class WeatherDatabase extends RoomDatabase {
    private static WeatherDatabase instance;
    public abstract WeatherDao WeatherDao();
    public abstract CurrentDao currentDao();
    public abstract HourlyDao hourlyDao();

    // public abstract WeatherDataAndCurrentDao weatherDataAndCurrentDao();

    public static synchronized WeatherDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    WeatherDatabase.class, "weather_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}