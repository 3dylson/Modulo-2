package com.example.android.sunshine.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.sunshine.R;
import com.example.android.sunshine.data.database.entity.Weather;
import com.example.android.sunshine.model.ListWeatherEntry;
import com.example.android.sunshine.presentation.ui.list.MainActivity;
import com.example.android.sunshine.utils.SunshineDateUtils;
import com.example.android.sunshine.utils.SunshineWeatherUtils;

import java.util.Date;
import java.util.List;

/**
 * Exposes a list of weather forecasts from a list of {@link Weather} to a {@link RecyclerView}.
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private List<ListWeatherEntry> mWeatherData;

    private final ForecastAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface ForecastAdapterOnClickHandler {
        void onClick(ListWeatherEntry weatherForDay);
    }


    public ForecastAdapter(ForecastAdapterOnClickHandler mClickHandler) {

        this.mClickHandler = mClickHandler;
    }

    /**
     * Cache of the children views for a forecast list item.
     */
    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mWeatherTextView;

        public ForecastAdapterViewHolder(View view) {
            super(view);
            mWeatherTextView = view.findViewById(R.id.tv_weather_data);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            ListWeatherEntry weatherForDay = mWeatherData.get(adapterPosition);
            mClickHandler.onClick(weatherForDay);
        }
    }



    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.forecast_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ForecastAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder forecastAdapterViewHolder, int position) {
        ListWeatherEntry weatherForThisDay = mWeatherData.get(position);
        forecastAdapterViewHolder.mWeatherTextView.setText(weatherForThisDay.toString());
    }


    @Override
    public int getItemCount() {
        if (null == mWeatherData) return 0;
        return mWeatherData.size();
    }


    public void setWeatherData(List<ListWeatherEntry> weatherData) {
        mWeatherData = weatherData;
        notifyDataSetChanged();
    }
}
