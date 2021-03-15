package com.lee.weatherforecast.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity(tableName = "minutely_table")
public class Minutely {
    @PrimaryKey(autoGenerate = true)
    private int minutely_id;
    @SerializedName("dt")
    @Expose
    private Integer dt;
    @SerializedName("precipitation")
    @Expose
    private Integer precipitation;

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public Integer getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Integer precipitation) {
        this.precipitation = precipitation;
    }

}
