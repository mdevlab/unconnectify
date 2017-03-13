package io.mdevlab.unconnectify.jobs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.mdevlab.unconnectify.alarm.AlarmManager;
import io.mdevlab.unconnectify.alarm.PreciseConnectivityAlarm;
import io.mdevlab.unconnectify.connectivitymodels.Bluetooth;
import io.mdevlab.unconnectify.connectivitymodels.Connectivity;
import io.mdevlab.unconnectify.connectivitymodels.ConnectivityFactory;
import io.mdevlab.unconnectify.connectivitymodels.Hotspot;
import io.mdevlab.unconnectify.connectivitymodels.Wifi;
import io.mdevlab.unconnectify.data.AlarmSqlHelper;
import io.mdevlab.unconnectify.utils.AlarmUtils;
import io.mdevlab.unconnectify.utils.Connection;
import io.mdevlab.unconnectify.utils.Constants;
import io.mdevlab.unconnectify.utils.DateUtils;

import static io.mdevlab.unconnectify.utils.AlarmUtils.getConnectionFromString;

/**
 * Created by mdevlab on 2/10/17.
 */

public class ConnectivityJob extends Job {

    private Context mContext;

    // Tag of the connection the current job is handling
    private String mTag;

    // Alarm sql helper object, used for database calls
    private AlarmSqlHelper mAlarmSqlHelper;

    // Current alarm to which the current job is assigned
    private PreciseConnectivityAlarm mCurrentAlarm;

    /**
     * Boolean that determines whether to switch from enabling connections
     * to disabling them, or vise versa
     */
    private boolean switchActivationState;

    /**
     * Constructor that initializes the context, tag and sql helper
     *
     * @param context
     * @param tag:    String indicating the type of connectivity
     */
    public ConnectivityJob(Context context, String tag) {
        this.mContext = context;
        mAlarmSqlHelper = new AlarmSqlHelper(mContext);
        this.mTag = tag;
    }

    /**
     * Method called right after this class is instantiated
     * It basically does 3 things:
     * - Runs the current job (handling the current connection tag
     * defined in the global variable 'mTag')
     * - Updates the alarm execution time. When a job is being set to run,
     * the execution time of the alarm to which it is associated is used. so after
     * the job is ran it's only natural to update it and set the execution time of the
     * following job
     * - Prepare the launch of the next job
     *
     * @param params
     * @return
     */
    @Override
    @NonNull
    protected Result onRunJob(Params params) {

        // Set the alarm to which the current job is assigned
        mCurrentAlarm = mAlarmSqlHelper.getAlarmByJobId(params.getId());

        boolean activate = params.getExtras().getBoolean(Constants.ACTIVATE_TAG, false);

        // Run the current job
        runCurrentJob(activate);

        // Update the execution time of the next job
        updateAlarmExecutionTime(activate);

        // Run the next job
        prepareNextJob(activate);

        // Update notification for next alarm
        updateNextAlarmNotification();

        return Result.SUCCESS;
    }

    /**
     * Method that runs a single job
     * This job either enables or disables one of the connectivity options
     *
     * @param enableConnectivity: Boolean indicating whether to enable or disable the connectivity
     */
    private void runCurrentJob(boolean enableConnectivity) {
        // Connectivity base object, can be wifi, hotspot or bluetooth
        Connectivity connectivity = ConnectivityFactory.getConnectivity(AlarmUtils.getConnectionFromString(mTag), mContext);

        // check whether for the current connection, the alarm is in conflict with another alarm
        int conflictAlarmId = AlarmManager.getInstance(mContext).handleAlarmConflicts(mCurrentAlarm, AlarmUtils.getConnectionFromString(mTag));

        /**
         * If the id is equal to -1, it means there wasn't any conflict
         * And so the current alarm's job goes on
         */
        if (conflictAlarmId == -1) {
            executeCurrentJob(connectivity, enableConnectivity);
        }

        /**
         * If the id isn't equal to -1, it means there is a conflict
         * In this case we execute the latest one
         */
        else {
            PreciseConnectivityAlarm conflictAlarm = mAlarmSqlHelper.getAlarmById(conflictAlarmId);
            if (conflictAlarm.getLastUpdate() < mCurrentAlarm.getLastUpdate())
                executeCurrentJob(connectivity, enableConnectivity);
        }
    }

