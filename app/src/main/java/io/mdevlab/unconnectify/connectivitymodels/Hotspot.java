package io.mdevlab.unconnectify.connectivitymodels;

import android.content.Context;
import android.net.wifi.WifiManager;

import cc.mvdan.accesspoint.WifiApControl;

/**
 * This class handles connecting to and disconnecting from Hotspot
 * <p>
 * Hotspot implements the singleton design pattern since only one instance of this class is needed
 * to manage enabling/disabling of Hotspot
 * <p>
 * For WifiApControl we use accesspoint Library  developed by mvdan
 * Enabling, disabling and configuring of wireless Access Points are all unaccessible in the SDK behind hidden methods in WifiManager .
 * Reflection is used to get access to those methods.
 * <p>
 * <p>
 * Created by mdevlab on 2/10/17.
 */


public class Hotspot extends  Connectivity{


    private static Hotspot hotspot = null;
    private static WifiManager wifiManager;
    private static WifiApControl apControl;


    private Hotspot(Context context) {
        this.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        apControl = WifiApControl.getInstance(context);
    }

    public static Hotspot getInstance(Context context) {
        if (hotspot == null) {
            hotspot = new Hotspot(context);

        }
        return hotspot;
    }

    //TODO Bachiri Make Sure to Verify apControl is'nt empty befor you run below methods
    public void disable() {
        apControl.disable();
    }


    public void enable() {
        // Wifi must be disabled to enable the access point
        if (wifiManager.isWifiEnabled())
            wifiManager.setWifiEnabled(false);
        apControl.enable();


    }


}
