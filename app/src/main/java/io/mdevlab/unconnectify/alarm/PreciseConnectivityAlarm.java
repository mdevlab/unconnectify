package io.mdevlab.unconnectify.alarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.mdevlab.unconnectify.utils.Connection;

/**
 * Class that extends the 'ConnectivityAlarm' class, and thus inherets
 * the following attributes:
 * - The exact time at which it is set to
 * - The days to which the alarm is set
 * - The connections the alarm is supposed to enable or disable
 * - The state of the alarm
 * On top of these attributes, this class adds the duration attribute which
 * defines when the alarm ends.
 * <p>
 * Created by mdevlab on 2/10/17.
 */

/**
 * Class that represents one alarm.
 * An alarm is defined by 4 attributes:
 * - The exact time at which it is set to. This represents the time
 * at which the alarm is supposed to go off at
 * - The days to which the alarm is set. These days are represented
 * by the days of the week (Monday, Tuesday, ...)
 * - The connections the alarm is supposed to enable or disable. So far
 * 3 connectivity options are handled: Wifi, Hotspot and Bluetooth
 * - The state of the alarm: Meaning whether the alarm is on or off,
 * active or inactive
 * <p>
 * Created by mdevlab on 2/10/17.
 */

public class PreciseConnectivityAlarm {

    // Days to which the alarm is set
    protected int mAlarmId;


    // Days to which the alarm is set
    protected List<Integer> mDays;

    // Time at which the alarm starts
    protected long mExecutionTimeInMils;

    // Connections concerned by the alarm
    protected List<Connection> mConnections;

    // Boolean defining whether or not the alarm is active/on
    protected boolean isActive;

    // Duration of the alarm
    private long mDuration;

    public PreciseConnectivityAlarm(){}

    /**
     * Constructor taking one single argument: The execution time of the alarm
     * This is the most important information needed to set the alarm
     * The day of execution of the alarm is set by default to the current day
     * And by default the alarm once created is active
     *
     * @param mExecutionTimeInMils: The execution time of the alarm, in milliseconds
     */
    public PreciseConnectivityAlarm(long mExecutionTimeInMils) {
        this.mExecutionTimeInMils = mExecutionTimeInMils;
        this.mDuration = 0;
        this.mDays = getToday();
        this.isActive = true;
    }

    /**
     * Constructor taking 3 arguments: The execution time, the days of execution and
     * the connection types concerned by the alarm.
     * By default the alarm is active once it's created
     *
     * @param mConnections:         Connections that will affected by the alarm
     * @param mDays:                Days to which the connection is set to, it mustn't be null
     * @param mExecutionTimeInMils: The execution time of the alarm
     */
    public PreciseConnectivityAlarm(List<Connection> mConnections, List<Integer> mDays, long mExecutionTimeInMils) {
        this.mConnections = mConnections;
        this.mDays = mDays;
        this.mExecutionTimeInMils = mExecutionTimeInMils;
        this.mDuration = 0;
        this.isActive = true;
    }

    /**
     * Constructor taking as an argument another connectivityAlarm
     * The new connectivityAlarm that's being created will be a copy of the alarm passed
     * as an argument
     *
     * @param alarm: An alarm on the base of which a new -the current- alarm will be created
     */
    public PreciseConnectivityAlarm(PreciseConnectivityAlarm alarm) {
        this.mConnections = alarm.getConnections();
        this.mExecutionTimeInMils = alarm.getExecuteTimeInMils();
        this.mDuration = alarm.getDuration();
        this.mDays = alarm.getDays();
        this.isActive = true;
    }

