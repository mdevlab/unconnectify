package io.mdevlab.unconnectify.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.format.DateFormat;

import java.util.Calendar;

/**
 * Created by mdevlab on 2/12/17.
 */


/**
 * this function is the main class for displaying the time picker fragement
 */
public class TimePickerFragment extends AppCompatDialogFragment {

    //The callback interface used to indicate the user is done filling in the time
    private TimePickerDialog.OnTimeSetListener mListener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current time as the default values for the picker
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), mListener, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    /**
     * Set the TimePickerListener of  DialogFragment
     *
     * @param mListener listener
     */
    public void setListener(TimePickerDialog.OnTimeSetListener mListener) {
        this.mListener = mListener;
    }
}
