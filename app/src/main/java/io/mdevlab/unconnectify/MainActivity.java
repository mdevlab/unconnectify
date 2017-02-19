package io.mdevlab.unconnectify;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.List;

import io.mdevlab.unconnectify.adapter.AlarmAdapter;
import io.mdevlab.unconnectify.alarm.AlarmManager;
import io.mdevlab.unconnectify.alarm.PreciseConnectivityAlarm;
import io.mdevlab.unconnectify.data.AlarmSqlHelper;
import io.mdevlab.unconnectify.fragment.TimePickerFragment;
import io.mdevlab.unconnectify.utils.DateUtils;




public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    public static final String TAG = MainActivity.class.getSimpleName();



    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView alarmList;
    private AlarmAdapter alarmAdapter;
    private AlarmSqlHelper alarmSqlHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Recycler View
        alarmList = (RecyclerView) findViewById(R.id.alarms_list);

        //Layout manager
        mLayoutManager = new LinearLayoutManager(MainActivity.this);

        //Set the Recycler with layout manager
        alarmList.setLayoutManager(mLayoutManager);

        //Set the animator with the default
        alarmList.setItemAnimator(new DefaultItemAnimator());

        //instance of the SQLHelper
        alarmSqlHelper = new AlarmSqlHelper(MainActivity.this);


        //Read the list of alarms
        List<PreciseConnectivityAlarm> alarms = alarmSqlHelper.readAllAlarms(null, null);


        //The alarm Adapter
        alarmAdapter = new AlarmAdapter(alarms, MainActivity.this);
        alarmList.setAdapter(alarmAdapter);


        //Alarms counts textview
        TextView alarmsCount = (TextView) findViewById(R.id.alarms_count);
        alarmsCount.setText(alarms.size() + " alarms");



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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

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
        long alarmid = AlarmManager.getInstance(getApplicationContext()).createAlarm(preciseConnectivityAlarm);

        //set the alarm id
        preciseConnectivityAlarm.setAlarmId((int) alarmid);

        alarmAdapter.addAlarm(preciseConnectivityAlarm);


    }
}
