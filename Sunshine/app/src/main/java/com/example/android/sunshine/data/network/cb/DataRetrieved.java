package com.example.android.sunshine.data.network.cb;

public interface DataRetrieved {

    void onDataFetchedSuccess(String forecast);

    void onDataFetchedFailed();
}
