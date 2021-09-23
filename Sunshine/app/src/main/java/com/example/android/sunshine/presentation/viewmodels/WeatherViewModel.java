package com.example.android.sunshine.presentation.viewmodels;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.data.network.utils.NetworkUtils;
import com.example.android.sunshine.data.network.utils.OpenWeatherJsonParser;

import java.net.URL;

public class WeatherViewModel extends AndroidViewModel {
    private final MutableLiveData<String[]> weatherLiveData = new MutableLiveData<>();


    public WeatherViewModel(@NonNull Application application) {
        super(application);
    }
}
