package io.mdevlab.unconnectify.connectivitymodels

import android.content.Context

import io.mdevlab.unconnectify.utils.Connection


object ConnectivityFactory {

    private val connectionMap = mutableMapOf<Connection, Connectivity>()

    fun get(connection: Connection?, context: Context): Connectivity? {
        return when (connection) {
            Connection.BLUETOOTH -> connectionMap.getOrPut(Connection.BLUETOOTH, { Bluetooth.getInstance() })
            Connection.CELLULAR_DATA -> connectionMap.getOrPut(Connection.CELLULAR_DATA, { CellularData.getInstance(context) })
            Connection.HOTSPOT -> connectionMap.getOrPut(Connection.HOTSPOT, { Hotspot.getInstance(context) })
            Connection.WIFI -> connectionMap.getOrPut(Connection.WIFI, { Wifi.getInstance(context) })
            else -> null
        }
    }
}
