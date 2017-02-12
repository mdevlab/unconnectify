package io.mdevlab.unconnectify.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.mdevlab.unconnectify.alarm.PreciseConnectivityAlarm;
import io.mdevlab.unconnectify.data.AlarmSqlHelper;

/**
 * This class is a temporary class for generating a fake data
 * The future plan is the have a detailed unit test cases
 * Using JUNIT for Unit testing  and Espresso for instrumented tests
 * Created by bachiri on 2/11/17.
 */

public class AlarmUtils {


    //TODO create UNIT TEST
    public static void createFakeData(Context context) {


        AlarmSqlHelper alarmSqlHelper = new AlarmSqlHelper(context);


        List<Integer> days = new ArrayList<Integer>();
        days.add(2);
        days.add(6);
        days.add(3);
        List<Connection> connections = new ArrayList<Connection>();
        connections.add(Connection.BLUETOOTH);
        connections.add(Connection.HOTSPOT);
        //connections.add(Connection.CELLULAR_DATA);
        connections.add(Connection.WIFI);


        PreciseConnectivityAlarm preciseConnectivityAlarm = new PreciseConnectivityAlarm(System.currentTimeMillis() + 100, 10, days, connections);
        PreciseConnectivityAlarm preciseConnectivityAlarmOne = new PreciseConnectivityAlarm(System.currentTimeMillis() + 300, 10, days, connections);
        PreciseConnectivityAlarm preciseConnectivityAlarmTwo = new PreciseConnectivityAlarm(System.currentTimeMillis() + 500, 10, days, connections);

        //alarmSqlHelper.updateAlarmJob(1,1000);
        // alarmSqlHelper.updateAlarm(2,10000,1045);

        //alarmSqlHelper.updateAlarmConnection(1,Connection.CELLULAR_DATA,true);
        //alarmSqlHelper.updateAlarmDay(1,2,false);
        List<PreciseConnectivityAlarm> a= alarmSqlHelper.readAllAlarms(null,null);
        //PreciseConnectivityAlarm preciseConnectivityAlarm2 =  alarmSqlHelper.readNextAlarm();
        alarmSqlHelper.createAlarm(preciseConnectivityAlarm);
        alarmSqlHelper.createAlarm(preciseConnectivityAlarmOne);
        alarmSqlHelper.createAlarm(preciseConnectivityAlarmTwo);
        //AlarmNotificationManager.triggerNotification(context);


        //alarmSqlHelper.updateAlarmJob(1,1990);
        //alarmSqlHelper.updateAlarmCurrentState(1,true);
        //PreciseConnectivityAlarm preciseConnectivityAlarm3 = alarmSqlHelper.getAlarmByJobId(1990);
        //List<PreciseConnectivityAlarm> b= alarmSqlHelper.readAllAlarms(null,null);


    }

    public static Connection getFirstConnection(PreciseConnectivityAlarm alarm) {
        List<Connection> connectionList = alarm.getConnections();

        if (connectionList.contains(Connection.WIFI))
            return Connection.WIFI;

        if (connectionList.contains(Connection.HOTSPOT))
            return Connection.HOTSPOT;

        return Connection.BLUETOOTH;
    }

    /**
     * Method that gets the connection enum value from a string
     *
     * @param tag: String value for a connection option
     * @return
     */
    public static Connection getConnectionFromString(String tag) {
        switch (tag) {
            case Constants.WIFI_TAG:
                return Connection.WIFI;
            case Constants.CELLULAR_DATA_TAG:
                return Connection.CELLULAR_DATA;
            case Constants.HOTSPOT_TAG:
                return Connection.HOTSPOT;
            case Constants.BLUETOOTH_TAG:
                return Connection.BLUETOOTH;
            default:
                return null;
        }
    }

    public static String getStringFromConnection(Connection connection) {
        if (connection == Connection.WIFI)
            return Constants.WIFI_TAG;

        if (connection == Connection.HOTSPOT)
            return Constants.HOTSPOT_TAG;

        if (connection == Connection.BLUETOOTH)
            return Constants.BLUETOOTH_TAG;

        return "";
    }
}
