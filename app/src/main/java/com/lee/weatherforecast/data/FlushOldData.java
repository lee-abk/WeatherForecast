package com.lee.weatherforecast.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class FlushOldData extends Worker {
    Context ctx;
    public FlushOldData(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.ctx=context;
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.d("FlushOldData","flush old data in background");
        WeatherDatabase database = WeatherDatabase.getInstance(ctx);

        database.WeatherDao().deleteAllweatherData();
        database.currentDao().deleteAllCurrent();
        database.hourlyDao().deleteAllHourly();


        return Result.success();
    }
}
