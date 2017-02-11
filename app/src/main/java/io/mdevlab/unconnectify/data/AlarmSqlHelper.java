package io.mdevlab.unconnectify.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import io.mdevlab.unconnectify.alarm.PreciseConnectivityAlarm;
import io.mdevlab.unconnectify.utils.Connection;

/**
 * This class will be the main class for all the database Operation
 * the creation of th Database
 * -Table Alarm
 * -Table alarm_days
 * -Table alarm_connections
 * and all The Needed CRUD operations
 * some specific helper Methods to interact with the DBs
 * <p>
 * Created by mdevlab on 2/10/17.
 */

public class AlarmSqlHelper extends SQLiteOpenHelper {

    //Datbase Name
    public static final String DATABASE_NAME = "Alarms.db";
    //Current Version 1 since Feb 11 2017
    public static final int DATABASE_VERSION = 1;
    public static final int NO_DURATION = 0;

    // Table Names
    private static final String TABLE_ALARM = "alarm";
    private static final String TABLE_ALARM_DAYS = "alarm_days";
    private static final String TABLE_ALARM_CONNECTIONS = "alarm_connections";

    //common column for alarm_days and  connection_days and alarm table
    private static final String KEY_ID = "id";

    // Alarm Table - column names
    public static final String ISACTIVE_COLUMN = "isactive";
    public static final String EXECUTION_TIME_COLUMN = "executiontimeinmils";
    public static final String DURATION = "duration";
    public static final String JOBID = "jobid";
    public static final String UPDATETIME = "updatetime";

    //common column for alarm_days and  connection_days
    private static final String KEY_ALARM_ID = "alarm_id";
    // Alarm_days Table - column names
    private static final String KEY_DAY_ID = "day_id";

    // Connection_days Table - column names
    private static final String KEY_CONNECTION_ID = "connection_id";


    /*
     Table Create Statements for table alarm
     alarm(id, isactive, executiontimeinmils)
        */

    private static final String CREATE_TABLE_ALARM = "CREATE TABLE "
            + TABLE_ALARM + "("
            + KEY_ID + " INTEGER PRIMARY KEY  AUTOINCREMENT ,"
            + ISACTIVE_COLUMN + " TEXT,"
            + EXECUTION_TIME_COLUMN + " INTEGER,"
            + DURATION + "INTEGER,"
            + JOBID + "INTEGER,"
            + UPDATETIME + "INTEGER)";

    /*
     Table Create Statements for table alarm_days
     alarm_days(id, alarm_id, day_id)
      */

    private static final String CREATE_TABLE_ALARM_DAYS = "CREATE TABLE "
            + TABLE_ALARM_DAYS + "("
            + KEY_ID + " INTEGER PRIMARY KEY  AUTOINCREMENT ,"
            + KEY_ALARM_ID + " INTEGER,"
            + KEY_DAY_ID + " INTEGER )";

    /*
    Table Create Statements for table alarm_connections
        alarm_connections(id, alarm_id, connection_id)
        */

    private static final String CREATE_TABLE_ALARM_CONNECTIONS = "CREATE TABLE "
            + TABLE_ALARM_CONNECTIONS + "("
            + KEY_ID + " INTEGER PRIMARY KEY  AUTOINCREMENT ,"
            + KEY_ALARM_ID + " INTEGER,"
            + KEY_CONNECTION_ID + " INTEGER )";

    /**
     * Constructor for the AlarmSqlHelper
     *
     * @param context
     */

