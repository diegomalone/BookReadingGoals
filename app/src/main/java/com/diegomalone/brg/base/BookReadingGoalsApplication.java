package com.diegomalone.brg.base;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.FirebaseDatabase;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class BookReadingGoalsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Fabric.with(this, new Crashlytics());

        Timber.plant(new Timber.DebugTree());
    }
}
