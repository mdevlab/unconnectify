package io.mdevlab.unconnectify.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.format.DateFormat;

import java.util.Calendar;

import io.mdevlab.unconnectify.MainActivity;
import io.mdevlab.unconnectify.utils.Constants;

/**
 * Created by mdevlab on 2/12/17.
 */


/**
 * this function is the main class for displaying the time picker fragment
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), mListener, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

        // Get this fragment's extras (arguments)
        final Bundle extras = getArguments();

        /**
         * The extras indicate whether this time picker is for the end time
         * If it is, a "deactivate" button is added to the time picker
         * If this new button is clicked upon, the end time is disabled
         */
        if (extras != null
                && extras.containsKey(Constants.END_TIME_BUNDLE_KEY)
                && extras.containsKey(Constants.ALARM_POSITION))
            timePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Deactivate", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((MainActivity) getActivity()).disableEndTime(extras.getInt(Constants.ALARM_POSITION));
                }
            });

        return timePickerDialog;
    }

    /**
     * Set the TimePickerListener of DialogFragment
     *
     * @param mListener listener
     */
    public void setListener(TimePickerDialog.OnTimeSetListener mListener) {
        this.mListener = mListener;
    }
}
