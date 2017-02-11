package io.mdevlab.unconnectify.jobs;

import android.content.Context;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by mdevlab on 2/10/17.
 */

public class ConnectivityJobCreator implements JobCreator {

    public static final String WIFI = "wifi";
    public static final String CELLULAR_DATA = "cellular data";
    public static final String HOTSPOT = "hotspot";
    public static final String BLUETOOTH = "bluetooth";

    private Context mContext;

    public ConnectivityJobCreator(Context context) {
        this.mContext = context;
    }

    @Override
    public Job create(String tag) {
        switch (tag) {
            case WIFI:
                return new ConnectivityJob(mContext);
            default:
                return null;
        }
    }
}
