package io.mdevlab.unconnectify.connectivitymodels;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Apparently cellular data (3G and 4G) is no longer available for enabling/disabling starting from lollipop
 * So cellular data will be set on hold for the moment
 * <p>
 * Created by mdevlab on 2/10/17.
 */

public class CellularData extends  Connectivity{

    private final String TAG = CellularData.class.getSimpleName();

    private static CellularData instance = null;
    private TelephonyManager mTelephonyManager = null;

    private CellularData(Context context) {
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public static CellularData getInstance(Context context) {
        if (instance == null)
            instance = new CellularData(context);
        return instance;
    }

    public void setMobileDataState(boolean mobileDataEnabled) {
        try {
            Method setMobileDataEnabledMethod = mTelephonyManager.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
            if (setMobileDataEnabledMethod != null)
                setMobileDataEnabledMethod.invoke(mTelephonyManager, mobileDataEnabled);
        } catch (Exception ex) {
            Log.e(TAG, "Error setting mobile data state", ex);
        }
    }

    public boolean getMobileDataState() {
        try {
            Method getMobileDataEnabledMethod = mTelephonyManager.getClass().getDeclaredMethod("getDataEnabled");
            if (getMobileDataEnabledMethod != null)
                return (Boolean) getMobileDataEnabledMethod.invoke(mTelephonyManager);
        } catch (Exception ex) {
            Log.e(TAG, "Error getting mobile data state", ex);
        }
        return false;
    }

    @Override
    protected void enable() {

    }

    @Override
    protected void disable() {

    }
}
