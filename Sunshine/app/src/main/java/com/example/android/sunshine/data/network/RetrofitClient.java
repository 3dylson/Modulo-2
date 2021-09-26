package com.example.android.sunshine.data.network;

import static com.example.android.sunshine.data.network.ServerValues.BASE_URL;
import static com.example.android.sunshine.data.network.ServerValues.FORMAT;
import static com.example.android.sunshine.data.network.ServerValues.NUM_DAYS;
import static com.example.android.sunshine.data.network.ServerValues.UNITS;

import android.util.Log;

import com.example.android.sunshine.data.network.cb.DataRetrieved;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    private static final String TAG = RetrofitClient.class.getSimpleName();

    //private DataRetrieved listener = null;

    public static void getWeatherWithLatLon(DataRetrieved listener, Double latitude, Double longitude) {
        apiWeather().getCurrentWeatherWithLatitudeLongitude(latitude.toString(),
                longitude.toString(), FORMAT, UNITS, String.valueOf(NUM_DAYS))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (!response.isSuccessful()) {
                            Log.e(TAG, "code: " + response.code());
                            return;
                        }

                        listener.onDataFetchedSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e(TAG, "Unable to get forecast. Error: " + t.getMessage());
                        listener.onDataFetchedFailed();
                    }
                });
    }

    public static void getWeatherWithLocation(DataRetrieved listener, String location) {
        apiWeather().getCurrentWeatherWithLocation(location,FORMAT,UNITS,String.valueOf(NUM_DAYS))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (!response.isSuccessful()) {
                            Log.e(TAG, "code: " + response.code());
                            return;
                        }

                        listener.onDataFetchedSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e(TAG, "Unable to get forecast. Error: " + t.getMessage());
                        listener.onDataFetchedFailed();
                    }
                });
    }



    static WeatherAPI apiWeather() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        HttpLoggingInterceptor.Level bodyLevel = HttpLoggingInterceptor.Level.BODY;
        interceptor.level(bodyLevel);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

       return retrofit.create(WeatherAPI.class);
    }


}
