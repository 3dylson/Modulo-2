package com.example.android.sunshine.data.network;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.data.database.SunshineDatabase;
import com.example.android.sunshine.data.database.dao.WeatherDao;
import com.example.android.sunshine.data.database.entity.Weather;
import com.example.android.sunshine.data.network.cb.DataRetrieved;
import com.example.android.sunshine.data.network.responsemodels.WeatherResponse;
import com.example.android.sunshine.data.network.utils.OpenWeatherJsonParser;
import com.example.android.sunshine.model.ListWeatherEntry;
import com.example.android.sunshine.utils.SunshineDateUtils;
import static com.example.android.sunshine.data.network.ServerValues.NUM_DAYS;

import android.content.Context;

import org.json.JSONException;

import java.util.Date;
import java.util.List;

/**
 * Handles data operations in Sunshine. Acts as a mediator between {@link WeatherAPI}
 * and {@link WeatherDao}
 */
public class SunshineRepository implements DataRetrieved {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static SunshineRepository INSTANCE;
    private final WeatherDao mWeatherDao;
    private final Context context;
    // LiveData storing the latest downloaded weather forecasts
    private final MutableLiveData<Weather[]> mDownloadedWeatherForecasts;

    SunshineRepository(WeatherDao mWeatherDao, Context context, MutableLiveData<Weather[]> mDownloadedWeatherForecasts){
        this.mWeatherDao = mWeatherDao;
        this.context = context;
        this.mDownloadedWeatherForecasts = mDownloadedWeatherForecasts;

        fetchWeather();

        mDownloadedWeatherForecasts.observeForever(newForeCastsFromNetwork -> {
            SunshineDatabase.databaseWriteExecutor.execute(()->{
                deleteOldData();
                mWeatherDao.bulkInsert(newForeCastsFromNetwork);
            });
        });
    }

    public synchronized static SunshineRepository getInstance(WeatherDao weatherDao, Context context, MutableLiveData<Weather[]> mutableLiveData) {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                INSTANCE = new SunshineRepository(weatherDao, context, mutableLiveData);
            }
        }
        return INSTANCE;
    }

    void fetchWeather() {
        SunshineDatabase.databaseWriteExecutor.execute(this::getData);
    }

    private void getData() {
        if (SunshinePreferences.isLocationLatLonAvailable(context)) {
            double[] preferredCoordinates = SunshinePreferences.getLocationCoordinates(context);
            double latitude = preferredCoordinates[0];
            double longitude = preferredCoordinates[1];
            RetrofitClient.getWeatherWithLatLon(this, latitude, longitude);
        } else {
            String locationQuery = SunshinePreferences.getPreferredWeatherLocation(context);
            RetrofitClient.getWeatherWithLocation(this, locationQuery);
        }
    }

    public LiveData<List<ListWeatherEntry>> getCurrentWeatherForecasts() {
        Date today = SunshineDateUtils.getNormalizedUtcDateForToday();
        return mWeatherDao.getCurrentWeatherForecasts(today);
    }

    public LiveData<Weather> getWeatherByDate(Date date) {
        return mWeatherDao.getWeatherByDate(date);
    }

    private void deleteOldData() {
        Date today = SunshineDateUtils.getNormalizedUtcDateForToday();
        mWeatherDao.deleteOldWeather(today);
    }

    /**
     * Checks if there are enough days of future weather for the app to display all the needed data.
     *
     * @return Whether a fetch is needed
     */
    private boolean isFetchNeeded() {
        Date today = SunshineDateUtils.getNormalizedUtcDateForToday();
        int count = mWeatherDao.countAllFutureWeather(today);
        return (count < NUM_DAYS);
    }


    @Override
    public void onDataFetchedSuccess(String forecast) {
        try {

            WeatherResponse response = new OpenWeatherJsonParser().parse(forecast);
            if (response != null && response.getWeatherForecast().length != 0) {
                mDownloadedWeatherForecasts.postValue(response.getWeatherForecast());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDataFetchedFailed() {

    }
}
