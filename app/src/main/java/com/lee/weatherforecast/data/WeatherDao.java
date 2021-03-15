package com.lee.weatherforecast.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


    @Dao
    public interface WeatherDao {
        @Insert
        long insert(WeatherData weatherData);
        @Update
        void update(WeatherData weatherData);
        @Delete
        void delete(WeatherData weatherData);

        @Query("DELETE FROM weatherData_table")
        void deleteAllweatherData();

        @Query("SELECT * FROM weatherData_table")
        public LiveData<List<WeatherData>> getAllWeatherData();

        @Query("SELECT weatherdata_id FROM weatherData_table " +
                "WHERE lat = :lat and lon= :lon order by weatherdata_id desc" )
        public LiveData<List<Long>> searchLocation(Double lat, Double lon);
    }