    private void executeCurrentJob(Connectivity connectivity, boolean enableConnectivity) {
        // If it's wifi, enable/disable wifi
        if (connectivity instanceof Wifi)
            enableWifi(enableConnectivity);

            // If it's hotspot, enable/disable hotspot
        else if (connectivity instanceof Hotspot)
            enableHotspot(enableConnectivity);

            // If it's bluetooth, enable/disable bluetooth
        else if (connectivity instanceof Bluetooth)
            enableBluetooth(enableConnectivity);
    }

    /**
     * Method that enables/disables wifi depending on the boolean variable passed on to it
     *
     * @param activateWifi
     */
    private void enableWifi(boolean activateWifi) {
        if (activateWifi)
            Wifi.getInstance(mContext).enable();
        else
            Wifi.getInstance(mContext).disable();
    }

    /**
     * Method that enables/disables hotspot depending on the boolean variable passed on to it
     *
     * @param activateHotspot
     */
    private void enableHotspot(boolean activateHotspot) {
        if (activateHotspot)
            Hotspot.getInstance(mContext).enable();
        else
            Hotspot.getInstance(mContext).disable();
    }

    /**
     * Method that enables/disables bluetooth depending on the boolean variable passed on to it
     *
     * @param activateBluetooth
     */
    private void enableBluetooth(boolean activateBluetooth) {
        if (activateBluetooth)
            Bluetooth.getInstance().enable();
        else
            Bluetooth.getInstance().disable();
    }

    /**
     * Method that updates the execution time of an alarm
     * When a job is launched, it takes the execution time from the alarm
     * object it's assigned to.
     * The execution time represents the number of milliseconds left until the
     * job is ran.
     * Its minimum value is 1.
     * This method first checks the latest handled connectivity:
     * -- If it's the last connectivity to handle, The duration of the alarm is checked.
     * ------ If it's equal to 1, then we know that the alarm doesn't have an ending time,
     * it was meant to only disable the connections at a certain time. So We set the next
     * job to the next day in the chain of the alarm.
     * ------ If the duration isn't equal to 1, we set the next execution time to when the
     * alarm should re-enable the connections.
     * -- If it isn't the last one in the chain, another connection will need to be handled
     * by this job right away, so the execution time is set to 1
     */
    private void updateAlarmExecutionTime(boolean activate) {

        // Variable that's going to hold the value of the nex execution time
        long newExecutionTime;

        if (isLastConnectivity(mTag)) {
            long alarmDuration = mCurrentAlarm.getDuration();

            // If alarmDuration is equal to 1, the next launch of the alarm is set for another day
            if (alarmDuration == 1) {
                int numberOfDaysUntilNextAlarm = AlarmUtils.getNumberOfDaysUntilNextAlarm(mCurrentAlarm, true);
                newExecutionTime = TimeUnit.DAYS.toMillis(numberOfDaysUntilNextAlarm);
                switchActivationState = false;
            } else {
                switchActivationState = true;
                if (activate)
                    newExecutionTime = getTimeOfNextDayAlarm();
                else
                    newExecutionTime = mCurrentAlarm.getDuration();
            }
        } else {
            newExecutionTime = 1L;
            switchActivationState = false;
        }

        mCurrentAlarm.setExecuteTimeInMils(newExecutionTime);
        mAlarmSqlHelper.updateAlarm(mCurrentAlarm.getAlarmId(),
                mCurrentAlarm.getStartTime(),
                newExecutionTime,
                mCurrentAlarm.getDuration());
    }

