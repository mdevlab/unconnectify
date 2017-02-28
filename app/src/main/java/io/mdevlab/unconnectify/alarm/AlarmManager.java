package io.mdevlab.unconnectify.alarm;

import android.content.Context;

import com.evernote.android.job.JobManager;

import java.util.List;

import io.mdevlab.unconnectify.data.AlarmSqlHelper;
import io.mdevlab.unconnectify.jobs.ConnectivityJobManager;
import io.mdevlab.unconnectify.notification.AlarmNotificationManager;
import io.mdevlab.unconnectify.utils.AlarmUtils;
import io.mdevlab.unconnectify.utils.Connection;

/**
 * Created by mdevlab on 2/10/17.
 */

public class AlarmManager {

    private static Context mContext;
    private static AlarmManager instance = null;
    private static AlarmSqlHelper alarmSqlHelper = null;

    private AlarmManager(Context context) {
        this.mContext = context;
        alarmSqlHelper = new AlarmSqlHelper(context);
    }

    public static AlarmManager getInstance(Context context) {
        if (instance == null)
            instance = new AlarmManager(context);
        return instance;
    }

    /**
     * creating an alarm consists of:
     * - Saving the alarm in the local database
     * - Launching whatever necessary jobs
     *
     * @param alarm
     */
    public long createAlarm(PreciseConnectivityAlarm alarm) {

        // Saving the alarm to the local database using the sql helper
        long alarmId = alarmSqlHelper.createAlarm(alarm);

        // Launching alarm job
        alarm.setAlarmId((int) alarmId);
        createAlarmJob(alarm);

        return alarmId;
    }

    /**
     * Method that sets the first job for an alarm right after its creation
     * It's also called to update an alarm's job after this alarm has been modified
     * So it starts by canceling the previous job first before creating a new one
     *
     * @param alarm: The newly created alarm
     */
    private void createAlarmJob(PreciseConnectivityAlarm alarm) {
        createAlarmJob(alarm, false);
    }

    /**
     * Method that sets the first job for an alarm right after its creation
     * It's also called to update an alarm's job after this alarm has been modified
     * So it starts by canceling the previous job first before creating a new one
     *
     * @param alarm: The newly created alarm
     */
    private void createAlarmJob(PreciseConnectivityAlarm alarm, boolean activate) {

        // Cancel previous job assigned to alarm if it exists
        if (alarm.getJobId() != -1)
            JobManager.instance().cancel(alarm.getJobId());

        ConnectivityJobManager.buildJobRequest(alarm, AlarmUtils.getStringFromConnection(alarm.getFirstConnection()),
                activate,
                alarm.getExecuteTimeInMils());

        alarmSqlHelper.updateAlarmJob(alarm.getAlarmId(), alarm.getJobId());
    }

    /**
     * This method is for deleting an alarm from the database
     *
     * @param alarmId id of the alarm
     * @return true if deleted, false otherwise
     */
    public Boolean clearAlarm(int alarmId) {

        // Cancel the job assigned to the alarm being deleted
        cancelAlarmJob(alarmSqlHelper.getAlarmById(alarmId));

        // Deleting the alarm from the local database using the sql helper
        int lines = alarmSqlHelper.deleteAlarm(alarmId);

        if (lines > 0) {
            return true;
        }

        return false;
    }

    /**
     * Method that cancels an alarm's job
     *
     * @param alarm: Alarm of which we want to cancel the job
     */
    private void cancelAlarmJob(PreciseConnectivityAlarm alarm) {

        // If the alarm has a job, cancel it
        if (alarm.getJobId() != -1) {
            alarm.setJobId(-1);
            alarmSqlHelper.updateAlarmJob(alarm.getAlarmId(), -1);
            JobManager.instance().cancel(alarm.getJobId());
        }
    }

    /**
     * Method that updates an alarm's state (on/off)
     *
     * @param alarm:    Alarm object whose state is being changed
     * @param isActive: New state of the alarm, either on (true) or off (false)
     */
    public void updateAlarmState(PreciseConnectivityAlarm alarm, boolean isActive) {

        // Update the state of the alarm object and in the dababase
        alarm.setActive(isActive);
        alarm.setCurrentState(isActive);
        alarmSqlHelper.updateAlarmCurrentState(alarm.getAlarmId(), isActive);

        // If alarm is now active, create its job
        if (isActive) {
            createAlarmJob(alarm);
        }

        // Else, cancel its current running job
        else
            cancelAlarmJob(alarm);
    }

