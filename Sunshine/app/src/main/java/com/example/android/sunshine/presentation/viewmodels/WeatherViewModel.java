package com.example.android.sunshine.presentation.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.sunshine.data.database.SunshineDatabase;
import com.example.android.sunshine.data.database.entity.Weather;
import com.example.android.sunshine.data.network.SunshineRepository;
import com.example.android.sunshine.data.network.responsemodels.WeatherResponse;
import com.example.android.sunshine.model.ListWeatherEntry;

import java.util.List;

public class WeatherViewModel extends AndroidViewModel {
    private final SunshineRepository mRepository;
    private final LiveData<List<ListWeatherEntry>> mForecast;


    public WeatherViewModel(@NonNull Application application) {
        super(application);
        SunshineDatabase database = SunshineDatabase.getInstance(application);
        MutableLiveData<Weather[]> data = new MutableLiveData<>();
        mRepository = SunshineRepository.getInstance(database.weatherDao(), application, data);
        mForecast = mRepository.getCurrentWeatherForecasts();
    }

    public void refresh(){
        mRepository.fetchWeather();
    }

    public LiveData<List<ListWeatherEntry>> getForecast() {
        return mForecast;
    }
}
