package io.mdevlab.unconnectify;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.List;

import io.mdevlab.unconnectify.adapter.AlarmAdapter;
import io.mdevlab.unconnectify.adapter.AlarmViewHolder;
import io.mdevlab.unconnectify.alarm.AlarmManager;
import io.mdevlab.unconnectify.alarm.PreciseConnectivityAlarm;
import io.mdevlab.unconnectify.connectivitymodels.Hotspot;
import io.mdevlab.unconnectify.data.AlarmSqlHelper;
import io.mdevlab.unconnectify.fragment.TimePickerFragment;
import io.mdevlab.unconnectify.utils.DateUtils;
import io.mdevlab.unconnectify.utils.DialogUtils;
import io.mdevlab.unconnectify.utils.FeatureDiscovery;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    public static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mAlarmList;
    private AlarmAdapter mAlarmAdapter;
    private AlarmSqlHelper mAlarmSqlHelper;
    private TextView mAlarmsCount;
    private Toolbar mToolbar;
    private SharedPreferences onboardingSharedPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.menu_main);
        setSupportActionBar(mToolbar);

        //Recycler View
        mAlarmList = (RecyclerView) findViewById(R.id.alarms_list);

        //Layout manager
        mLayoutManager = new LinearLayoutManager(MainActivity.this);

        //Set the Recycler with layout manager
        mAlarmList.setLayoutManager(mLayoutManager);

        //Set the animator with the default
        mAlarmList.setItemAnimator(new DefaultItemAnimator());

        //instance of the SQLHelper
        mAlarmSqlHelper = new AlarmSqlHelper(MainActivity.this);

        //Read the list of alarms
        List<PreciseConnectivityAlarm> alarms = mAlarmSqlHelper.readAllAlarms(null, null);

        //The alarm Adapter
        mAlarmAdapter = new AlarmAdapter(alarms, MainActivity.this);
        mAlarmList.setAdapter(mAlarmAdapter);

        // Feature discovery
        mAlarmList.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                AlarmViewHolder holder = (AlarmViewHolder) mAlarmList.findViewHolderForAdapterPosition(0);
                if (holder != null) {
                    FeatureDiscovery.getInstance().onFirstAlarmCreatedFeatureDiscovery((Activity) MainActivity.this,
                            holder.mStartTime,
                            holder.mEndTime,
                            holder.mWifi,
                            holder.mSunday,
                            holder);
                }
            }
        });




        //Alarms counts textview
        mAlarmsCount = (TextView) findViewById(R.id.alarms_count);
        mAlarmsCount.setText(alarms.size() + " alarms");
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem createAlarm = menu.findItem(R.id.action_add_alarm);
        if (createAlarm != null)
            FeatureDiscovery.getInstance().createAlarmFeatureDiscovery(MainActivity.this, findViewById(R.id.action_add_alarm));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_alarm) {
            showTimePicker();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Show the time picker based on TimePickerFragment
     */
    private void showTimePicker() {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setListener(this);
        timePickerFragment.show(getSupportFragmentManager(), "time picker");
    }


    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

        //Verify if The timePicker is shown
        //This is a know Bug https://code.google.com/p/android/issues/detail?id=34833
        if (timePicker.isShown()) {

            /**
             * If 'minute' is between 0 and 9 it only has one digit,
             * so a '0' is added to the beginning of it
             */
            String s_minute = (0 <= minute && minute <= 9) ? "0" + minute : String.valueOf(minute);

            // New execution time
            long newExecutionTime = DateUtils.getLongFromTime(hourOfDay + ":" + s_minute);


            //Create the default alarm
            PreciseConnectivityAlarm preciseConnectivityAlarm = new PreciseConnectivityAlarm(newExecutionTime);

            //Create and insert the new Alarm
            long alarmId = AlarmManager.getInstance(getApplicationContext()).createAlarm(preciseConnectivityAlarm);

            //set the alarm id
            preciseConnectivityAlarm.setAlarmId((int) alarmId);

            mAlarmAdapter.addAlarm(preciseConnectivityAlarm);

            setAlarmsCount();
        }
    }

    /**
     * This function adjust the number of alarms displayed in mAlarmsCount textview
     */
    public void setAlarmsCount() {
        // Adjust number of alarms in alarms count textview
        mAlarmsCount.setText(mAlarmAdapter.getItemCount() + " alarms");
    }

    public AlarmAdapter getAlarmAdapter() {
        return mAlarmAdapter;
    }

    public TextView getAlarmsCount() {
        return mAlarmsCount;
    }

    /**
     * Method that disables end time, and sets the value of the boolean
     * 'hasDisabledEndTime' to false
     *
     * @param position: Position of alarm to update
     */
    public void disableEndTime(int position) {
        if (mAlarmList != null && mAlarmList.findViewHolderForAdapterPosition(position) != null)
            ((AlarmViewHolder) mAlarmList.findViewHolderForAdapterPosition(position)).disableEndTime();
    }

    /**
     * Function used for testing Hotspot enabling
     *
     * @param view
     */
    public void enable(View view) {

        if (DialogUtils.showDialog(MainActivity.this)) {
            Hotspot.getInstance(MainActivity.this).enable();
        }
    }

    /**
     * Function used for testing Hotspot disabling
     *
     * @param view
     */
    public void disable(View view) {
        if (DialogUtils.showDialog(MainActivity.this)) {
            Hotspot.getInstance(MainActivity.this).disable();
        }
    }



}
