package com.example.android.sunshine.data.network;

import androidx.lifecycle.LiveData;

import com.example.android.sunshine.data.database.dao.WeatherDao;
import com.example.android.sunshine.data.database.entity.Weather;

/**
 * Handles data operations in Sunshine. Acts as a mediator between {@link WeatherAPI}
 * and {@link WeatherDao}
 */
public class SunshineRepository {

    private static SunshineRepository sunshineRepository;
    private final WeatherDao mWeatherDao;
    private WeatherAPI weatherAPI;

    SunshineRepository(WeatherDao mWeatherDao){
        this.mWeatherDao = mWeatherDao;
        weatherAPI = RetrofitClient.initWeatherAPI();

        //LiveData<Weather[]> networkData = weatherAPI.getCurrentWeatherWithLatitudeLongitude()
    }

    /*public static SunshineRepository getInstance() {

    }*/
}
