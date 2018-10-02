package battery.droid.com.droidbattery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Robson on 12/08/2017.
 */

public class DroidScreenOnOff extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        try {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                if (!DroidCommon.isCharging) {
                    DroidService.StopService(context);
                    Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " OFF ");
                }

            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                DroidService.StartService(context);
                Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " ON ");
            }
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
    }
}
