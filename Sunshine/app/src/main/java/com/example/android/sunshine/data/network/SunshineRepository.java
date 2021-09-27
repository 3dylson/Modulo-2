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
import com.example.android.sunshine.utils.NotificationUtils;
import com.example.android.sunshine.utils.SunshineDateUtils;
import static com.example.android.sunshine.data.network.ServerValues.NUM_DAYS;

import android.content.Context;
import android.text.format.DateUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
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
    private final MutableLiveData<Weather[]> mDownloadedWeatherForecasts = new MutableLiveData<>();
    private final Weather[] emptyList = (Weather[]) new ArrayList<>().toArray(new Weather[0]);
    private Weather todayWeather;

    SunshineRepository(WeatherDao mWeatherDao, Context context){
        this.mWeatherDao = mWeatherDao;
        this.context = context;

        fetchWeather();

        mDownloadedWeatherForecasts.observeForever(newForeCastsFromNetwork -> {
            SunshineDatabase.databaseWriteExecutor.execute(()->{
                deleteOldData();
                mWeatherDao.bulkInsert(newForeCastsFromNetwork);
                todayWeather = newForeCastsFromNetwork.clone()[0];
            });
        });

    }

    public synchronized static SunshineRepository getInstance(WeatherDao weatherDao, Context context) {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                INSTANCE = new SunshineRepository(weatherDao, context);
            }
        }
        return INSTANCE;
    }

    public void fetchWeather() {
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
    public boolean isFetchNeeded() {
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
                /*
                 * Finally, after we insert data, determine whether or not
                 * we should notify the user that the weather has been refreshed.
                 */
                boolean notificationsEnabled = SunshinePreferences.areNotificationsEnabled(context);

                /*
                 * If the last notification was shown was more than 1 day ago, we want to send
                 * another notification to the user that the weather has been updated. Remember,
                 * it's important that you shouldn't spam your users with notifications.
                 */
                long timeSinceLastNotification = SunshinePreferences
                        .getEllapsedTimeSinceLastNotification(context);

                boolean oneDayPassedSinceLastNotification = false;

                if (timeSinceLastNotification >= DateUtils.DAY_IN_MILLIS) {
                    oneDayPassedSinceLastNotification = true;
                }

                /*
                 * We only want to show the notification if the user wants them shown and we
                 * haven't shown a notification in the past day.
                 */
                if (todayWeather != null && notificationsEnabled && oneDayPassedSinceLastNotification) {
                    NotificationUtils.notifyUserOfNewWeather(context, todayWeather );
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDataFetchedFailed() {
        mDownloadedWeatherForecasts.postValue(emptyList);
    }
}
