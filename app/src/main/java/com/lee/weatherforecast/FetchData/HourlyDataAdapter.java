package com.lee.weatherforecast.FetchData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lee.weatherforecast.R;
import com.lee.weatherforecast.data.Hourly;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class HourlyDataAdapter extends RecyclerView.Adapter<HourlyDataAdapter.HourlyHolder>{
    private List<Hourly> hourlyData ;
    @NonNull
    @Override
    public HourlyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_data,parent,false);
                //.inflate(R.Layout.hourly_temp, parent, false);*/
        return new HourlyHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull HourlyHolder holder, int position) {

       // Log.i("onBindViewHolder", "inside temp "+currentHourly.getTemp());
        if(position==0)
        {
            holder.textCTime.setText("Time");
            holder.textCTemp.setText("Temperature:");
            holder.textCFeels.setText("Feels Like:");
        }
        else {
            Hourly currentHourly = hourlyData.get(position-1);

            holder.textCTime.setText( getFormattedTime(currentHourly.getDt()) + "");
            holder.textCTemp.setText(currentHourly.getTemp() + " C");
            holder.textCFeels.setText(String.valueOf(currentHourly.getFeelsLike()) + " C");
        }
        }
    String getFormattedTime(long unixSeconds)
    {
        Date date = new java.sql.Date(unixSeconds*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("hh:mm a");
        return sdf.format(date);

    }
    @Override
    public int getItemCount() {
        if(hourlyData==null)
            return 0;
        else
            return hourlyData.size();
    }
    public void setHourly(List<Hourly> hourly) {
        this.hourlyData = hourly;
        notifyDataSetChanged();
    }
    class HourlyHolder extends RecyclerView.ViewHolder {
        private TextView textCTemp;
        private TextView textCFeels;
        private TextView textCTime;
        public HourlyHolder(View itemView) {
            super(itemView);

            textCTime = itemView.findViewById(R.id.txtctime);
            textCTemp = itemView.findViewById(R.id.txtctemp);
            textCFeels = itemView.findViewById(R.id.txtcfeels);
        }
    }
}
