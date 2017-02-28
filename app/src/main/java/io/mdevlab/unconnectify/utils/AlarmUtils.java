package io.mdevlab.unconnectify.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.mdevlab.unconnectify.alarm.PreciseConnectivityAlarm;

/**
 * Created by mdevlab on 2/11/17.
 */

public class AlarmUtils {

    /**
     * Method that gets the connection enum value from a string
     *
     * @param tag: String value for a connection option
     * @return
     */
    public static Connection getConnectionFromString(String tag) {
        switch (tag) {
            case Constants.WIFI_TAG:
                return Connection.WIFI;
            case Constants.CELLULAR_DATA_TAG:
                return Connection.CELLULAR_DATA;
            case Constants.HOTSPOT_TAG:
                return Connection.HOTSPOT;
            case Constants.BLUETOOTH_TAG:
                return Connection.BLUETOOTH;
            default:
                return null;
        }
    }

    /**
     * Method that gets the string value of a connection enum
     *
     * @param connection: Connection object
     * @return
     */
    public static String getStringFromConnection(Connection connection) {
        if (connection == Connection.WIFI)
            return Constants.WIFI_TAG;

        if (connection == Connection.HOTSPOT)
            return Constants.HOTSPOT_TAG;

        if (connection == Connection.BLUETOOTH)
            return Constants.BLUETOOTH_TAG;

        return "";
    }

    /**
     * Debugging method that displays all an alarm's information
     *
     * @param alarm:    Alarm object being debugged
     * @param function: Function calling this method
     */
    public static void displayAlarm(PreciseConnectivityAlarm alarm, String function) {
        Log.e("Alarm display", function);

        // Start time
        Log.e("Alarm display", "Start time = " + alarm.getStartTime() + " which is at " + DateUtils.getTimeFromLong(alarm.getStartTime()));

        // Execution time
        Log.e("Alarm display", "Execution time = " + alarm.getExecuteTimeInMils() + " which is at " + DateUtils.getTimeFromLong(alarm.getExecuteTimeInMils()));

        // Duration
        Log.e("Alarm display", "Duration = " + alarm.getDuration() + " which is at " + DateUtils.getTimeFromLong(alarm.getExecuteTimeInMils() + alarm.getDuration()));

        // Connections
        Log.e("Alarm display", "Connections: ");
        for (Connection conn : alarm.getConnections())
            Log.e("Alarm display", "" + conn);

        // Days
        Log.e("Alarm display", "Days: ");
        for (Integer day : alarm.getDays())
            Log.e("Alarm display", "" + day);

        // Separator
        Log.e("Alarm display", "-");
    }

    /**
     * Method that returns the default connection option set on the alarm's
     * creation.
     * For the moment the default connection is Wifi
     *
     * @return: A list containing the default connection option
     */
    public static List<Connection> getDefaultConnection() {
        List<Connection> wifi = new ArrayList<>();
        wifi.add(Connection.WIFI);
        return wifi;
    }

    /**
     * Method that returns the time left in miliseconds until the next alarm trigger
     *
     * @param alarm
     * @return
     */
    public static long getAlarmExecutionTime(PreciseConnectivityAlarm alarm) {

        // The default value of the execution time
        long executionTime = alarm.getStartTime() - System.currentTimeMillis();

        // If the execution time is inferior to the current system time,
        if (executionTime < 0) {

            /**
             * If execution time + Duration > Current time
             * It means the second phase of the alarm needs to be handled
             * The second phase being to re-enable the connections
             * The second phase is triggered at the end time of the alarm
             */
            if (executionTime + alarm.getDuration() > 0) {
                executionTime += alarm.getDuration();
            }

            /**
             * In this case the alarm will be triggered on another day
             * So we get the next day the alarm is supposed to be triggered on,
             * then add to it the execution time
             *
             * This is calculated using the formula below:
             * executionTime = (Number of days until next alarm trigger).toMiliseconds - (Current time - start time).toMiliseconds
             *
             * Example:
             * - Start time is 7:41 pm
             * - Current time is 9:41 pm
             * - Today is Sunday, next alarm trigger is Tuesday
             * So:
             * - Number of days until next alarm trigger = 2 days = 48 hours
             * - Current time - start time = 2 hours
             * So basically the next alarm is in 46 hours, convert this to miliseconds and this
             * gives us the time left until the next alarm trigger, which is the alarm's executionTime
             */
            else {
                executionTime = TimeUnit.DAYS.toMillis(getNumberOfDaysUntilNextAlarm(alarm)) - (System.currentTimeMillis() - alarm.getStartTime());
            }
        }

        return executionTime;
    }

    /**
     * Method that returns the number of days until the next day the
     * alarm is supposed to be launched on
     *
     * @return
     */
    public static int getNumberOfDaysUntilNextAlarm(PreciseConnectivityAlarm alarm) {
        /**
         * The minimum number of days is set to the maximum value it can take,
         * which is 7, one week, which is also the maximum periodicity of an
         * active alarm
         */
        int minimumDays = 7;

        // The value of the current day of the week
        int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        int differenceBetweenTwoDays;
        for (int day : alarm.getDays()) {
            // We first get the difference between the 2 days
            differenceBetweenTwoDays = DateUtils.differenceBetweenTwoDays(today, day);

            // A new minimum is assigned to 'minimumDays' if the difference is smaller its value
            if (differenceBetweenTwoDays < minimumDays)
                minimumDays = differenceBetweenTwoDays;
        }
        return minimumDays;
    }
}
