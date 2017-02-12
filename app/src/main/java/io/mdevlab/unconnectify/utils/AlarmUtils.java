package io.mdevlab.unconnectify.utils;

import android.app.NotificationManager;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.mdevlab.unconnectify.alarm.PreciseConnectivityAlarm;
import io.mdevlab.unconnectify.data.AlarmSqlHelper;
import io.mdevlab.unconnectify.notification.AlarmNotificationManager;

/**
 * This class is a temporary class for generating a fake data
 * The future plan is the have a detailed unit test cases
 * Using JUNIT for Unit testing  and Espresso for instrumented tests
 * Created by bachiri on 2/11/17.
 */

public class AlarmUtils {


    //TODO create UNIT TEST
    public static void createFakeData(Context context){



        AlarmSqlHelper alarmSqlHelper = new AlarmSqlHelper(context);



        //List<Integer> days = new ArrayList<Integer>();
        //days.add(2);
        // days.add(20);
        //days.add(3);
        //List<Connection> connections = new ArrayList<Connection>();
        //connections.add(Connection.BLUETOOTH);
        //connections.add(Connection.HOTSPOT);
        //connections.add(Connection.CELLULAR_DATA);
        //connections.add(Connection.WIFI);


        //PreciseConnectivityAlarm preciseConnectivityAlarm = new PreciseConnectivityAlarm(System.currentTimeMillis()+100,10,days,connections);

        //alarmSqlHelper.updateAlarmJob(1,1000);
        // alarmSqlHelper.updateAlarm(2,10000,1045);

        //alarmSqlHelper.updateAlarmConnection(1,Connection.CELLULAR_DATA,true);
        //alarmSqlHelper.updateAlarmDay(1,2,false);
        //List<PreciseConnectivityAlarm> a= alarmSqlHelper.readAllAlarms(null,null);
        //PreciseConnectivityAlarm preciseConnectivityAlarm2 =  alarmSqlHelper.readNextAlarm();
        //alarmSqlHelper.createAlarm(preciseConnectivityAlarm);
        //AlarmNotificationManager.triggerNotification(context);



        //alarmSqlHelper.updateAlarmJob(1,1990);
        //alarmSqlHelper.updateAlarmCurrentState(1,true);
        //PreciseConnectivityAlarm preciseConnectivityAlarm3 = alarmSqlHelper.getAlarmByJobId(1990);
        //List<PreciseConnectivityAlarm> b= alarmSqlHelper.readAllAlarms(null,null);




    }
}
