package io.mdevlab.unconnectify.alarm;

import java.util.List;

import io.mdevlab.unconnectify.utils.AlarmUtils;
import io.mdevlab.unconnectify.utils.Connection;
import io.mdevlab.unconnectify.utils.DateUtils;

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

    // Boolean defining the current state of connection within the Alarm
    //if True then ON
    //if False then OFF
    protected boolean mCurrentState;

    // Date of last update to the alarm
    private long mLastUpdate;

    // Id of the job assigned to the alarm
    private int mJobId = -1;

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
        this.mStartTime = mExecutionTimeInMils;
        this.mDuration = mDuration;
        this.mDays = mDays;
        this.mConnections = mConnections;
        this.isActive = true;
        this.mCurrentState = false;
        this.mExecutionTimeInMils = AlarmUtils.getAlarmExecutionTime(this);
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
        this(mExecutionTimeInMils, 1);
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
     * Constructor taking two arguments: The execution time of the alarm and its duration
     * The day of execution of the alarm is set by default to the current day
     * And by default the alarm once created is active
     *
     * @param mExecutionTimeInMils
     * @param mDuration
     */
    public PreciseConnectivityAlarm(long mExecutionTimeInMils, int mDuration) {
        this.mStartTime = mExecutionTimeInMils;
        this.mDuration = mDuration;
        this.mDays = DateUtils.getToday();
        this.mConnections = AlarmUtils.getDefaultConnection();
        this.isActive = true;
        this.mCurrentState = false;
        this.mExecutionTimeInMils = AlarmUtils.getAlarmExecutionTime(this);
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
    public boolean inConflictWithAlarm(PreciseConnectivityAlarm alarm, Connection connection) {
        if (alarm != null) {

            // Determine if both alarms handle one or more same connection(s)
            if (hasSameConnectivity(connection))
                return true;

            // Determine if both alarms are set to the same day(s)
            if (isBeingLaunchedToday(alarm.getDays()))
                return true;

            // Determine if both alarms function in the same period
            if (isExecutedInTheSamePeriod(alarm.getLastUpdate(), alarm.getExecuteTimeInMils(), alarm.getDuration()))
                return true;
        }
        return false;
    }

    /**
     * Method that determines whether or not the current alarm object
     * and another alarm object handle the same connections
     *
     * @param connection: Connection list of the alarm object
     * @return
     */
    private boolean hasSameConnectivity(Connection connection) {
        return mConnections.contains(connection);
    }

    /**
     * Method that determines whether or not the current alarm object
     * and another alarm object are both set to be launched on the current
     * day
     *
     * @param daysList: List of days in which the other alarm is set to
     * @return
     */
    private boolean isBeingLaunchedToday(List<Integer> daysList) {
        return daysList.contains(DateUtils.getToday().get(0));
    }

    /**
     * Method that determines whether or not 2 connections function in the same period of time
     *
     * @param executionTime: Execution/ start time of the alarm
     * @param duration:      Duration of the alarm
     * @return
     */
    private boolean isExecutedInTheSamePeriod(long lastUpdate, long executionTime, long duration) {


        /**
         * To know the exact time on which an alarm is launched, we get it using
         * the formula : lastUpdate + executionTime
         *
         * Two alarms are in conflict when either one of them is launched between
         * the execution time of the other one and its end time
         */
        return (isInRange(lastUpdate + executionTime,
                mLastUpdate + mExecutionTimeInMils + mDuration,
                lastUpdate + executionTime + duration) ||
                isInRange(mLastUpdate + mExecutionTimeInMils,
                        lastUpdate + executionTime + duration,
                        mExecutionTimeInMils + mDuration)
        );
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


    public boolean getCurrentState() {
        return mCurrentState;
    }

    public void setCurrentState(boolean currentState) {
        this.mCurrentState = currentState;
    }

    public long getLastUpdate() {
        return mLastUpdate;
    }

    public void setLastUpdate(long mLastUpdate) {
        this.mLastUpdate = mLastUpdate;
    }

    public int getJobId() {
        return mJobId;
    }

    public void setJobId(int jobId) {
        this.mJobId = jobId;
    }

    /**
     * Method that returns the last connection of the current alarm
     * The chain of connections is: Wifi -> Hotspot -> Bluetooth
     * So the last connection would be Bluetooth if the alarm handles bluetooth,
     * if not the last connection would be Hotspot it the alarm handles
     * hotspot, else the last connection is Wifi.
     *
     * @return
     */
    public Connection getLastConnection() {
        if (mConnections.contains(Connection.BLUETOOTH))
            return Connection.BLUETOOTH;

        if (mConnections.contains(Connection.HOTSPOT))
            return Connection.HOTSPOT;

        return Connection.WIFI;
    }

    /**
     * Method that returns the first connection of the current alarm
     * The chain of connections is: Wifi -> Hotspot -> Bluetooth
     * So the first connection would be Wifi if the alarm handles wifi,
     * if not the first connection would be Hotspot it the alarm handles
     * hotspot, else the first connection is Bluetooth.
     *
     * @return
     */
    public Connection getFirstConnection() {
        if (mConnections.contains(Connection.WIFI))
            return Connection.WIFI;

        if (mConnections.contains(Connection.HOTSPOT))
            return Connection.HOTSPOT;

        return Connection.BLUETOOTH;
    }
}
