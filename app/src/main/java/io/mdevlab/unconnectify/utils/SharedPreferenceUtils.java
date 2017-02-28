package io.mdevlab.unconnectify.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by bachiri on 2/28/17.
 */

/**
 * This Class is for Reading and writing to/from a sharedPreference
 */
public class SharedPreferenceUtils {

    //TODO create A generic method if we need to write/Read other Types of SharedPreferences

    /**
     * This method is for retrieving the shared preference related to the given file name
     * @param context the context
     * @param sharedPreferencefilekey shared preferences file
     * @return the SharedPreferences
     */
    public static  SharedPreferences getTheSharedPreference(Context context, String sharedPreferencefilekey){

        SharedPreferences sharedPref = context.getSharedPreferences(
                sharedPreferencefilekey, Context.MODE_PRIVATE);

        return  sharedPref;
    }

    /**
     * This function is for Writing to a shared preference a boolean Value
     *
     * @param sharedPref            the shared preference to write to
     * @param sharedPreferenceName  the name of the key
     * @param sharedPreferenceValue the value
     */
    public static void putBooleanPreference(SharedPreferences sharedPref, String sharedPreferenceName, Boolean sharedPreferenceValue) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(sharedPreferenceName, sharedPreferenceValue);
        editor.commit();

    }


    /**
     * This method is for reading from the given preference the value of the shared preference key
     *
     * @param sharedPref           the shared preference to write to
     * @param sharedPreferenceName the name of the key
     * @param defaultValue         the default value of the shared preference
     * @return the Value of the shared Preference
     */
    public Boolean getBooleanPreference(SharedPreferences sharedPref, String sharedPreferenceName, Boolean defaultValue) {

        Boolean sharedPreferenceValue = sharedPref.getBoolean(sharedPreferenceName, defaultValue);
        return sharedPreferenceValue;
    }
}
