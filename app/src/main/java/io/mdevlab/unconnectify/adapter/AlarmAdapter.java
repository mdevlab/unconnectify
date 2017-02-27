package io.mdevlab.unconnectify.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.Calendar;
import java.util.List;

import io.mdevlab.unconnectify.MainActivity;
import io.mdevlab.unconnectify.R;
import io.mdevlab.unconnectify.alarm.AlarmManager;
import io.mdevlab.unconnectify.alarm.PreciseConnectivityAlarm;
import io.mdevlab.unconnectify.utils.Connection;
import io.mdevlab.unconnectify.utils.DateUtils;

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
        alarms.add(preciseConnectivityAlarm);
        notifyDataSetChanged();
    }

    /**
     * delete the alarm from the list position and with the given id
     *
     * @param position the position in the list
     * @param alarmId  the id of the alarm used for db purposes as we delete the alarm by id
     */
    public void deleteAlarm(int position, int alarmId) {
        alarms.remove(position);
        notifyDataSetChanged();
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
        holder.setCheckToggleOnOff(false);
        holder.mSwitchOnOffToggle.setChecked(true);
        holder.setCheckToggleOnOff(true);

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
        holder.setCheckWifi(false);
        holder.mWifi.setChecked(currentAlarm.getConnections().contains(Connection.WIFI));
        holder.setCheckWifi(true);

        // Hotspot
        holder.setCheckHotspot(false);
        holder.mHotspot.setChecked(currentAlarm.getConnections().contains(Connection.HOTSPOT));
        holder.setCheckHotspot(true);

        // Bluetooth
        holder.setCheckBluetooth(false);
        holder.mBluetooth.setChecked(currentAlarm.getConnections().contains(Connection.BLUETOOTH));
        holder.setCheckBluetooth(true);

        // Sunday
        holder.setCheckSunday(false);
        holder.mSaturday.setChecked(currentAlarm.getDays().contains(Calendar.SUNDAY));
        holder.setCheckSunday(true);

        // Monday
        holder.setCheckMonday(false);
        holder.mMonday.setChecked(currentAlarm.getDays().contains(Calendar.MONDAY));
        holder.setCheckMonday(true);

        // Tuesday
        holder.setCheckTuesday(false);
        holder.mTuesday.setChecked(currentAlarm.getDays().contains(Calendar.TUESDAY));
        holder.setCheckTuesday(true);

        // Wednesday
        holder.setCheckWednesday(false);
        holder.mWednesday.setChecked(currentAlarm.getDays().contains(Calendar.WEDNESDAY));
        holder.setCheckWednesday(true);

        // Thursday
        holder.setCheckThursday(false);
        holder.mThursday.setChecked(currentAlarm.getDays().contains(Calendar.THURSDAY));
        holder.setCheckThursday(true);

        // Friday
        holder.setCheckFriday(false);
        holder.mFriday.setChecked(currentAlarm.getDays().contains(Calendar.FRIDAY));
        holder.setCheckFriday(true);

        // Saturday
        holder.setCheckSaturday(false);
        holder.mSaturday.setChecked(currentAlarm.getDays().contains(Calendar.SATURDAY));
        holder.setCheckSaturday(true);
    }

    @Override
    public int getItemCount() {
        if (alarms != null)
            return alarms.size();
        return 0;
    }
}
