package io.mdevlab.unconnectify.connectivitymodels;

import android.bluetooth.BluetoothAdapter;

/**
 * This class handles connecting to and disconnecting from Bluetooth
 * <p>
 * Bluetooth implements the singleton design pattern since only one instance of this class is needed
 * to manage enabling/disabling Bluetooth
 * <p>
 * Created by mdevlab on 2/10/17.
 */

public class Bluetooth extends Connectivity {

    private static Bluetooth instance = null;
    private BluetoothAdapter mBluetoothAdapter = null;

    private Bluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public static Bluetooth getInstance() {
        if (instance == null)
            instance = new Bluetooth();
        return instance;
    }

    public void enable() {
        if (!mBluetoothAdapter.isEnabled())
            mBluetoothAdapter.enable();
    }

    public void disable() {
        if (mBluetoothAdapter.isEnabled())
            mBluetoothAdapter.disable();
    }
}
