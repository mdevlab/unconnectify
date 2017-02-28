package io.mdevlab.unconnectify.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.Calendar;
import java.util.List;

import io.mdevlab.unconnectify.MainActivity;
import io.mdevlab.unconnectify.R;
import io.mdevlab.unconnectify.alarm.AlarmManager;
import io.mdevlab.unconnectify.alarm.PreciseConnectivityAlarm;
import io.mdevlab.unconnectify.utils.Connection;
import io.mdevlab.unconnectify.utils.DateUtils;
import io.mdevlab.unconnectify.utils.FeatureDiscovery;

/**
 * Created by mdevlab on 2/12/17.
 */

public class AlarmAdapter extends RecyclerView.Adapter<AlarmViewHolder> {


    //Colors for items background
    private int[] colors = {
            R.color.color_1,
            R.color.color_2,
            R.color.color_3,
            R.color.color_4,
            R.color.color_5,
            R.color.color_6,
            R.color.color_7,
            R.color.color_8,
            R.color.color_9,
            R.color.color_10
    };

    private List<PreciseConnectivityAlarm> alarms;
    private Context mContext;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    /**
     * Constructor for  the alarm adapter
     *
     * @param alarms  list of alarms provided
     * @param context
     */
    public AlarmAdapter(List<PreciseConnectivityAlarm> alarms, Context context) {
        this.alarms = alarms;
        this.mContext = context;
    }

    /**
     * add alarm to the List of alarms
     *
     * @param preciseConnectivityAlarm
     */
    public void addAlarm(PreciseConnectivityAlarm preciseConnectivityAlarm) {
        alarms.add(0, preciseConnectivityAlarm);
        this.notifyDataSetChanged();
    }

    /**
     * delete the alarm from the list position and with the given id
     *
     * @param position the position in the list
     * @param alarmId  the id of the alarm used for db purposes as we delete the alarm by id
     */
    public void deleteAlarm(int position, int alarmId) {
        alarms.remove(position);
        this.notifyDataSetChanged();
        ((MainActivity) mContext).setAlarmsCount();
        AlarmManager.getInstance(mContext).clearAlarm(alarmId);
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_list_view, parent, false);
        return new AlarmViewHolder(itemView, mContext);
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, final int position) {
        final PreciseConnectivityAlarm currentAlarm = alarms.get(position);

        //Delete the alarm
        holder.mDeleteAlarm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                deleteAlarm(position, currentAlarm.getAlarmId());
            }
        });

        viewBinderHelper.bind(holder.mSwipeRevealLayout, String.valueOf(currentAlarm.getAlarmId()));

        // Setting the alarm object to the holder
        holder.setAlarm(currentAlarm);

        // Setting the position to the holder
        holder.setPosition(position);

        // Setting the card's color
        holder.mContainer.setBackgroundColor(ContextCompat.getColor(mContext, colors[position % colors.length]));

        // Setting the card's mask
        holder.mSwitchedOffAlarmCover.setVisibility(currentAlarm.isActive() ? View.GONE : View.VISIBLE);

        // Setting the switch on/off toggle button
        setToggleWithoutEvent(holder.mSwitchOnOffToggle, !currentAlarm.isActive());

        // Start time
        holder.mStartTime.setText(DateUtils.getTimeFromLong(currentAlarm.getStartTime()));

        // End time and separator
        if (currentAlarm.getDuration() != 1) {
            holder.mEndTime.setText(DateUtils.getTimeFromLong(currentAlarm.getStartTime() + currentAlarm.getDuration()));
        } else {
            holder.mEndTime.setAlpha(0.5f);
            holder.mTimesSeparator.setAlpha(0.5f);
        }

        // Wifi
        if (currentAlarm.getConnections().contains(Connection.WIFI))
            setToggleWithoutEvent(holder.mWifi, true);

        // Hotspot
        if (currentAlarm.getConnections().contains(Connection.HOTSPOT))
            setToggleWithoutEvent(holder.mHotspot, true);

        // Bluetooth
        if (currentAlarm.getConnections().contains(Connection.BLUETOOTH))
            setToggleWithoutEvent(holder.mBluetooth, true);

        // Sunday
        if (currentAlarm.getDays().contains(Calendar.SUNDAY))
            setToggleWithoutEvent(holder.mSunday, true);

        // Monday
        if (currentAlarm.getDays().contains(Calendar.MONDAY))
            setToggleWithoutEvent(holder.mMonday, true);

        // Tuesday
        if (currentAlarm.getDays().contains(Calendar.TUESDAY))
            setToggleWithoutEvent(holder.mTuesday, true);

        // Wednesday
        if (currentAlarm.getDays().contains(Calendar.WEDNESDAY))
            setToggleWithoutEvent(holder.mWednesday, true);

        // Thursday
        if (currentAlarm.getDays().contains(Calendar.THURSDAY))
            setToggleWithoutEvent(holder.mThursday, true);

        // Friday
        if (currentAlarm.getDays().contains(Calendar.FRIDAY))
            setToggleWithoutEvent(holder.mFriday, true);

        // Saturday
        if (currentAlarm.getDays().contains(Calendar.SATURDAY))
            setToggleWithoutEvent(holder.mSaturday, true);

        FeatureDiscovery.getInstance().onFirstAlarmCreatedFeatureDiscovery((Activity) mContext,
                holder.mStartTime,
                holder.mEndTime,
                holder.mWifi,
                holder.mSunday);
    }

    @Override
    public int getItemCount() {
        if (alarms != null)
            return alarms.size();
        return 0;
    }

    /**
     * Method that checks or unchecks a toggle button without triggering its
     * setOnCheck listener
     *
     * @param toggleButton
     * @param isChecked
     */
    private void setToggleWithoutEvent(ToggleButton toggleButton, boolean isChecked) {
        toggleButton.setEnabled(false);
        toggleButton.setChecked(isChecked);
        toggleButton.setEnabled(true);
    }
}
