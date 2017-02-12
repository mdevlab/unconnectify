package io.mdevlab.unconnectify.jobs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;

import io.mdevlab.unconnectify.connectivitymodels.Bluetooth;
import io.mdevlab.unconnectify.connectivitymodels.Connectivity;
import io.mdevlab.unconnectify.connectivitymodels.ConnectivityFactory;
import io.mdevlab.unconnectify.connectivitymodels.Hotspot;
import io.mdevlab.unconnectify.connectivitymodels.Wifi;
import io.mdevlab.unconnectify.utils.Connection;
import io.mdevlab.unconnectify.utils.Constants;

/**
 * Created by mdevlab on 2/10/17.
 */

public class ConnectivityJobOld extends Job {

    private Context mContext;
    private String mTag;

    /**
     * @param context
     * @param tag:    String indicating the type of connectivity
     */
    public ConnectivityJobOld(Context context, String tag) {
        this.mContext = context;
        this.mTag = tag;
    }

    /**
     * Method that's called when this class is instantiated
     * It basically runs a job to enable/disable a connection
     *
     * @param params: bundle containing data passed on from the job request
     * @return: In general the result of the job this method runs.
     * But for our case SUCCESS is always returned.
     */
    @Override
    @NonNull
    protected Result onRunJob(Params params) {
        /**
         * The connectivity factory class is used here in order to receive a base connectivity
         * object. This object represents one of the supported connectivity models:
         * wifi, hotspot and bluetooth
         */
        Connectivity connectivity = ConnectivityFactory.getConnectivity(getConnectionFromString(mTag), mContext);

        /**
         * This boolean variable 'activate' is used to determine whether to activate or
         * deactivate the connectivity option (wifi, bluetooth, etc).
         * The value of this variable is retrieved from the argument 'params'
         * If no value is found in params's extras, the default value of 'false'
         * is given to it
         */
        boolean activate = params.getExtras().getBoolean(Constants.ACTIVATE_TAG, false);

        /**
         * The connectivity object along with the boolean variable 'activate' are
         * passed on to the method below allowing to enable/disable connection accordingly
         */
        handleConnectivityJob(connectivity, activate);

        return Result.SUCCESS;
    }

    /**
     * Method that handles enabling or disabling a connectivity option based
     * on a parameter passed on to it
     *
     * @param connectivity: Base object for one of the types of connectivity.
     *                      It's used to enable/disable connectivity
     * @param activate:     Boolean variable indicating whether to enable or disable connection
     */
    public void handleConnectivityJob(Connectivity connectivity, boolean activate) {
        if (connectivity instanceof Wifi) {
            if (activate)
                Wifi.getInstance(mContext).enable();
            else
                Wifi.getInstance(mContext).disable();
        } else if (connectivity instanceof Hotspot) {
            if (activate)
                Hotspot.getInstance(mContext).enable();
            else
                Hotspot.getInstance(mContext).disable();
        } else if (connectivity instanceof Bluetooth) {
            if (activate)
                Bluetooth.getInstance().enable();
            else
                Bluetooth.getInstance().disable();
        }
    }

    /**
     * Method that gets the connection enum value from a string
     *
     * @param tag: String value for a connection option
     * @return
     */
    private Connection getConnectionFromString(String tag) {
        switch (tag) {
            case Constants.WIFI_TAG:
                return Connection.WIFI;
            case Constants.CELLULAR_DATA_TAG:
                return Connection.CELLULAR_DATA;
            case Constants.HOTSPOT_TAG:
                return Connection.HOTSPOT;
            case Constants.BLUETOOTH_TAG:
                return Connection.BLUETOOTH;
            default:
                return null;
        }
    }
}