    public AlarmSqlHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public AlarmSqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_ALARM);
        db.execSQL(CREATE_TABLE_ALARM_DAYS);
        db.execSQL(CREATE_TABLE_ALARM_CONNECTIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //TODO Implements the backup plan before updating the table so our users d'ont lose their alarms
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ALARM);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ALARM_DAYS);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ALARM_CONNECTIONS);

        // create new tables
        onCreate(db);
    }

    /**
     * This class create an alarm from PreciseConnectivityAlarmobject
     * parse the object get all attributes an than create the object along with it's days and connections
     *
     * @param connectivityAlarm
     * @return the id of the inserted alarm returned by insert method of SQLiteDatabase class
     */
    public Long createAlarm(PreciseConnectivityAlarm connectivityAlarm) {

        //get a writable instance of alarm db
        SQLiteDatabase db = this.getWritableDatabase();

        //get days and connections collections
        List<Connection> connections = connectivityAlarm.getConnections();
        List<Integer> days = connectivityAlarm.getDays();

        // instantiate row to be inserted
        ContentValues values = new ContentValues();
        values.put(ISACTIVE_COLUMN, connectivityAlarm.isActive());
        values.put(EXECUTION_TIME_COLUMN, connectivityAlarm.getExecuteTimeInMils());
        //update the current alarm update time value
        values.put(UPDATETIME, System.currentTimeMillis());
        //put the duration value
        values.put(DURATION, connectivityAlarm.getDuration());

        //insert row
        long alarmId = db.insert(TABLE_ALARM, null, values);

        // insert tag_ids
        for (long day_id : days) {
            createAlarmDay(alarmId, day_id);
        }

        // insert tag_ids
        for (Connection connection_id : connections) {
            createAlarmConnection(alarmId, connection_id);
        }

        return alarmId;


    }

    /**
     * This method add a row to alarm_connections table the connection attached to the alarm
     *
     * @param alarmId       the alarm id attached to this connection
     * @param connection_id the connection id attached to this alarm
     * @return
     */
    private Long createAlarmConnection(long alarmId, Connection connection_id) {

        //get a writable instance of alarm db
        SQLiteDatabase db = this.getWritableDatabase();

        // instantiate row to be inserted
        ContentValues values = new ContentValues();
        values.put(KEY_ALARM_ID, alarmId);
        values.put(KEY_CONNECTION_ID, connection_id.getValue());

        //insert row
        long id = db.insert(TABLE_ALARM_CONNECTIONS, null, values);

        //return alarm_connections KEY_ID
        return id;
    }

    /**
     * This method add a row to alarm_days table the day attached to the alarm
     *
     * @param alarmId the alarm id attached to this connection
     * @param day_id  the day id attached to this alarm
     * @return
     */
    private Long createAlarmDay(long alarmId, long day_id) {

        //get a writable instance of alarm db
        SQLiteDatabase db = this.getWritableDatabase();

        // instantiate row to be inserted
        ContentValues values = new ContentValues();
        values.put(KEY_ALARM_ID, alarmId);
        values.put(KEY_DAY_ID, day_id);

        //insert row
        long id = db.insert(TABLE_ALARM_DAYS, null, values);

        //return alarm_days KEY_ID
        return id;
    }

    /**
     * This method return all alarms within the specified selection
     * call example : readAllAlarms(ISACTIVE_COLUMN +" = ?", new String[] {"true"});
     *
     * @param selection     the selection clause
     * @param selectionArgs the arguments of the selection cause
     * @return the list of  PreciseConnectivityAlarm
     */
    public List<PreciseConnectivityAlarm> readAllAlarms(String selection, String[] selectionArgs) {
        List<PreciseConnectivityAlarm> alarms = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ALARM,
                null,
                selection,
                selectionArgs,
                null,
                null,
                EXECUTION_TIME_COLUMN);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PreciseConnectivityAlarm ca = new PreciseConnectivityAlarm();
                int alarmid = cursor.getInt((cursor.getColumnIndex(KEY_ID)));
                ca.setAlarmId(cursor.getInt((cursor.getColumnIndex(KEY_ID))));
                ca.setExecuteTimeInMils(cursor.getInt((cursor.getColumnIndex(EXECUTION_TIME_COLUMN))));
                ca.setActive(Boolean.parseBoolean(cursor.getString((cursor.getColumnIndex(ISACTIVE_COLUMN)))));
                ca.setDuration(cursor.getInt((cursor.getColumnIndex(DURATION))));
                List<Integer> days = getAllDaysOfAlarm(alarmid);
                List<Connection> connections = getAllConnectionOfAlarm(alarmid);
                ca.setConnections(connections);
                ca.setDays(days);
                // adding to alarms list
                alarms.add(ca);
            } while (cursor.moveToNext());
        }

        return alarms;
    }

    /**
     * Get all days on which the alarm will repeated
     *
     * @param id of the alarm
     * @return return a list  of Days  of the given id
     */
    public List<Integer> getAllDaysOfAlarm(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Integer> listOfDays = new ArrayList<>();
        Cursor cursor = db.query(TABLE_ALARM_DAYS,
                null,
                KEY_ALARM_ID + " = ?",
                new String[]{Integer.toString(id)},
                null,
                null,
                KEY_DAY_ID);
        if (cursor.moveToFirst()) {
            do {
                listOfDays.add(cursor.getInt((cursor.getColumnIndex(KEY_DAY_ID))));
            } while (cursor.moveToNext());
        }
        return listOfDays;
    }

    /**
     * This function return a list of connection related to this alarm and
     * Get all Connection on which the alarm will apply the alarm to
     *
     * @param id of the alarm
     * @return return a list  of Connection of the given id
     */
    public List<Connection> getAllConnectionOfAlarm(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Connection> listOfConnections = new ArrayList<>();
        Cursor cursor = db.query(TABLE_ALARM_DAYS,
                null,
                KEY_ALARM_ID + " = ?",
                new String[]{Integer.toString(id)},
                null,
                null,
                KEY_DAY_ID);
        if (cursor.moveToFirst()) {
            do {
                listOfConnections.add(Connection.fromInt(cursor.getInt((cursor.getColumnIndex(KEY_DAY_ID)))));
            } while (cursor.moveToNext());
        }
        return listOfConnections;
    }


    /**
     * Deleting single Alarm by id
     *
     * @param id alarm to be deleted
     */
    public void deleteAlarm(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_ALARM, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    /**
     * @param alarmId
     * @param executionTime
     * @param alarmDuration
     * @return
     */
    public int updateAlarm(int alarmId, long executionTime, long alarmDuration) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EXECUTION_TIME_COLUMN, executionTime);
        values.put(DURATION, alarmDuration);
        values.put(UPDATETIME, System.currentTimeMillis());

        // updating row
        return db.update(TABLE_ALARM, values, KEY_ALARM_ID + " = ?",
                new String[]{String.valueOf(alarmId)});
    }

    /**
     * @param alarmId
     * @param selectedConnection
     * @param isActive
     */
    public void updateAlarmConnection(int alarmId, Connection selectedConnection, boolean isActive) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (isActive) {
            createAlarmConnection(alarmId, selectedConnection);
        } else {
            db.delete(TABLE_ALARM_CONNECTIONS, KEY_CONNECTION_ID + " = ? AND " + KEY_ALARM_ID + "= ?",
                    new String[]{String.valueOf(alarmId), String.valueOf(selectedConnection.getValue())});
        }

    }

    /**
     * @param alarmId
     * @param selectedDay
     * @param isActive
     */
    public void updateAlarmDay(int alarmId, int selectedDay, boolean isActive) {

        SQLiteDatabase db = this.getWritableDatabase();
        if (isActive) {
            createAlarmDay(alarmId, selectedDay);
        } else {
            db.delete(TABLE_ALARM_DAYS, KEY_DAY_ID + " = ? AND " + KEY_ALARM_ID + "= ?",
                    new String[]{String.valueOf(alarmId), String.valueOf(selectedDay)});
        }

    }


    public PreciseConnectivityAlarm readNextAlarm() {
        return null;
    }

    public PreciseConnectivityAlarm getAlarm() {
        return null;
    }

    /**
     * this method close
     */
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
