package io.mdevlab.unconnectify.adapter;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.chauthai.swipereveallayout.SwipeRevealLayout;

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

public class AlarmViewHolder extends RecyclerView.ViewHolder implements TimePickerDialog.OnTimeSetListener {

    private AlarmSqlHelper mAlarmSqlHelper;
    private PreciseConnectivityAlarm mAlarm;
    private Context mContext;
    private boolean hasChosenStartTime = true;

    SwipeRevealLayout mSwipeRevealLayout;

    View mSwitchOnOff;
    ToggleButton mSwitchOnOffToggle;
    ImageView mDeletealarmImageView;

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

    public AlarmViewHolder(View itemView,final  Context context) {
        super(itemView);

        mAlarmSqlHelper = new AlarmSqlHelper(context);
        mContext = context;

        mSwipeRevealLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipeRevealLayout);

        // Switch alarm on/off
        mSwitchOnOff = itemView.findViewById(R.id.switch_alarm_on_off);
        mSwitchOnOffToggle = (ToggleButton) itemView.findViewById(R.id.switch_alarm_on_off_toggle);
        mSwitchOnOffToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null) {
                    AlarmManager.getInstance(mContext).updateAlarmState(mAlarm, isChecked);
                }
            }
        });

        //Delete the alarm
        mDeletealarmImageView = (ImageView)  itemView.findViewById(R.id.delete_alarm_button);


        // Main view container
        mContainer = itemView.findViewById(R.id.container);

        // start time
        mStartTime = (TextView) itemView.findViewById(R.id.start_time);
        mStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
                hasChosenStartTime = true;
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
                hasChosenStartTime = false;
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
                    changeOpacity(mSunday, isChecked);
                }
            }
        });

        mMonday = (ToggleButton) itemView.findViewById(R.id.monday);
        mMonday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null) {
                    AlarmManager.getInstance(mContext).updateAlarmDay(mAlarm.getAlarmId(), Calendar.MONDAY, isChecked);
                    changeOpacity(mMonday, isChecked);
                }
            }
        });

        mTuesday = (ToggleButton) itemView.findViewById(R.id.tuesday);
        mTuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null) {
                    AlarmManager.getInstance(mContext).updateAlarmDay(mAlarm.getAlarmId(), Calendar.TUESDAY, isChecked);
                    changeOpacity(mTuesday, isChecked);
                }
            }
        });

        mWednesday = (ToggleButton) itemView.findViewById(R.id.wednesday);
        mWednesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null) {
                    AlarmManager.getInstance(mContext).updateAlarmDay(mAlarm.getAlarmId(), Calendar.WEDNESDAY, isChecked);
                    changeOpacity(mWednesday, isChecked);
                }
            }
        });

        mThursday = (ToggleButton) itemView.findViewById(R.id.thursday);
        mThursday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null) {
                    AlarmManager.getInstance(mContext).updateAlarmDay(mAlarm.getAlarmId(), Calendar.THURSDAY, isChecked);
                    changeOpacity(mThursday, isChecked);
                }
            }
        });

        mFriday = (ToggleButton) itemView.findViewById(R.id.friday);
        mFriday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null) {
                    AlarmManager.getInstance(mContext).updateAlarmDay(mAlarm.getAlarmId(), Calendar.FRIDAY, isChecked);
                    changeOpacity(mFriday, isChecked);
                }
            }
        });

        mSaturday = (ToggleButton) itemView.findViewById(R.id.saturday);
        mSaturday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mAlarm != null) {
                    AlarmManager.getInstance(mContext).updateAlarmDay(mAlarm.getAlarmId(), Calendar.SATURDAY, isChecked);
                    changeOpacity(mSaturday, isChecked);
                }
            }
        });
    }

    private void showTimePicker() {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setListener(this);
        timePickerFragment.show(((MainActivity) mContext).getSupportFragmentManager(), "time picker");
    }

    private void changeOpacity(View toggleButton, boolean isChecked) {
        float opacity = 0.5f;
        if (isChecked)
            opacity = 1f;
        toggleButton.setAlpha(opacity);
    }

    public PreciseConnectivityAlarm getAlarm() {
        return mAlarm;
    }

    public void setAlarm(PreciseConnectivityAlarm mAlarm) {
        this.mAlarm = mAlarm;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        /**
         * If 'minute' is between 0 and 9 it only has one digit,
         * so a '0' is added to the beginning of it
         */
        String s_minute = (0 <= minute && minute <= 9) ? "0" + minute : String.valueOf(minute);

        /**
         * By default, we suppose that the start time is being updated
         * So the textView to update once the time has been chosen by the user
         * is the startTime textView
         */
        TextView viewToUpdate = mStartTime;

        // New execution time
        long newExecutionTime = DateUtils.getLongFromTime(hourOfDay + ":" + s_minute);

        // Duration hasn't changed
        long newDuration = mAlarm.getDuration();

        // If the end time is being updated
        if (!hasChosenStartTime) {
            // The textView to update is the endTime
            viewToUpdate = mEndTime;

            /**
             * duration = endTime - executionTime
             * endTime's value is in newExecutionTime, so:
             * duration = newExecutionTime - executionTime
             */
            newDuration = newExecutionTime - mAlarm.getExecuteTimeInMils();

            // Execution time hasn't changed
            newExecutionTime = mAlarm.getExecuteTimeInMils();
        }

        // Update the UI
        viewToUpdate.setText(hourOfDay + ":" + s_minute);

        // Update the alarm in the database and the alarm's job
        AlarmManager.getInstance(mContext).updateAlarm(mAlarm.getAlarmId(), newExecutionTime, newDuration);
    }


}
