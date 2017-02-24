package io.mdevlab.unconnectify.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by mdevlab on 2/10/17.
 */

public class DialogUtils {

    /**
     * this method is used to redirect users to setting activity ,
     * user will be redirected to this setting activity until he/she provide the write access ,this function is called in devise runing 6.0+
     *
     * @param context the context is used for lunching the AlertDialog
     * @return true if we have the right to modify the system setting false if not yet
     */
    public static Boolean showDialog(final Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(context)) {
            new AlertDialog.Builder(context)
                    .setMessage("Allow reading/writing the system settings? Necessary to set up access points.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                            intent.setData(Uri.parse("package:" + context.getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }).show();
            return false;
        }
        return true;
    }
}
