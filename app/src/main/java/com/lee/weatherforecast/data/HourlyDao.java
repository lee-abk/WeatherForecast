package com.lee.weatherforecast.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface HourlyDao {
    @Insert
    void insert(Hourly hourly);
    @Update
    void update(Hourly hourly);
    @Delete
    void delete(Hourly hourly);

    @Query("DELETE FROM hourly_table")
    void deleteAllHourly();
    @Query("SELECT hourly_id,hourlyWeatherdata_id,`temp`,feelsLike,dt FROM hourly_table order by hourly_id limit 24")
    public LiveData<List<Hourly>> getLatestHourly();

    @Query("SELECT hourly_id,hourlyWeatherdata_id,`temp`,feelsLike,dt FROM hourly_table where hourlyWeatherdata_id=:parentId order by hourly_id limit 24")
    public LiveData<List<Hourly>> getHourlyforLocation(long parentId);

}
