package io.mdevlab.unconnectify.connectivitymodels

import android.content.Context
import android.net.wifi.WifiManager


class Wifi private constructor(context: Context) : Connectivity() {

    private val wifiManager: WifiManager by lazy {
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    override fun enable() {
        if (!wifiManager.isWifiEnabled) {
            wifiManager.isWifiEnabled = true
        }
    }

    override fun disable() {
        if (wifiManager.isWifiEnabled) {
            wifiManager.isWifiEnabled = false
        }
    }

    companion object {
        private var instance: Wifi? = null
        fun getInstance(context: Context): Wifi {
            if (instance == null) {
                instance = Wifi(context)
            }
            return instance!!
        }
    }
}
