package com.example.android.sunshine.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.android.sunshine.data.database.SunshineDatabase;
import com.example.android.sunshine.data.network.SunshineRepository;

public class FetchWorker extends Worker {

    private final SunshineRepository mRepository;

    public FetchWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        SunshineDatabase database = SunshineDatabase.getInstance(context);
        this.mRepository = SunshineRepository.getInstance(database.weatherDao(), context);
    }

    @NonNull
    @Override
    public Result doWork() {

        mRepository.fetchWeather();

        return Result.success();
    }
}
