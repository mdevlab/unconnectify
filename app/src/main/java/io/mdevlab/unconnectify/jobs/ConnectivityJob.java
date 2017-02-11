package io.mdevlab.unconnectify.jobs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import io.mdevlab.unconnectify.connectivitymodels.Wifi;

/**
 * Created by mdevlab on 2/10/17.
 */

public class ConnectivityJob extends Job {

    public static final String TAG = "wifi";
    private Context mContext;

    public ConnectivityJob(Context context) {
        this.mContext = context;
    }

    @Override
    @NonNull
    protected Result onRunJob(Params params) {

        Wifi.getInstance(mContext).disable();
        return Result.SUCCESS;
    }

    public static JobRequest buildJobRequest() {
        return new JobRequest.Builder(ConnectivityJob.TAG)
                .setExact(50L)
                .setPersisted(true)
                .build();
    }
}
