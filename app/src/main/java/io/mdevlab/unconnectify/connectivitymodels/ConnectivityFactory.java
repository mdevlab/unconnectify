package io.mdevlab.unconnectify.connectivitymodels;

/**
 * Created by mdevlab on 2/11/17.
 */

import android.content.Context;

import io.mdevlab.unconnectify.utils.Connection;

/**
 * This class is the factory of connectivity objects
 * we create object without exposing the creation logic to the client
 * actually this is the implementation of factory Design pattern
 */
public class ConnectivityFactory {

    /**
     * this the main entry to ConnectivityFactory to get a connectivity object
     *
     * @param connection type of connect object to be created
     * @param context    the context which the context take to create the connectivity object
     * @return
     */
    public static Connectivity getConnectivity(Connection connection, Context context) {

        //if we provide a null connection nothing to return
        if (connection == null) {
            return null;
        }
        /*
        we rely on switch case to get the right connectivity object
         -possible cases:
        1-WIFI
        2-HOTSPOT
        3-CELLULAR_DATA
        4-BLUETOOTH
        */
        switch (connection) {

            case WIFI:
                return Wifi.getInstance(context);

            case HOTSPOT:
                return Hotspot.getInstance(context);

            case CELLULAR_DATA:
                return CellularData.getInstance(context);

            case BLUETOOTH:
                return Bluetooth.getInstance();


        }

        return null;

    }
}
