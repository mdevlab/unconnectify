package io.mdevlab.unconnectify.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by mdevlab on 2/12/17.
 */


/**
 * This class is the class helper for all date conversion and transformations
 * -Using Date formats
 * -Form time in millisecond to date
 * -Date to  millisecond
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
}
