package io.mdevlab.unconnectify.adapter;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Calendar;

import io.mdevlab.unconnectify.MainActivity;
import io.mdevlab.unconnectify.R;
import io.mdevlab.unconnectify.alarm.AlarmManager;
import io.mdevlab.unconnectify.alarm.PreciseConnectivityAlarm;
import io.mdevlab.unconnectify.data.AlarmSqlHelper;
import io.mdevlab.unconnectify.fragment.TimePickerFragment;
import io.mdevlab.unconnectify.utils.Connection;
import io.mdevlab.unconnectify.utils.DateUtils;

/**
 * Created by mdevlab on 2/12/17.
 */

public class AlarmViewHolder extends RecyclerView.ViewHolder {

    private AlarmSqlHelper mAlarmSqlHelper;
    private PreciseConnectivityAlarm mAlarm;
    private Context mContext;

    TextView mStartTime;
    TextView mTimesSeparator;
    TextView mEndTime;

    CheckBox mSetEndTime;

    ToggleButton mWifi;
    ToggleButton mHotspot;
    ToggleButton mBluetooth;

    ToggleButton mSunday;
    ToggleButton mMonday;
    ToggleButton mTuesday;
    ToggleButton mWednesday;
    ToggleButton mThursday;
    ToggleButton mFriday;
    ToggleButton mSaturday;

    public AlarmViewHolder(View itemView, Context context) {
        super(itemView);

        mAlarmSqlHelper = new AlarmSqlHelper(context);
        mContext = context;

        // start time
        mStartTime = (TextView) itemView.findViewById(R.id.start_time);
        mStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        // Times separator
        mTimesSeparator = (TextView) itemView.findViewById(R.id.times_separator);

        // End time
        mEndTime = (TextView) itemView.findViewById(R.id.end_time);
        mEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        // Set
        mSetEndTime = (CheckBox) itemView.findViewById(R.id.set_end_time);
        mSetEndTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                long duration;
                float alpha;
                boolean isEnabled;

                if (!isChecked) {
                    duration = 0;
                    alpha = 0.5f;
                    isEnabled = false;
                } else {
                    duration = DateUtils.getLongFromTime(mEndTime.getText().toString()) - DateUtils.getLongFromTime(mStartTime.getText().toString());
                    alpha = 1f;
                    isEnabled = true;
                }

                mEndTime.setEnabled(isEnabled);
                mEndTime.setAlpha(alpha);
                mTimesSeparator.setAlpha(alpha);

                if (mAlarm != null)
                    mAlarmSqlHelper.updateAlarm(mAlarm.getAlarmId(),
                            mAlarm.getExecuteTimeInMils(),
                            duration);
            }
        });

        mWifi = (ToggleButton) itemView.findViewById(R.id.wifi);
        mWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null)
                    AlarmManager.getInstance(mContext).updateAlarmConnection(mAlarm.getAlarmId(), Connection.WIFI, isChecked);
            }
        });

        mHotspot = (ToggleButton) itemView.findViewById(R.id.hotspot);
        mHotspot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null)
                    AlarmManager.getInstance(mContext).updateAlarmConnection(mAlarm.getAlarmId(), Connection.HOTSPOT, isChecked);
            }
        });

        mBluetooth = (ToggleButton) itemView.findViewById(R.id.bluetooth);
        mBluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null)
                    AlarmManager.getInstance(mContext).updateAlarmConnection(mAlarm.getAlarmId(), Connection.BLUETOOTH, isChecked);
            }
        });

        mSunday = (ToggleButton) itemView.findViewById(R.id.sunday);
        mSunday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null)
                    AlarmManager.getInstance(mContext).updateAlarmDay(mAlarm.getAlarmId(), Calendar.SUNDAY, isChecked);
            }
        });

        mMonday = (ToggleButton) itemView.findViewById(R.id.monday);
        mMonday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null)
                    AlarmManager.getInstance(mContext).updateAlarmDay(mAlarm.getAlarmId(), Calendar.MONDAY, isChecked);
            }
        });

        mTuesday = (ToggleButton) itemView.findViewById(R.id.tuesday);
        mTuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null)
                    AlarmManager.getInstance(mContext).updateAlarmDay(mAlarm.getAlarmId(), Calendar.TUESDAY, isChecked);
            }
        });

        mWednesday = (ToggleButton) itemView.findViewById(R.id.wednesday);
        mWednesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null)
                    AlarmManager.getInstance(mContext).updateAlarmDay(mAlarm.getAlarmId(), Calendar.WEDNESDAY, isChecked);
            }
        });

        mThursday = (ToggleButton) itemView.findViewById(R.id.thursday);
        mThursday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null)
                    AlarmManager.getInstance(mContext).updateAlarmDay(mAlarm.getAlarmId(), Calendar.THURSDAY, isChecked);
            }
        });

        mFriday = (ToggleButton) itemView.findViewById(R.id.friday);
        mFriday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null)
                    AlarmManager.getInstance(mContext).updateAlarmDay(mAlarm.getAlarmId(), Calendar.FRIDAY, isChecked);
            }
        });

        mSaturday = (ToggleButton) itemView.findViewById(R.id.saturday);
        mSaturday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null)
                    AlarmManager.getInstance(mContext).updateAlarmDay(mAlarm.getAlarmId(), Calendar.SATURDAY, isChecked);
            }
        });
    }

    private void showTimePicker() {
        DialogFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(((MainActivity) mContext).getSupportFragmentManager(), "time picker");
    }

    public PreciseConnectivityAlarm getAlarm() {
        return mAlarm;
    }

    public void setAlarm(PreciseConnectivityAlarm mAlarm) {
        this.mAlarm = mAlarm;
    }
}
