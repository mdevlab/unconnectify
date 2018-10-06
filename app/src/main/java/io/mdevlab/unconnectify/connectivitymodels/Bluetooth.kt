package io.mdevlab.unconnectify.connectivitymodels

import android.bluetooth.BluetoothAdapter


class Bluetooth private constructor() : Connectivity() {

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        BluetoothAdapter.getDefaultAdapter()
    }

    override fun enable() {
        if (!bluetoothAdapter.isEnabled) {
            bluetoothAdapter.enable()
        }
    }

    override fun disable() {
        if (bluetoothAdapter.isEnabled) {
            bluetoothAdapter.disable()
        }
    }

    companion object {
        private var instance: Bluetooth? = null
        fun getInstance(): Bluetooth {
            if (instance == null) {
                instance = Bluetooth()
            }
            return instance!!
        }
    }
}
