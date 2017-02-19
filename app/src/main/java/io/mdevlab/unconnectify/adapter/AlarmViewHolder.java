package io.mdevlab.unconnectify.adapter;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.List;

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

    View mContainer;

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

        mContainer = itemView.findViewById(R.id.container);

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
                if (mAlarm != null) {
                    AlarmManager.getInstance(mContext).updateAlarmConnection(mAlarm.getAlarmId(), Connection.WIFI, isChecked);

                    Log.e("Connections", "Connections of alarm " + mAlarm.getAlarmId());
                    List<Connection> days = mAlarmSqlHelper.getAllConnectionOfAlarm(mAlarm.getAlarmId());
                    for (Connection day : days) {
                        Log.e("Connections", String.valueOf(day));
                    }
                }
            }
        });

        mHotspot = (ToggleButton) itemView.findViewById(R.id.hotspot);
        mHotspot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null) {
                    AlarmManager.getInstance(mContext).updateAlarmConnection(mAlarm.getAlarmId(), Connection.HOTSPOT, isChecked);

                    Log.e("Connections", "Connections of alarm " + mAlarm.getAlarmId());
                    List<Connection> days = mAlarmSqlHelper.getAllConnectionOfAlarm(mAlarm.getAlarmId());
                    for (Connection day : days) {
                        Log.e("Connections", String.valueOf(day));
                    }
                }
            }
        });

        mBluetooth = (ToggleButton) itemView.findViewById(R.id.bluetooth);
        mBluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null) {
                    AlarmManager.getInstance(mContext).updateAlarmConnection(mAlarm.getAlarmId(), Connection.BLUETOOTH, isChecked);

                    Log.e("Connections", "Connections of alarm " + mAlarm.getAlarmId());
                    List<Connection> days = mAlarmSqlHelper.getAllConnectionOfAlarm(mAlarm.getAlarmId());
                    for (Connection day : days) {
                        Log.e("Connections", String.valueOf(day));
                    }
                }
            }
        });

        mSunday = (ToggleButton) itemView.findViewById(R.id.sunday);
        mSunday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null) {
                    AlarmManager.getInstance(mContext).updateAlarmDay(mAlarm.getAlarmId(), Calendar.SUNDAY, isChecked);
                    changeOpacity(mAlarm.getAlarmId(), mSunday, isChecked);
                }
            }
        });

        mMonday = (ToggleButton) itemView.findViewById(R.id.monday);
        mMonday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null) {
                    AlarmManager.getInstance(mContext).updateAlarmDay(mAlarm.getAlarmId(), Calendar.MONDAY, isChecked);
                    changeOpacity(mAlarm.getAlarmId(), mMonday, isChecked);
                }
            }
        });

        mTuesday = (ToggleButton) itemView.findViewById(R.id.tuesday);
        mTuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null) {
                    AlarmManager.getInstance(mContext).updateAlarmDay(mAlarm.getAlarmId(), Calendar.TUESDAY, isChecked);
                    changeOpacity(mAlarm.getAlarmId(), mTuesday, isChecked);
                }
            }
        });

        mWednesday = (ToggleButton) itemView.findViewById(R.id.wednesday);
        mWednesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null) {
                    AlarmManager.getInstance(mContext).updateAlarmDay(mAlarm.getAlarmId(), Calendar.WEDNESDAY, isChecked);
                    changeOpacity(mAlarm.getAlarmId(), mWednesday, isChecked);
                }
            }
        });

        mThursday = (ToggleButton) itemView.findViewById(R.id.thursday);
        mThursday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null) {
                    AlarmManager.getInstance(mContext).updateAlarmDay(mAlarm.getAlarmId(), Calendar.THURSDAY, isChecked);
                    changeOpacity(mAlarm.getAlarmId(), mThursday, isChecked);
                }
            }
        });

        mFriday = (ToggleButton) itemView.findViewById(R.id.friday);
        mFriday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null) {
                    AlarmManager.getInstance(mContext).updateAlarmDay(mAlarm.getAlarmId(), Calendar.FRIDAY, isChecked);
                    changeOpacity(mAlarm.getAlarmId(), mFriday, isChecked);
                }
            }
        });

        mSaturday = (ToggleButton) itemView.findViewById(R.id.saturday);
        mSaturday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null) {
                    AlarmManager.getInstance(mContext).updateAlarmDay(mAlarm.getAlarmId(), Calendar.SATURDAY, isChecked);
                    changeOpacity(mAlarm.getAlarmId(), mSaturday, isChecked);
                }
            }
        });
    }

    private void showTimePicker() {
        DialogFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(((MainActivity) mContext).getSupportFragmentManager(), "time picker");
    }

    private void changeOpacity(int alarmId, View toggleButton, boolean isChecked) {
        float opacity = 0.5f;
        if (isChecked)
            opacity = 1f;
        toggleButton.setAlpha(opacity);

        Log.e("Days", "Days of alarm " + alarmId);
        List<Integer> days = mAlarmSqlHelper.getAllDaysOfAlarm(alarmId);
        for (Integer day : days) {
            Log.e("Days", String.valueOf(day));
        }
    }

    public PreciseConnectivityAlarm getAlarm() {
        return mAlarm;
    }

    public void setAlarm(PreciseConnectivityAlarm mAlarm) {
        this.mAlarm = mAlarm;
    }
}
