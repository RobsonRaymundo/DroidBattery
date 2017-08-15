package battery.droid.com.droidbattery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Robson on 03/05/2017.
 */

public class DroidPowerDisconnected extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DroidCommon.isCharging = false;
        DroidCommon.updateViewsColorBattery(context, Color.WHITE);
        DroidService.StartService(context);
        Log.d("DroidBattery", "DroidPowerDisconnected - onReceive ");
    }
}
