package com.example.android.sunshine.model;

import com.example.android.sunshine.data.database.entity.Weather;
import com.example.android.sunshine.presentation.adapters.ForecastAdapter;

import java.util.Date;

/**
 * Simplified {@link Weather} which only contains the details needed for the weather list in
 * the {@link ForecastAdapter}
 */
public class ListWeatherEntry {

    private int id;
    private int weatherIconId;
    private Date date;
    private double min;
    private double max;

    public ListWeatherEntry(int id, int weatherIconId, Date date, double min, double max) {
        this.id = id;
        this.weatherIconId = weatherIconId;
        this.date = date;
        this.min = min;
        this.max = max;
    }

    public int getId() {
        return id;
    }

    public int getWeatherIconId() {
        return weatherIconId;
    }

    public Date getDate() {
        return date;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }
}
