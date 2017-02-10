package io.mdevlab.unconnectify.connectivitymodels;

import android.bluetooth.BluetoothAdapter;

/**
 * This class handles connecting to and disconnecting from Bluetooth
 * <p>
 * Bluetooth implements the singleton design pattern since only one instance of this class is needed
 * to manage enabling/disabling Bluetooth
 * <p>
 * Created by husaynhakeem on 2/10/17.
 */

public class Bluetooth {

    private static Bluetooth instance = null;
    private BluetoothAdapter bluetoothAdapter = null;

    private Bluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public static Bluetooth getInstance() {
        if (instance == null)
            instance = new Bluetooth();
        return instance;
    }

    public void enable() {
        if (!bluetoothAdapter.isEnabled())
            bluetoothAdapter.enable();
    }

    public void disable() {
        if (bluetoothAdapter.isEnabled())
            bluetoothAdapter.disable();
    }
}
