package io.mdevlab.unconnectify.jobs;

import com.evernote.android.job.JobRequest;
import com.evernote.android.job.util.support.PersistableBundleCompat;

import io.mdevlab.unconnectify.alarm.PreciseConnectivityAlarm;
import io.mdevlab.unconnectify.utils.Constants;

/**
 * Created by mdevlab on 2/11/17.
 */

public class ConnectivityJobManager {

    /**
     * Method that build the job request of an alarm's job
     *
     * @param tag:           Tag of the connection the job is going to handle
     * @param activate:      Determines whether to enable or disable the connection
     * @param executionTime: Milliseconds left before executing the job
     * @return: The built job request
     */
    public static JobRequest buildJobRequest(PreciseConnectivityAlarm alarm, String tag, boolean activate, long executionTime) {
        PersistableBundleCompat extras = new PersistableBundleCompat();
        extras.putBoolean(Constants.ACTIVATE_TAG, activate);
        JobRequest jobRequest = new JobRequest.Builder(tag)
                .setExact(executionTime)
                .setExtras(extras)
                .setPersisted(true)
                .build();

        // Set the job id to the alarm
        alarm.setJobId(jobRequest.getJobId());

        return jobRequest;
    }
}