    /**
     * Method that returns the current day of the week (Monday, Tuesday, ...)
     * This method uses the predefined days of the week defined in the class Calendar
     *
     * @return: The current day of the week
     */
    private List<Integer> getToday() {
        List<Integer> today = new ArrayList<>();
        today.add(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        return today;
    }

    /**
     * Constructor taking 4 arguments: The execution time, the days of execution, the
     * connection types concerned by the alarm and the duration of the alarm.
     * By default the alarm is active once it's created
     *
     * @param mConnections:         Connections that will affected by the alarm
     * @param mDays:                Days to which the connection is set to, it mustn't be null
     * @param mExecutionTimeInMils: The execution time of the alarm
     * @param mDuration:            Duration after which the alarm should re-enable mConnections
     */
    public PreciseConnectivityAlarm(List<Connection> mConnections, List<Integer> mDays, long mExecutionTimeInMils, int mDuration) {
        this.mConnections = mConnections;
        this.mExecutionTimeInMils = mExecutionTimeInMils;
        this.mDuration = mDuration;
        this.mDays = mDays;
        this.isActive = true;
    }

    /**
     * Constructor taking two arguments: The execution time of the alarm and its duration
     * The day of execution of the alarm is set by default to the current day
     * And by default the alarm once created is active
     *
     * @param mExecutionTimeInMils
     * @param mDuration
     */
    public PreciseConnectivityAlarm(long mExecutionTimeInMils, int mDuration) {
        this.mExecutionTimeInMils = mExecutionTimeInMils;
        this.mDuration = mDuration;
        this.mDays = getToday();
        this.isActive = true;
    }

    /**
     * Method that checks whether the current alarm object is in
     * conflict with another alarm passed on as an argument
     *
     * @param alarm: Alarm with which the current alarm is compared
     *               to determine if a conflict between the two exists
     * @return: Whether or not the current alarm is in conflict with the
     * alarm passed on as an argument
     */
    public boolean inConflictWithAlarm(PreciseConnectivityAlarm alarm) {
        if (alarm != null) {
            // Determine if both alarms handle one or more same connection(s)
            if (hasSameConnectivity(alarm.getConnections()))
                return true;

            // Determine if both alarms are set to the same day(s)
            if (isLaunchedOnSameDay(alarm.getDays()))
                return true;

            // Determine if both alarms function in the same period
            if (isExecutedInTheSamePeriod(alarm.getExecuteTimeInMils(), alarm.getDuration()))
                return true;
        }
        return false;
    }

    /**
     * Method that determines whether or not the current alarm object
     * and another alarm object handle the same connections
     *
     * @param connectionlist: Connection list of the alarm object
     * @return
     */
    private boolean hasSameConnectivity(List<Connection> connectionlist) {
        for (Connection connection : connectionlist)
            // Checking whether 'connenction' is contained in the current alarm's object connections
            if (mConnections.contains(connection))
                return true;
        return false;
    }

    /**
     * Method that determines whether or not the current alarm object
     * and another alarm object are set to the same day(s)
     *
     * @param daysList: List of days in which the other alarm is set to
     * @return
     */
    private boolean isLaunchedOnSameDay(List<Integer> daysList) {
        for (Integer day : daysList)
            // Checking whether 'day' is contained in the current alarm's object days of execution
            if (mDays.contains(day))
                return true;
        return false;
    }

    private boolean isExecutedInTheSamePeriod(long executionTime, long duration) {
//        if( (executionTime <= (mExecutionTimeInMils + mDuration)) &&
//                ())
//            return false;
        return false;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public List<Connection> getConnections() {
        return mConnections;
    }

    public void setConnections(List<Connection> mConnections) {
        this.mConnections = mConnections;
    }

    public List<Integer> getDays() {
        return mDays;
    }

    public void setDays(List<Integer> mDays) {
        this.mDays = mDays;
    }

    public long getExecuteTimeInMils() {
        return mExecutionTimeInMils;
    }

    public void setExecuteTimeInMils(long mExecutionTimeInMils) {
        this.mExecutionTimeInMils = mExecutionTimeInMils;
    }

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long mDuration) {
        this.mDuration = mDuration;
    }


    public void setAlarmId(int alarmId) {
        this.mAlarmId = alarmId;
    }
}