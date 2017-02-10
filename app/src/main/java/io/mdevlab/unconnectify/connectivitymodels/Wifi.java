package io.mdevlab.unconnectify.connectivitymodels;

import android.content.Context;
import android.net.wifi.WifiManager;

/**
 * This class handles connecting to and disconnecting from a WIFI network
 * <p>
 * Wifi implements the singleton design pattern since only one instance of this class is needed
 * to manage enabling/disabling WIFI connection
 * <p>
 * Created by mdevlab on 2/10/17.
 */

public class Wifi {

    private static Wifi instance = null;
    private WifiManager mWifiManager = null;

    private Wifi(Context context) {
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public static Wifi getInstance(Context context) {
        if (instance == null)
            instance = new Wifi(context);
        return instance;
    }

    public void enable() {
        if (!mWifiManager.isWifiEnabled())
            mWifiManager.setWifiEnabled(true);
    }

    public void disable() {
        if (mWifiManager.isWifiEnabled())
            mWifiManager.setWifiEnabled(false);
    }
}