    /**
     * Method that updates the alarm's execution time and duration
     *
     * @param alarm:         The alarm being updated
     * @param executionTime: New execution time to be assigned to the alarm being updated
     * @param alarmDuration: New duration to be assigned to the alarm being updated
     */
    public void updateAlarm(PreciseConnectivityAlarm alarm, long executionTime, long alarmDuration) {

        // Update the alarm object
        if (executionTime != alarm.getExecuteTimeInMils()) {
            alarm.setStartTime(executionTime);
            alarm.setExecuteTimeInMils(AlarmUtils.getAlarmExecutionTime(alarm));
        } else
            alarm.setDuration(alarmDuration);

        // Update the alarm in the database
        alarmSqlHelper.updateAlarm(alarm.getAlarmId(),
                alarm.getStartTime(),
                alarm.getExecuteTimeInMils(),
                alarm.getDuration());

        /**
         * Create new job for the alarm
         * 'activate' indicates whether to enable or disable the connections in the job
         * If executionTime = startTime + duration, it means the next job is
         * supposed to re-enable the connections, this is why the value of 'activate'
         * becomes true.
         */
        boolean activate = false;
        if (alarm.getExecuteTimeInMils() == alarm.getStartTime() + alarm.getDuration())
            activate = true;
        createAlarmJob(alarm, activate);
    }

    /**
     * Method that updates the alarm's connections
     *
     * @param alarmId:            Id of the alarm being updated
     * @param selectedConnection: Selected/unselected connection to be attributed to the alarm being updated
     * @param isActive:           State of the selected connection
     */
    public void updateAlarmConnection(int alarmId, Connection selectedConnection, boolean isActive) {
        PreciseConnectivityAlarm alarm = alarmSqlHelper.getAlarmById(alarmId);

        // Update the alarm object
        if (isActive)
            alarm.getConnections().add(selectedConnection);
        else
            alarm.getConnections().remove(selectedConnection);

        // Update the alarm in the database
        alarmSqlHelper.updateAlarmConnection(alarmId, selectedConnection, isActive);

        // Create a new job
        createAlarmJob(alarm);
    }

    /**
     * Method that updates the alarm's days
     *
     * @param alarmId:     Id of the alarm being updated
     * @param selectedDay: Selected/unselected day to be attributed to the alarm being updated
     * @param isActive:    State of the selected day
     */
    public void updateAlarmDay(int alarmId, int selectedDay, boolean isActive) {
        PreciseConnectivityAlarm alarm = alarmSqlHelper.getAlarmById(alarmId);

        // Update the alarm object
        if (isActive)
            alarm.getDays().add(selectedDay);
        else
            alarm.getDays().remove(Integer.valueOf(selectedDay));

        // Update the alarm in the database
        alarmSqlHelper.updateAlarmDay(alarmId, selectedDay, isActive);

        // Create a new job
        createAlarmJob(alarm);
    }

    /**
     * Method that handles conflicts between alarms
     * This method checks whether its argument, an alarm is
     * in conflict with another alarm that already exists
     * A conflict between two alarms happens when all these conditions are met:
     * - both alarms run in the same day (or more than one day)
     * - At least one connection model is handled by both alarms
     * - The period of "work" of one alarm intersect with that of the other
     *
     * @param alarm: Alarm we want to check if in conflict with other alarms
     * @return: Id of the conflicting alarm's id if a conflict is found, -1 otherwise
     */
    public int handleAlarmConflicts(PreciseConnectivityAlarm alarm, Connection connection) {
        if (alarmSqlHelper != null) {

            // Get a list of all precise active alarms from the database
            List<PreciseConnectivityAlarm> activeAlarms = alarmSqlHelper.readAllActiveAlarms();

            /**
             * Loop on the elements of the alarm
             * The alarm passed on as an argument is compared with each element of the list
             * If a conflict is found, the conflicting alarm's Id is returned
             */
            for (PreciseConnectivityAlarm activeAlarm : activeAlarms) {

                // If the two alarms are in conflict, return the conflicting alarm's Id
                if (alarm.getAlarmId() != activeAlarm.getAlarmId())
                    if (alarm.inConflictWithAlarm(activeAlarm, connection))
                        return activeAlarm.getAlarmId();
            }
        }

        // At this point there aren't any conflicts, -1 is returned
        return -1;
    }

    public void handleNotification() {
        AlarmNotificationManager.triggerNotification(mContext);
    }
}
