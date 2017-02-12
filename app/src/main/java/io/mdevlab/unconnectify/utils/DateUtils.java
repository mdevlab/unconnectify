package io.mdevlab.unconnectify.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mdevlab on 2/12/17.
 */

public class DateUtils {

    /**
     * @param timeInMillis
     * @return: The time in the format HH:mm from the long passed on to it as
     * a parameter
     */
    public static String getTimeFromLong(long timeInMillis) {
        return new SimpleDateFormat("HH:mm").format(timeInMillis);
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
    public static int differenceBetweenTwoDays(int dayOne, int dayTwo) {
        int theDifference = dayTwo - dayOne;

        /**
         * Let's take the example of tuesday to monday of the following week.
         * The method call would be smth like this differenceBetweenTwoDays(tuesday, monday)
         * Using the Calendar class, Tuesday is equal to 3, Monday is equal to 2
         * so theDifference = -1. -1 modulo 7 is equal the same as 6 modulo 7.
         * There are 6 days separating tuesday of week 1 from monday of week 2, so
         * when the result is negative 7 is simply added to it.
         */
        if (theDifference < 0)
            theDifference += 7;

        return theDifference;
    }
}
