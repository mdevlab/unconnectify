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
 * This class will be the main class for all the database Operations
 * the creation of th Database
 * -Table Alarm
 * -Table alarm_days
 * -Table alarm_connections
 * and all The Needed CRUD operations
 * some specific helper Methods to interact with the DBs with specific attributes
 * <p>
 * Created by mdevlab on 2/10/17.
 */

public class AlarmSqlHelper extends SQLiteOpenHelper {

    public static final String TAG = AlarmSqlHelper.class.getSimpleName();
    //Datbase Name
    public static final String DATABASE_NAME = "Alarms.db";
    //Current Version 1 since Feb 11 2017
    public static final int DATABASE_VERSION = 1;
    public static final int NO_DURATION = 0;
    public static final int NO_JOBID = 0;
    public static final Boolean DEFAULT_CURRENTSTATE = false;
    public static final String ASC = "ASC";

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
    public static final String CURRENTSTATE = "currentstate";

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
            + CURRENTSTATE + " TEXT,"
            + EXECUTION_TIME_COLUMN + " INTEGER,"
            + DURATION + " INTEGER,"
            + JOBID + " INTEGER,"
            + UPDATETIME + " INTEGER)";

    /*
     Table Create Statements for table alarm_days
     alarm_days(id, alarm_id, day_id)
      */

    private static final String CREATE_TABLE_ALARM_DAYS = "CREATE TABLE "
            + TABLE_ALARM_DAYS + "("
            + KEY_ALARM_ID + " INTEGER,"
            + KEY_DAY_ID + " INTEGER ," +
            " PRIMARY KEY (" + KEY_ALARM_ID + ", " + KEY_DAY_ID + "))";

    /*
    Table Create Statements for table alarm_connections
        alarm_connections(id, alarm_id, connection_id)
        */

    private static final String CREATE_TABLE_ALARM_CONNECTIONS = "CREATE TABLE "
            + TABLE_ALARM_CONNECTIONS + "("
            + KEY_ALARM_ID + " INTEGER,"
            + KEY_CONNECTION_ID + " INTEGER ," +
            " PRIMARY KEY (" + KEY_ALARM_ID + ", " + KEY_CONNECTION_ID + "))";

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
        values.put(ISACTIVE_COLUMN, String.valueOf(connectivityAlarm.isActive()));
        values.put(CURRENTSTATE, String.valueOf(DEFAULT_CURRENTSTATE));
        values.put(EXECUTION_TIME_COLUMN, connectivityAlarm.getExecuteTimeInMils());
        //update the current alarm update time value
        values.put(UPDATETIME, System.currentTimeMillis());
        values.put(JOBID, NO_JOBID);
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

        //execute the query
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
                //create the instance of preciseConnectivityAlarm form getConnectivityAlarmFromCursor method
                PreciseConnectivityAlarm preciseConnectivityAlarm = getConnectivityAlarmFromCursor(cursor);

