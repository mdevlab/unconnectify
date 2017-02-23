package io.mdevlab.unconnectify;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.evernote.android.job.JobManager;

import io.fabric.sdk.android.Fabric;
import io.mdevlab.unconnectify.jobs.ConnectivityJobCreator;

/**
 * Created by mdevlab on 2/10/17.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        JobManager.create(this).addJobCreator(new ConnectivityJobCreator(getApplicationContext()));
    }
}
