package com.lee.weatherforecast;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lee.weatherforecast.FetchData.HourlyDataAdapter;
import com.lee.weatherforecast.data.Current;
import com.lee.weatherforecast.data.FlushOldData;
import com.lee.weatherforecast.data.Hourly;
import com.lee.weatherforecast.databinding.ActivityMainBinding;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private WeatherViewModel weatherViewModel;
    static String TAG="Main";
    private ActivityMainBinding binding;
    LifecycleOwner owner;
    LiveData<List<Long>> weather_id;
    RecyclerView hourlyView;
    HourlyDataAdapter hourlyDataAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.activity_main);
        weatherViewModel=new ViewModelProvider(this).get(WeatherViewModel.class);
        scheduleDelete();
        hourlyView=binding.recyclertemp;
        hourlyView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        hourlyDataAdapter=new HourlyDataAdapter();
        hourlyView.setAdapter(hourlyDataAdapter);
        weatherViewModel.getAllCurrent().observe(this, new Observer<List<Current>>() {
            @Override
            public void onChanged(List<Current> current) {
                Log.i(TAG,"inside main live data onchange Current size"+current.size());
               // Log.i(TAG,"inside main live Current onchange "+current.get(0).getTemp());
                if(current.size()>0)
                    setCurrentData(current.get(0));

            }
        });
        weatherViewModel.getLatestHourly().observe(this, new Observer<List<Hourly>>() {
            @Override
            public void onChanged(List<Hourly> hourlies) {
               if(hourlies.size()>0) {
                   setHourlyData(hourlies);
                   Log.i(TAG, "got latest hourly " + hourlies.get(0).getTemp() + " " + hourlies.get(1).getTemp());
               }
            }
        });
        owner=this;
        String location="Mumbai";
        FloatingActionButton btnSearch=(FloatingActionButton)(findViewById(R.id.btnSearch));
        btnSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String location=binding.txtSearch.getText().toString()+"";
                if(location.length()>0) {
                    fetchWeatherId(location);
                   Log.i(TAG, "adding observer ");
                    weather_id.observe(owner, new Observer<List<Long>>() {
                    @Override
                    public void onChanged(List<Long> aLong) {
                        Log.i(TAG, "found in database " + aLong.size());

                        if (aLong.size() == 0) {
                            weatherViewModel.getCurrentWeather();
                            Log.i(TAG, "aLong null after weatherViewModel.getCurrentWeather(); ");
                            weather_id.removeObservers(owner);
                            Log.i(TAG, "aLong null after remove observer");
                        } else {
                            Log.i(TAG, "aLong " + aLong.get(0));
                            weatherViewModel.getCurrentForLocation(aLong.get(0)).observe(owner, new Observer<Current>() {
                                @Override
                                public void onChanged(Current current) {
                                    Log.i(TAG, "current location change ");
                                    setCurrentData(current);
                                }
                            });
                            weatherViewModel.getHourlyForLocation(aLong.get(0)).observe(owner, new Observer<List<Hourly>>() {
                                @Override
                                public void onChanged(List<Hourly> hourlies) {
                                    Log.i(TAG, "got hourly "+hourlies.get(0).getTemp()+" "+hourlies.get(1).getTemp());

                                }
                            });

                        }
                    }
                });
                }
                else
                    Toast.makeText(getApplicationContext(),"Please type location to search",Toast.LENGTH_LONG);
            }
        });
    }
    private void scheduleDelete()
    {

        final PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(FlushOldData.class,24, TimeUnit.HOURS,1,TimeUnit.MINUTES)
                .setInitialDelay(1000,TimeUnit.MILLISECONDS)

                .build();

        WorkManager workManager =  WorkManager.getInstance(this);

        workManager.enqueue(periodicWorkRequest);

        workManager.getWorkInfoByIdLiveData(periodicWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable WorkInfo workInfo) {
                        if (workInfo != null) {
                            Log.d(TAG, " periodicWorkRequest Status changed to : " + workInfo.getState());

                        }
                    }
                });
    }
    String getFormattedDate(long unixSeconds)
    {
        //long unixSeconds = (long)(weatherData.getCurrent().getSunrise());
        Date date = new java.sql.Date(unixSeconds*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MMM-yyyy");
        return sdf.format(date);

    }
    String getFormattedTime(long unixSeconds)
    {
        Date date = new java.sql.Date(unixSeconds*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("hh:mm a");
        return sdf.format(date);

    }


    private void fetchWeatherId(String location)
    {
        weather_id= weatherViewModel.getWeatherforLocation(location);

    }
    public void setHourlyData(List<Hourly> hourlyData)
    {
        setCurrentTemp(hourlyData);
        hourlyDataAdapter.setHourly(hourlyData);

    }
    private void setCurrentTemp(List<Hourly> hourlyData)
    {
        int time = (int) (System.currentTimeMillis());
        for(Hourly hourly:hourlyData)
            if(time>=hourly.getDt() && time<hourly.getDt())
                binding.txtTemp.setText(hourly.getTemp()+"C");
    }

    public void setCurrentData(Current currentData) {
        Log.i(TAG, "inside set currentdata");

        binding.txtDate.setText(""+getFormattedDate((long)currentData.getDt()));
        binding.txtTemp.setText(""+currentData.getTemp()+"C");

        if (currentData.getClouds()<25)
            binding.imgclouds.setImageDrawable(getDrawable(R.drawable.sun));
        else
        if (currentData.getClouds()<70)
            binding.imgclouds.setImageDrawable(getDrawable(R.drawable.sun_clouds1));
        else
        if (currentData.getClouds()>=70)
            binding.imgclouds.setImageDrawable(getDrawable(R.drawable.sun_clouds2));
        binding.lblfeel.setText("Feels Like: "+ currentData.getFeelsLike()+"C");
        binding.txthumid.setText(""+currentData.getHumidity()+"%");
        binding.txtdew.setText(""+currentData.getDewPoint()+" C");

        binding.txtpressure.setText(""+currentData.getPressure()+"mB");
        binding.txtuv.setText(""+currentData.getUvi());
        binding.txtvisibility.setText(""+currentData.getVisibility()+"m");
        binding.txtspeed.setText(""+currentData.getWindSpeed()+"km/h");
        binding.txtdegree.setText(""+currentData.getWindDeg());
        binding.txtrise.setText(""+getFormattedTime((long)currentData.getSunrise()));
        binding.txtset.setText(""+getFormattedTime((long)currentData.getSunset()));

    }
}