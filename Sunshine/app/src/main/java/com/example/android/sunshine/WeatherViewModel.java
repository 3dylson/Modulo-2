package com.example.android.sunshine;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utils.NetworkUtils;
import com.example.android.sunshine.utils.OpenWeatherJsonUtils;

import java.net.URL;
import java.util.Arrays;

public class WeatherViewModel extends AndroidViewModel {
    private final MutableLiveData<String[]> weatherLiveData = new MutableLiveData<>();

    //private final Observer<String[]> weatherLiveDataObserver = weatherLiveData -> loadWeatherData();


    public WeatherViewModel(@NonNull Application application) {
        super(application);
        loadWeatherData();
    }

    public LiveData<String[]> getWeather() {
        return weatherLiveData;
    }


    public LiveData<String[]> loadWeatherData() {

        String location = SunshinePreferences.getPreferredWeatherLocation(getApplication().getApplicationContext());
        new FetchWeatherTask().execute(location);
        //weatherLiveData.observeForever(weatherLiveDataObserver);
        return getWeather();
    }

    class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            URL weatherRequestUrl = NetworkUtils.getUrl(getApplication().getApplicationContext());

            try {
                String jsonWeatherResponse = NetworkUtils
                        .getResponseFromHttpUrl(weatherRequestUrl);

                return OpenWeatherJsonUtils
                        .getSimpleWeatherStringsFromJson(getApplication().getApplicationContext(), jsonWeatherResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] weatherLiveData) {
            WeatherViewModel.this.weatherLiveData.setValue(weatherLiveData);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        //weatherLiveData.removeObserver(weatherLiveDataObserver);
    }

}
