package io.mdevlab.unconnectify.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import io.mdevlab.unconnectify.alarm.ConnectivityAlarm;
import io.mdevlab.unconnectify.alarm.PreciseConnectivityAlarm;

/**
 * Created by mdevlab on 2/10/17.
 */

public class AlarmSqlHelper extends SQLiteOpenHelper {

    public AlarmSqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void createAlarm(ConnectivityAlarm connectivityAlarm) {

    }

    public void createAlarm(PreciseConnectivityAlarm preciseConnectivityAlarm) {

    }

    public List<PreciseConnectivityAlarm> readAllAlarms() {
        return null;
    }

    public List<PreciseConnectivityAlarm> readAllActiveAlarms() {
        return null;
    }

    public PreciseConnectivityAlarm readNextAlarm() {
        return null;
    }
}