                // adding alarm  to alarms list
                alarms.add(preciseConnectivityAlarm);
                //jump to next line within the cursor
            } while (cursor.moveToNext());
        }
        //return the final list of alarms
        return alarms;
    }

    /**
     * This function build PreciseConnectivityAlarm from a cursor
     *
     * @param cursor the cursor from we will retrieve  PreciseConnectivityAlarm data
     * @return PreciseConnectivityAlarm created from the given cursor
     */
    public PreciseConnectivityAlarm getConnectivityAlarmFromCursor(Cursor cursor) {
        //the preciseConnectivityAlarm to be returned
        PreciseConnectivityAlarm preciseConnectivityAlarm = new PreciseConnectivityAlarm();
        //get the alarm id
        int alarmid = cursor.getInt((cursor.getColumnIndex(KEY_ID)));

        //instanciate the preciseConnectivityAlarm with setters methods
        preciseConnectivityAlarm.setAlarmId(cursor.getInt((cursor.getColumnIndex(KEY_ID))));
        preciseConnectivityAlarm.setExecuteTimeInMils(cursor.getLong((cursor.getColumnIndex(EXECUTION_TIME_COLUMN))));
        preciseConnectivityAlarm.setActive(Boolean.parseBoolean(cursor.getString((cursor.getColumnIndex(ISACTIVE_COLUMN)))));
        preciseConnectivityAlarm.setCurrentState(Boolean.parseBoolean(cursor.getString((cursor.getColumnIndex(CURRENTSTATE)))));
        preciseConnectivityAlarm.setDuration(cursor.getInt((cursor.getColumnIndex(DURATION))));
        preciseConnectivityAlarm.setLastUpdate(cursor.getInt((cursor.getColumnIndex(UPDATETIME))));
        preciseConnectivityAlarm.setJobId(cursor.getInt((cursor.getColumnIndex(JOBID))));

                /*Fill days and connections using respectively getAllDaysOfAlarm getAllConnectionOfAlarm the helper methods
                * to get the alarms days and connections
                */
        List<Integer> days = getAllDaysOfAlarm(alarmid);
        List<Connection> connections = getAllConnectionOfAlarm(alarmid);

        //set days and connections list
        preciseConnectivityAlarm.setConnections(connections);
        preciseConnectivityAlarm.setDays(days);
        return preciseConnectivityAlarm;
    }

    /**
     * This method call readAllAlarms by specifying the selection and the selectino arguments
     * readAllAlarms return the list of all active alarms
     *
     * @return A list of all active Alarms
     */
    public List<PreciseConnectivityAlarm> readAllActiveAlarms() {
        return readAllAlarms(ISACTIVE_COLUMN + " = ?", new String[]{"true"});
    }

    /**
     * Get all days on which the alarm will be  repeated
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
        Cursor cursor = db.query(TABLE_ALARM_CONNECTIONS,
                null,
                KEY_ALARM_ID + " = ?",
                new String[]{Integer.toString(id)},
                null,
                null,
                KEY_CONNECTION_ID + " ASC");
        if (cursor.moveToFirst()) {
            do {
                listOfConnections.add(Connection.fromInt(cursor.getInt((cursor.getColumnIndex(KEY_CONNECTION_ID)))));
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
     * Update the executionTime and alarmDuration   of the given alarm
     *
     * @param alarmId       the alarm id to be updated
     * @param executionTime the executionTime value to be inserted
     * @param alarmDuration the alarmDuration value to be inserted
     * @return
     */
    public int updateAlarm(int alarmId, long executionTime, long alarmDuration) {
        //Get the writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        //values to be updated EXECUTION_TIME_COLUMN,DURATION,UPDATETIME
        ContentValues values = new ContentValues();
        values.put(EXECUTION_TIME_COLUMN, executionTime);
        values.put(DURATION, alarmDuration);
        values.put(UPDATETIME, System.currentTimeMillis());

        // updating the row
        return db.update(TABLE_ALARM, values, KEY_ID + " = ?",
                new String[]{String.valueOf(alarmId)});
    }

    /**
     * Update the Jobid of the given alarm
     *
     * @param alarmId the alarm id to be updated
     * @param jobId   the Jobid value to be inserted
     * @return
     */
    public int updateAlarmJob(int alarmId, int jobId) {
        //Get the writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        //values to be updated JOBID
        ContentValues values = new ContentValues();
        values.put(JOBID, jobId);
        values.put(UPDATETIME, System.currentTimeMillis());

        // updating the row
        return db.update(TABLE_ALARM, values, KEY_ID + " = ?",
                new String[]{String.valueOf(alarmId)});

    }

    /**
     * Update the connection of a given alarm
     *
     * @param alarmId            alarm concerned
     * @param selectedConnection The type of connection
     * @param isActive           status of Connection  if true create the connection row else delete the row
     */
    public void updateAlarmConnection(int alarmId, Connection selectedConnection, boolean isActive) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (isActive) {
            createAlarmConnection(alarmId, selectedConnection);
        } else {
            db.delete(TABLE_ALARM_CONNECTIONS, KEY_ALARM_ID + " = ? AND " + KEY_CONNECTION_ID + "= ?",
                    new String[]{String.valueOf(alarmId), String.valueOf(selectedConnection.getValue())});
        }
        //Update the attached alarm
        alarmUpdated(alarmId);
    }

    /**
     * Update the Day of a given alarm
     *
     * @param alarmId     alarm concerned
     * @param selectedDay The Day
     * @param isActive    status of the day  if true create the day row else delete the row
     */
    public void updateAlarmDay(int alarmId, int selectedDay, boolean isActive) {

        SQLiteDatabase db = this.getWritableDatabase();
        if (isActive) {
            createAlarmDay(alarmId, selectedDay);

        } else {
            int i = db.delete(TABLE_ALARM_DAYS, KEY_ALARM_ID + " = ? AND " + KEY_DAY_ID + "= ?",
                    new String[]{String.valueOf(alarmId), String.valueOf(selectedDay)});

        }
        //Update the attached alarm
        alarmUpdated(alarmId);

    }

    /**
     * this method close the Database
     */
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /**
     * This method return the next Alarm based on  execution time and if the alarm is active
     *
     * @return the next alarm
     */
    public PreciseConnectivityAlarm readNextAlarm() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        PreciseConnectivityAlarm preciseConnectivityAlarm;

        cursor = db.rawQuery("SELECT * FROM " + TABLE_ALARM + " WHERE " + ISACTIVE_COLUMN + "=? ORDER BY " + EXECUTION_TIME_COLUMN + " ASC, " + UPDATETIME + " DESC",
                new String[]{"true"});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            //create the instance of preciseConnectivityAlarm form getConnectivityAlarmFromCursor method
            preciseConnectivityAlarm = getConnectivityAlarmFromCursor(cursor);
            return preciseConnectivityAlarm;
        }

        return null;
    }

    /**
     * This function return the alarm by the provided jobid
     * actually the job id provider should brovide a unique id for an alarm
     *
     * @param jobId each alarm is associated to a job  to be runned in the background
     * @return the alarm
     */
    public PreciseConnectivityAlarm getAlarmByJobId(int jobId) {
        return  getAlarmByIds(jobId,"SELECT * FROM " + TABLE_ALARM + " WHERE " + JOBID + "=? ");
    }

    /**
     * This function return the alarm by the provided id
     * actually the  id provider should brovide a unique id for an alarm
     *
     * @param id each alarm is associated to an id
     */
    public PreciseConnectivityAlarm getAlarmById(int id) {
        return  getAlarmByIds(id,"SELECT * FROM " + TABLE_ALARM + " WHERE " + KEY_ID + "=? ");
    }


    /**
     * A helper for retrieving a row by the specific id  ,this function is used in both getAlarmByJobId and getAlarmById
     *
     * @param id
     * @param Query
     * @return
     */
    private PreciseConnectivityAlarm getAlarmByIds(int id,String Query){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor;
        PreciseConnectivityAlarm preciseConnectivityAlarm;
        cursor = db.rawQuery(Query,
                new String[]{String.valueOf(id)});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            //create the instance of preciseConnectivityAlarm form getConnectivityAlarmFromCursor method
            preciseConnectivityAlarm = getConnectivityAlarmFromCursor(cursor);
            return preciseConnectivityAlarm;
        }

        return null;

    }




    /**
     * this method is called specifically to notice the alarm row if any change happened to this alarm
     *
     * @param alarmId alarm id to notice
     * @return number of rows updated
     */
    private int alarmUpdated(int alarmId) {
        //Get the writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        //update the update column each time we update the alarm
        ContentValues values = new ContentValues();
        values.put(UPDATETIME, System.currentTimeMillis());


        // updating the row
        return db.update(TABLE_ALARM, values, KEY_ID + " = ?",
                new String[]{String.valueOf(alarmId)});

    }

    /**
     * This method update the current state of the alarm
     *
     * @param alarmId      alarm concerned
     * @param currentState the statte of current Connection whether true(ON) or false(OFF)
     * @return the number of  row affected normally 1 or 0
     */
    public int updateAlarmCurrentState(int alarmId, boolean currentState) {
        //Get the writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        //values to be updated JOBID
        ContentValues values = new ContentValues();
        values.put(CURRENTSTATE, String.valueOf(currentState));
        values.put(UPDATETIME, System.currentTimeMillis());

        // updating the row
        return db.update(TABLE_ALARM, values, KEY_ID + " = ?",
                new String[]{String.valueOf(alarmId)});


    }

    public PreciseConnectivityAlarm getAlarm() {
        return null;
    }
}