    /**
     * Method that returns whether or not this connection is the last that will be
     * handled by the job or not.
     *
     * @param connectionTag: Tag identifying the connection type
     * @return: Whether or not the connection type is the last in the chain
     * of connections to be handled by the alarm
     */
    private boolean isLastConnectivity(String connectionTag) {
        return getConnectionFromString(connectionTag) == mCurrentAlarm.getLastConnection();
    }

    /**
     * Method that prepares the next job
     * It simply gets the next connection, determines whether to enable
     * or disable it and then build the request for the new job
     *
     * @param activateNextConnection
     */
    private void prepareNextJob(boolean activateNextConnection) {

        // Getting the tag for the next connection to be handled
        String nextTag = getNextConnectivityTag(mTag);

        // Determine whether to activate or deactivate connection
        if (switchActivationState)
            activateNextConnection = !activateNextConnection;

        // Build request for new job
        ConnectivityJobManager.buildJobRequest(mCurrentAlarm,
                nextTag,
                activateNextConnection,
                mCurrentAlarm.getExecuteTimeInMils());
    }

    /**
     * Method that updates the displayed notification with the latest alarm info
     */
    private void updateNextAlarmNotification() {
        AlarmManager.getInstance(mContext).handleNotification();
    }

    /**
     * Method that returns the next connection the job should handle
     *
     * @param connectionTag: Tag of the connection that's just been handled
     * @return: Next connection the alarm's job should handle
     */
    private String getNextConnectivityTag(String connectionTag) {

        // List of the connections of the current alarm
        List<Connection> currentAlarmConnections = mCurrentAlarm.getConnections();

        /**
         * For each connection, we check if the connection list of the alarm contains
         * the connection that follows it, if it does we return that connection tag,
         * if not we do a recursive call to this method giving it this time the tag
         * of the following connectivity tag (that wasn't found in the connectivity list
         * of the alarm)
         */
        switch (connectionTag) {

            case Constants.WIFI_TAG:
                if (currentAlarmConnections.contains(Connection.HOTSPOT))
                    return Constants.HOTSPOT_TAG;
                else
                    return getNextConnectivityTag(Constants.HOTSPOT_TAG);

            case Constants.HOTSPOT_TAG:
                if (currentAlarmConnections.contains(Connection.BLUETOOTH))
                    return Constants.BLUETOOTH_TAG;
                else
                    return getNextConnectivityTag(Constants.BLUETOOTH_TAG);

            case Constants.BLUETOOTH_TAG:
                if (currentAlarmConnections.contains(Connection.WIFI))
                    return Constants.WIFI_TAG;
                else
                    return getNextConnectivityTag(Constants.WIFI_TAG);

            default:
                return mTag;
        }
    }

    /**
     * @return: Time in milliseconds of the next launch of the alarm on another day
     */
    private long getTimeOfNextDayAlarm() {

        // Start time of the alarm as HH:mm
        String startTime = DateUtils.getTimeFromLong(mCurrentAlarm.getStartTime());

        // The minutes and hours of the start time of the alarm
        int startTimeMinute = Integer.parseInt(startTime.split(":")[0]);
        int startTimeHour = Integer.parseInt(startTime.split(":")[1]);

        // Start time in milliseconds of the alarm on the next day
        long startTimeOnTheNextDay = TimeUnit.MINUTES.toMillis(startTimeMinute) + TimeUnit.HOURS.toMillis(startTimeHour);

        /**
         * Days in milliseconds until the next alarm launch
         * We deduct 1 from the result because the result is going to be summed with
         * the start time on the following day, so one day (or part of it at least)
         * has already been consumed
         */
        long daysUntilNextAlarm = TimeUnit.DAYS.toMillis(AlarmUtils.getNumberOfDaysUntilNextAlarm(mCurrentAlarm, true) - 1);

        return startTimeOnTheNextDay + daysUntilNextAlarm;
    }
}
