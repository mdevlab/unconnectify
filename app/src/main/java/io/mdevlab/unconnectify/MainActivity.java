package io.mdevlab.unconnectify;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import io.mdevlab.unconnectify.adapter.AlarmAdapter;
import io.mdevlab.unconnectify.alarm.PreciseConnectivityAlarm;
import io.mdevlab.unconnectify.data.AlarmSqlHelper;
import io.mdevlab.unconnectify.utils.AlarmUtils;
import io.mdevlab.unconnectify.utils.DialogUtils;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView alarmList = (RecyclerView) findViewById(R.id.alarms_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        alarmList.setLayoutManager(mLayoutManager);
        alarmList.setItemAnimator(new DefaultItemAnimator());

        AlarmUtils.createFakeData(getApplicationContext());

        AlarmSqlHelper alarmSqlHelper = new AlarmSqlHelper(MainActivity.this);
        List<PreciseConnectivityAlarm> alarms = alarmSqlHelper.readAllAlarms(null, null);

        AlarmAdapter alarmAdapter = new AlarmAdapter(alarms, MainActivity.this);

        TextView alarmsCount = (TextView) findViewById(R.id.alarms_count);
        alarmsCount.setText(alarms.size() + " alarms");

        alarmList.setAdapter(alarmAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        DialogUtils.showDialog(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
