package io.mdevlab.unconnectify.alarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.mdevlab.unconnectify.utils.Connection;

/**
 * Class that represents one alarm.
 * An alarm is defined by these attributes:
 * - The exact time at which it is set to. This represents the time
 * at which the alarm is supposed to go off at
 * - The duration of the alarm
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

    private int mAlarmId;

    // Time at which the alarm starts
    private long mStartTime;

    /**
     * Time at which the alarm should be launch
     * The difference between it and 'mStartTime' is that 'mStartTime'
     * is static, it doesn't change, whereas 'mExecutionTimeInMils' changes
     * after every job.
     */
    private long mExecutionTimeInMils;

    // Duration of the alarm
    private long mDuration;

    // Days to which the alarm is set
    private List<Integer> mDays;

    // Connections concerned by the alarm
    private List<Connection> mConnections;

    // Boolean defining whether or not the alarm is active/on
    private boolean isActive;

    // Date of last update of the alarm
    private long mLastUpdate;

    // Id of the job assigned to the alarm
    private int jobId = -1;

    /**
     * Empty constructor
     */
    public PreciseConnectivityAlarm() {
    }

    /**
     * Constructor taking 4 arguments: The execution time, the duration, the days of
     * execution and the connection types concerned by the alarm.
     * By default the alarm is active once it's created
     *
     * @param mConnections:         Connections that will affected by the alarm
     * @param mDays:                Days to which the connection is set to, it mustn't be null
     * @param mExecutionTimeInMils: The execution time of the alarm
     * @param mDuration:            Duration after which the alarm should re-enable mConnections
     */
    public PreciseConnectivityAlarm(long mExecutionTimeInMils, long mDuration, List<Integer> mDays, List<Connection> mConnections) {
        this.mExecutionTimeInMils = mExecutionTimeInMils;
        this.mDuration = mDuration;
        this.mDays = mDays;
        this.mConnections = mConnections;
        this.isActive = true;
    }

    /**
     * Constructor taking 3 arguments: The execution time, the days of execution and
     * the connection types concerned by the alarm.
     * By default the alarm is active once it's created and its duration is 0
     *
     * @param mConnections:         Connections that will affected by the alarm
     * @param mDays:                Days to which the connection is set to, it mustn't be null
     * @param mExecutionTimeInMils: The execution time of the alarm
     */
    public PreciseConnectivityAlarm(long mExecutionTimeInMils, List<Integer> mDays, List<Connection> mConnections) {
        this(mExecutionTimeInMils, 0, mDays, mConnections);
    }

    /**
     * Constructor taking one single argument: The execution time of the alarm
     * This is the most important information needed to set the alarm
     * The duration of the alarm is 0 by default
     * The day of execution of the alarm is set by default to the current day
     * The connection by default set is Wifi
     * And by default the alarm once created is active
     *
     * @param mExecutionTimeInMils: The execution time of the alarm, in milliseconds
     */
    public PreciseConnectivityAlarm(long mExecutionTimeInMils) {
        this.mExecutionTimeInMils = mExecutionTimeInMils;
        this.mDuration = 0;
        this.mDays = getToday();
        this.mConnections = getDefaultConnection();
        this.isActive = true;
    }

    /**
     * Constructor taking as an argument another alarm
     * The new alarm object that's being created will be a copy of the alarm passed
     * as an argument
     *
     * @param alarm: An alarm on the base of which a new -the current- alarm will be created
     */
    public PreciseConnectivityAlarm(PreciseConnectivityAlarm alarm) {
        this(alarm.getExecuteTimeInMils(),
                alarm.getDuration(),
                alarm.getDays(),
                alarm.getConnections());
    }

    /**
     * Method that returns the default connection option set on the alarm's
     * creation.
     * For the moment the default connection is Wifi
     *
     * @return: A list containing the default connection option
     */
    private List<Connection> getDefaultConnection() {
        List<Connection> wifi = new ArrayList<>();
        wifi.add(Connection.WIFI);
        return wifi;
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
     * @return: Id of the alarm in conflict with the current alarm, -1 if no
     * conflict found
     */
    public int inConflictWithAlarm(PreciseConnectivityAlarm alarm) {
        if (alarm != null) {

            // Determine if both alarms handle one or more same connection(s)
            if (hasSameConnectivity(alarm.getConnections()))
                return alarm.getAlarmId();

            // Determine if both alarms are set to the same day(s)
            if (isLaunchedOnSameDay(alarm.getDays()))
                return alarm.getAlarmId();

            // Determine if both alarms function in the same period
            if (isExecutedInTheSamePeriod(alarm.getExecuteTimeInMils(), alarm.getDuration()))
                return alarm.getAlarmId();
        }
        return -1;
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

    /**
     * Method that determines whether or not 2 connections function in the same period of time
     *
     * @param executionTime: Execution/ start time of the alarm
     * @param duration:      Duration of the alarm
     * @return
     */
    private boolean isExecutedInTheSamePeriod(long executionTime, long duration) {
        return (isInRange(executionTime, mExecutionTimeInMils + mDuration, executionTime + duration) ||
                isInRange(mExecutionTimeInMils, executionTime + duration, mExecutionTimeInMils + mDuration));
    }

    /**
     * Utility method used in the method 'isExecutedInTheSamePeriod'
     * It checks if a number is contained in a range
     *
     * @param lowerBound:   Lower bound of the range
     * @param middleMember: Number being checked if in range or not
     * @param upperBound:   Upper bound of the range
     * @return: Whether or not the middle number is in the range [lowerBound, upperBound]
     */
    private boolean isInRange(long lowerBound, long middleMember, long upperBound) {
        if (lowerBound <= middleMember && middleMember <= upperBound)
            return true;
        return false;
    }

    public int getAlarmId() {
        return mAlarmId;
    }

    public void setAlarmId(int alarmId) {
        this.mAlarmId = alarmId;
    }

    public long getStartTime() {
        return mStartTime;
    }

    public void setStartTime(long mStartTime) {
        this.mStartTime = mStartTime;
    }

    public long getExecuteTimeInMils() {
        return mExecutionTimeInMils;
    }

    public void setExecuteTimeInMils(long mExecutionTimeInMils) {
        this.mExecutionTimeInMils = mExecutionTimeInMils;
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

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long mDuration) {
        this.mDuration = mDuration;
    }

    public long getmLastUpdate() {
        return mLastUpdate;
    }

    public void setmLastUpdate(long mLastUpdate) {
        this.mLastUpdate = mLastUpdate;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    /**
     * Method that returns the last connection of the current alarm
     * The chain of connections is: Wifi -> Hotspot -> Bluetooth
     * So the last connection would be Bluetooth is the alarm handles bluetooth,
     * if not the last connection would be Hotspot it the alarm handles
     * Bluetooth, else the last connection is Wifi.
     *
     * @return
     */
    public Connection getLastConnectionOfAlarm() {
        if (mConnections.contains(Connection.BLUETOOTH))
            return Connection.BLUETOOTH;

        if (mConnections.contains(Connection.HOTSPOT))
            return Connection.HOTSPOT;

        return Connection.WIFI;
    }
}
