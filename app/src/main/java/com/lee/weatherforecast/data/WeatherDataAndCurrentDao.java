package com.lee.weatherforecast.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface WeatherDataAndCurrentDao {
    @Transaction
    @Query("SELECT * FROM weatherdata_table")
    public LiveData< List<WeatherDataandCurrent>> getWeatherDataandCurrent();



}

