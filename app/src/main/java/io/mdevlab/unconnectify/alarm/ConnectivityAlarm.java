package io.mdevlab.unconnectify.alarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.mdevlab.unconnectify.utils.Connection;

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

public class ConnectivityAlarm {

    // Days to which the alarm is set
    protected List<Integer> mDays;

    // Time at which the alarm starts
    protected long mExecutionTimeInMils;

    // Connections concerned by the alarm
    protected List<Connection> mConnections;

    // Boolean defining whether or not the alarm is active/on
    protected boolean isActive;

    public ConnectivityAlarm() {
    }

    /**
     * Constructor taking one single argument: The execution time of the alarm
     * This is the most important information needed to set the alarm
     * The day of execution of the alarm is set by default to the current day
     * And by default the alarm once created is active
     *
     * @param mExecutionTimeInMils: The execution time of the alarm, in milliseconds
     */
    public ConnectivityAlarm(long mExecutionTimeInMils) {
        this.mExecutionTimeInMils = mExecutionTimeInMils;
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
    public ConnectivityAlarm(List<Connection> mConnections, List<Integer> mDays, long mExecutionTimeInMils) {
        this.mConnections = mConnections;
        this.mDays = mDays;
        this.mExecutionTimeInMils = mExecutionTimeInMils;
        this.isActive = true;
    }

    /**
     * Constructor taking as an argument another connectivityAlarm
     * The new connectivityAlarm that's being created will be a copy of the alarm passed
     * as an argument
     *
     * @param connectivityAlarm: An alarm on the base of which a new -the current- alarm will be created
     */
    public ConnectivityAlarm(ConnectivityAlarm connectivityAlarm) {
        this.mConnections = connectivityAlarm.getConnections();
        this.mExecutionTimeInMils = connectivityAlarm.getExecuteTimeInMils();
        this.mDays = connectivityAlarm.getDays();
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
}
