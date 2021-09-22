package com.example.android.sunshine.data.network;

import static com.example.android.sunshine.data.network.ServerValues.BASE_URL;

import retrofit2.Retrofit;

public class RetrofitClient {


    static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build();

    public static WeatherAPI initWeatherAPI () {
        return retrofit.create(WeatherAPI.class);
    }


}
