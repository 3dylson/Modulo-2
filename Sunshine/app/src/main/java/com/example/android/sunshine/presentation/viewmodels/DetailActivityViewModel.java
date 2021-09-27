package com.example.android.sunshine.presentation.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.sunshine.data.database.SunshineDatabase;
import com.example.android.sunshine.data.database.entity.Weather;
import com.example.android.sunshine.data.network.SunshineRepository;

import java.util.Date;

public class DetailActivityViewModel extends AndroidViewModel {

    // Weather forecast the user is looking at
    private final LiveData<Weather> mWeather;


    public DetailActivityViewModel(@NonNull Application application, Date date ) {
        super(application);
        SunshineDatabase database = SunshineDatabase.getInstance(application);
        SunshineRepository mRepository = SunshineRepository.getInstance(database.weatherDao(), application);
        // Date for the weather forecast
        mWeather = mRepository.getWeatherByDate(date);

    }


    public LiveData<Weather> getWeather() {
        return mWeather;
    }
}

