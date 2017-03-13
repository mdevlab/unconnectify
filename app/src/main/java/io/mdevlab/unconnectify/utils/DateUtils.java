package io.mdevlab.unconnectify.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.mdevlab.unconnectify.alarm.PreciseConnectivityAlarm;

/**
 * This class is the class helper for all date conversion and transformations
 * -Using Date formats
 * -Form time in millisecond to date
 * -Date to  millisecond
 * <p>
 * Created by mdevlab on 2/12/17.
 */
public class DateUtils {

    //This Pattern is used to specify the displaying of the Time for a specific notification
    public static final String NOTIFICATION_DATE_FORMAT = "EEE HH:mm";

    /**
     * Return date in specified format.
     *
     * @param timeinMillis Date in milliseconds
     * @param dateFormat   Date format
     * @return String representing date in specified format
     */
    public static String timeinMillistoDate(long timeinMillis, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeinMillis);
        return formatter.format(calendar.getTime());
    }


    /**
     * @param timeInMillis
     * @return: The time in the format HH:mm from the long passed on to it as
     * a parameter
     */
    public static String getTimeFromLong(long timeInMillis) {
        return new SimpleDateFormat("HH:mm").format(timeInMillis);
    }

    /**
     * Method that takes in a string representing time
     * It returns the time in milliseconds
     *
     * @param time: String in the format HH:mm
     * @return
     */
    public static long getLongFromTime(String time) {
        Date date = new Date();
        date.setHours(Integer.parseInt(time.split(":")[0]));
        date.setMinutes(Integer.parseInt(time.split(":")[1]));
        return date.getTime();
    }

    /**
     * @return: The current time in milliseconds
     */
    public static long getCurrentTimeInMillis() {
        Date date = new Date();
        return date.getTime();
    }

    /**
     * Method that returns the number of days between two days seperated at
     * most by a week. Example: Number of days between monday and sunday of the
     * same week, number of days between saturday and tuesday of the following
     * week.
     *
     * @param dayOne: Day superior in time to dayTwo
     * @param dayTwo: Day the precedes dayOne
     * @return: Number of days separating dayOne and dayTwo
     */
    public static int differenceBetweenTwoDays(int dayOne, int dayTwo, PreciseConnectivityAlarm alarm, boolean skipCurrentDay) {
        int theDifference = dayTwo - dayOne;

        /**
         * Let's take the example of tuesday to monday of the following week.
         * The method call would be smth like this differenceBetweenTwoDays(tuesday, monday)
         * Using the Calendar class, Tuesday is equal to 3, Monday is equal to 2
         * so theDifference = -1. -1 modulo 7 is equal the same as 6 modulo 7.
         * There are 6 days separating tuesday of week 1 from monday of week 2, so
         * when the result is negative 7 is simply added to it.
         */
        if (theDifference == 0) {

            if (skipCurrentDay)
                return 7;

            String startTime = getTimeFromLong(alarm.getStartTime());
            int startTimeHour = Integer.parseInt(startTime.split(":")[0]);
            int startTimeMinutes = Integer.parseInt(startTime.split(":")[1]);

            String currentTime = getTimeFromLong(System.currentTimeMillis());
            int currentTimeHour = Integer.parseInt(currentTime.split(":")[0]);
            int currentTimeMinutes = Integer.parseInt(currentTime.split(":")[1]);

            if (startTimeHour < currentTimeHour || (startTimeHour == currentTimeHour && startTimeMinutes < currentTimeMinutes))
                return 7;
        }
        return theDifference;
    }

    /**
     * Method that returns the current day of the week (Monday, Tuesday, ...)
     * This method uses the predefined days of the week defined in the class Calendar
     *
     * @return: The current day of the week
     */
    public static List<Integer> getToday() {
        List<Integer> today = new ArrayList<>();
        today.add(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        return today;
    }
}
