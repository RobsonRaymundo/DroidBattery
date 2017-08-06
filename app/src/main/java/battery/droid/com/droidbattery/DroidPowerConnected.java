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

public class DroidPowerConnected extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DroidCommon.isCharging = true;
        DroidCommon.updateViewsColorBattery(context, Color.GREEN);
        DroidService.StopStartService(context);
        Log.d("DroidBattery", "DroidPowerConnected - onReceive ");
    }
}
