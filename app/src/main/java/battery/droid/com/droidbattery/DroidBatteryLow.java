package battery.droid.com.droidbattery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by Robson on 12/08/2017.
 */

public class DroidBatteryLow extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DroidCommon.updateViewsColorBattery(context, Color.RED);
        DroidService.StartService(context);
        Log.d("DroidBattery", "DroidBatteryLow - onReceive ");
    }
}
