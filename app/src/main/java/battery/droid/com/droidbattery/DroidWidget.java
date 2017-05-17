package battery.droid.com.droidbattery;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by Robson on 02/05/2017.
 */

public class DroidWidget extends AppWidgetProvider {
    private int batteryLevel = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String battery = calculateBatteryLevel(context);
        DroidConfigurationActivity.BatteryCurrent = battery;
   //     Log.d("DroidBattery", "onReceive: " + battery);
        DroidInfoBattery.updateViews(context, battery);
            //   DroidConfigurationActivity.ColorCurrent = DroidConfigurationActivity.ColorCurrent + 1;
    }

    private String calculateBatteryLevel(Context context) {
    //    Log.d("DroidBattery", "calculateBatteryLevel: " );
        Intent batteryIntent = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
        return String.valueOf(level * 100 / scale);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
  //      Log.d("DroidBattery", "onUpdate: " );
 //       DroidConfigurationActivity.ColorCurrent = 2;
    }
}
