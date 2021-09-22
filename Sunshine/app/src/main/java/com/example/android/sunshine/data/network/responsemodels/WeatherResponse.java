package com.example.android.sunshine.data.network.responsemodels;

import androidx.annotation.NonNull;

import com.example.android.sunshine.data.database.entity.Weather;

/**
 * Weather response from the backend. Contains the weather forecasts.
 */
public class WeatherResponse {

    @NonNull
    private final Weather[] mWeatherForecast;

    public WeatherResponse(@NonNull final Weather[] weatherForecast) {
        mWeatherForecast = weatherForecast;
    }

    @NonNull
    public Weather[] getmWeatherForecast() {
        return mWeatherForecast;
    }
}
