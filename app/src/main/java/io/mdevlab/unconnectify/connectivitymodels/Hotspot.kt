package io.mdevlab.unconnectify.connectivitymodels

import android.content.Context
import android.net.wifi.WifiManager

import cc.mvdan.accesspoint.WifiApControl


/**
 * For WifiApControl we use accesspoint Library  developed by mvdan
 * Enabling, disabling and configuring of wireless Access Points are all unaccessible in the SDK behind hidden methods in WifiManager .
 * Reflection is used to get access to those methods.
 */
class Hotspot private constructor(context: Context) : Connectivity() {

    private val wifiManager: WifiManager by lazy {
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    private val apControl: WifiApControl by lazy {
        WifiApControl.getInstance(context)
    }

    /**
     * Enables the default hotspot, which requires the wifi to be turned off
     */
    override fun enable() {
        if (wifiManager.isWifiEnabled) {
            wifiManager.isWifiEnabled = false
        }
        apControl.enable()
    }

    override fun disable() {
        apControl.disable()
    }

    companion object {
        private var hotspot: Hotspot? = null
        fun getInstance(context: Context): Hotspot {
            if (hotspot == null) {
                hotspot = Hotspot(context)
            }
            return hotspot!!
        }
    }
}
