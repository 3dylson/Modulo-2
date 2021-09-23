package com.example.android.sunshine.data.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.android.sunshine.data.database.SunshineDatabase;
import com.example.android.sunshine.data.database.entity.Weather;
import com.example.android.sunshine.model.ListWeatherEntry;

import java.util.Date;
import java.util.List;

/**
 * {@link Dao} which provides an api for all data operations with the {@link SunshineDatabase}
 */
@Dao
public interface WeatherDao {

    @Query("SELECT id, weatherIconId, date, min, max FROM weather WHERE date >= :date")
    LiveData<List<ListWeatherEntry>> getCurrentWeatherForecasts(Date date);

    @Query("SELECT COUNT(id) FROM weather WHERE date >= :date")
    int countAllFutureWeather(Date date);

    @Query("SELECT * FROM weather WHERE date = :date")
    LiveData<Weather> getWeatherByDate(Date date);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(Weather... weather);

    @Query("DELETE FROM weather WHERE date < :date")
    void deleteOldWeather(Date date);
}
