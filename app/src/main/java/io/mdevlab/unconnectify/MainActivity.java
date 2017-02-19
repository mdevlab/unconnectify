package io.mdevlab.unconnectify;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.mdevlab.unconnectify.adapter.AlarmAdapter;
import io.mdevlab.unconnectify.alarm.PreciseConnectivityAlarm;
import io.mdevlab.unconnectify.data.AlarmSqlHelper;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mAlarmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//
        mAlarmList = (RecyclerView) findViewById(R.id.alarms_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mAlarmList.setLayoutManager(mLayoutManager);
        mAlarmList.setItemAnimator(new DefaultItemAnimator());

//        AlarmUtils.createFakeData(getApplicationContext());

        AlarmSqlHelper alarmSqlHelper = new AlarmSqlHelper(MainActivity.this);
        List<PreciseConnectivityAlarm> alarms = alarmSqlHelper.readAllAlarms(null, null);
//
        AlarmAdapter alarmAdapter = new AlarmAdapter(alarms, MainActivity.this);

        TextView alarmsCount = (TextView) findViewById(R.id.alarms_count);
        alarmsCount.setText(alarms.size() + " alarms");

        mAlarmList.setAdapter(alarmAdapter);
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
            Toast.makeText(this, "Add an alarm!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
