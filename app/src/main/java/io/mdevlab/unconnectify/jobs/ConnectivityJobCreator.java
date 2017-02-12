package io.mdevlab.unconnectify.jobs;

import android.content.Context;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by mdevlab on 2/10/17.
 */

public class ConnectivityJobCreator implements JobCreator {

    private Context mContext;

    public ConnectivityJobCreator(Context context) {
        this.mContext = context;
    }

    @Override
    public Job create(String tag) {
        return new ConnectivityJob(mContext, tag);
    }
}
