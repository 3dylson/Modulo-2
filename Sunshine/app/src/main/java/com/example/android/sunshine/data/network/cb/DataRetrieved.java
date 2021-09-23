package com.example.android.sunshine.data.network.cb;

import com.example.android.sunshine.data.network.responsemodels.WeatherResponse;

import org.json.JSONException;

public interface DataRetrieved {

    void onDataFetchedSuccess(String forecast);

    void onDataFetchedFailed();
}
