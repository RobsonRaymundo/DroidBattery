package battery.droid.com.droidbattery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by Robson on 12/08/2017.
 */

public class DroidScreenOnOff extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            DroidService.StopService(context);
            Log.d("DroidBattery", "DroidScreenOnOff - onReceive - Off ");

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            DroidService.StartService(context);
            Log.d("DroidBattery", "DroidScreenOnOff - onReceive - On ");
        }
    }
}
