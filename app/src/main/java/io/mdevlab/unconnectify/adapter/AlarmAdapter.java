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

import io.mdevlab.unconnectify.R;
import io.mdevlab.unconnectify.alarm.PreciseConnectivityAlarm;
import io.mdevlab.unconnectify.utils.Connection;
import io.mdevlab.unconnectify.utils.DateUtils;

/**
 * Created by mdevlab on 2/12/17.
 */

public class AlarmAdapter extends RecyclerView.Adapter<AlarmViewHolder> {

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

    public AlarmAdapter(List<PreciseConnectivityAlarm> alarms, Context context) {
        this.alarms = alarms;
        this.mContext = context;
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_list_view, parent, false);
        return new AlarmViewHolder(itemView, mContext);
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, int position) {
        PreciseConnectivityAlarm currentAlarm = alarms.get(position);

        viewBinderHelper.bind(holder.mSwipeRevealLayout, String.valueOf(currentAlarm.getAlarmId()));

        // Setting the alarm object to the holder
        holder.setAlarm(currentAlarm);

        // Setting the card's color
        holder.mContainer.setBackgroundColor(ContextCompat.getColor(mContext, colors[position % colors.length]));

        // Start time
        holder.mStartTime.setText(DateUtils.getTimeFromLong(currentAlarm.getStartTime()));

        // End time and separator
        if (currentAlarm.getDuration() != 1) {
            holder.mEndTime.setText(DateUtils.getTimeFromLong(currentAlarm.getStartTime() + currentAlarm.getDuration()));
            holder.mEndTime.setEnabled(true);
            holder.mSetEndTime.setChecked(true);
        } else {
            holder.mEndTime.setAlpha(0.5f);
            holder.mEndTime.setEnabled(false);
            holder.mTimesSeparator.setAlpha(0.5f);
            holder.mSetEndTime.setChecked(false);
        }

        // Wifi
        if (currentAlarm.getConnections().contains(Connection.WIFI))
            holder.mWifi.setChecked(true);

        // Hotspot
        if (currentAlarm.getConnections().contains(Connection.HOTSPOT))
            holder.mHotspot.setChecked(true);

        // Bluetooth
        if (currentAlarm.getConnections().contains(Connection.BLUETOOTH))
            holder.mBluetooth.setChecked(true);

        // Sunday
        if (currentAlarm.getDays().contains(Calendar.SUNDAY))
            holder.mSunday.setChecked(true);

        // Monday
        if (currentAlarm.getDays().contains(Calendar.MONDAY))
            holder.mMonday.setChecked(true);

        // Tuesday
        if (currentAlarm.getDays().contains(Calendar.TUESDAY))
            holder.mTuesday.setChecked(true);

        // Wednesday
        if (currentAlarm.getDays().contains(Calendar.WEDNESDAY))
            holder.mWednesday.setChecked(true);

        // Thursday
        if (currentAlarm.getDays().contains(Calendar.THURSDAY))
            holder.mThursday.setChecked(true);

        // Friday
        if (currentAlarm.getDays().contains(Calendar.FRIDAY))
            holder.mFriday.setChecked(true);

        // Saturday
        if (currentAlarm.getDays().contains(Calendar.SATURDAY))
            holder.mSaturday.setChecked(true);
    }

    @Override
    public int getItemCount() {
        if (alarms != null)
            return alarms.size();
        return 0;
    }
}
