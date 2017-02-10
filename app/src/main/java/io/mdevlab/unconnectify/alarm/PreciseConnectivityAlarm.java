package io.mdevlab.unconnectify.alarm;

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

public class PreciseConnectivityAlarm extends ConnectivityAlarm {

    private int mDuration;

    /**
     * Constructor that takes in a connectivityAlarm object and a duration
     * The newly created alarm will be an exact copy of the passed on alarm argument
     * with the difference of having a duration after which it will re-enable the connections
     * it had previously disabled
     *
     * @param connectivityAlarm: An alarm on the base of which a new alarm with an end time will be created
     * @param mDuration:         Duration after which the alarm should re-enable the connections it previously disabled
     */
    public PreciseConnectivityAlarm(ConnectivityAlarm connectivityAlarm, int mDuration) {
        super(connectivityAlarm);
        this.mDuration = mDuration;
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
        super(mConnections, mDays, mExecutionTimeInMils);
        this.mDuration = mDuration;
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
        super(mExecutionTimeInMils);
        this.mDuration = mDuration;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int mDuration) {
        this.mDuration = mDuration;
    }
}
