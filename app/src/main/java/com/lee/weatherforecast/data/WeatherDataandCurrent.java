package com.lee.weatherforecast.data;

import androidx.room.Embedded;
import androidx.room.Relation;

public class WeatherDataandCurrent {
    @Embedded
    public WeatherData weatherData;
    @Relation(
            parentColumn = "weatherdata_id",
            entityColumn = "currentWeatherdata_id"
    )
    public Current current;

}
