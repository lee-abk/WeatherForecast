package com.lee.weatherforecast.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CurrentDao {
    @Insert
    void insert(Current current);
    @Update
    void update(Current current);
    @Delete
    void delete(Current current);

    @Query("DELETE FROM current_table")
    void deleteAllCurrent();
    @Query("SELECT * FROM current_table order by current_id desc")
    public LiveData<List<Current>> getAllCurrent();

    @Query("SELECT * FROM current_table where currentWeatherdata_id=:parentId")
    public LiveData<Current> getCurrentforLocation(long parentId);

}


