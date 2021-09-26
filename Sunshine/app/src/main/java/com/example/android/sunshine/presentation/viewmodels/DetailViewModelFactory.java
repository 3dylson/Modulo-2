package com.example.android.sunshine.presentation.viewmodels;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Date;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private final Application application;
    private final Date mDate;

    public DetailViewModelFactory(Application application, Date mDate) {
        this.application = application;
        this.mDate = mDate;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailActivityViewModel( application,mDate);
    }
}
