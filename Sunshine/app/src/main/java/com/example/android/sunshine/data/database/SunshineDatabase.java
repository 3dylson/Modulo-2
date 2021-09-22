package com.example.android.sunshine.data.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.android.sunshine.data.database.converter.DateConverter;
import com.example.android.sunshine.data.database.dao.WeatherDao;
import com.example.android.sunshine.data.database.entity.Weather;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * {@link SunshineDatabase} database for the application including a table for {@link Weather}
 * with the DAO {@link WeatherDao}.
 */

// List of the entry classes and associated TypeConverters
@Database(entities = {Weather.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class SunshineDatabase extends RoomDatabase {

    private static final String LOG_TAG = SunshineDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "weather";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static SunshineDatabase INSTANCE;

    public static SunshineDatabase getInstance(Context context) {
        Log.d(LOG_TAG, "Getting the database");
        if (INSTANCE == null) {
            synchronized (LOCK) {
                INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        SunshineDatabase.class,
                        SunshineDatabase.DATABASE_NAME)
                        .build();
                Log.d(LOG_TAG, "Made new database");
            }
        }
        return INSTANCE;
    }

    // The associated DAOs for the database
    public abstract WeatherDao weatherDao();

    private ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(2);

}
