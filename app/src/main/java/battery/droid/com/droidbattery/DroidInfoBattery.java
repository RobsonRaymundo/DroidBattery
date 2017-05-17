package battery.droid.com.droidbattery;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Random;

/**
 * Created by Robson on 02/05/2017.
 */

public class DroidInfoBattery {
    public static BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);
            String battery = String.valueOf(level);
            if (DroidConfigurationActivity.BatteryCurrent != battery) {
                DroidConfigurationActivity.BatteryCurrent = battery;
                updateViews(context, battery);
             //   DroidConfigurationActivity.ColorCurrent = DroidConfigurationActivity.ColorCurrent + 1;
            }
        }
    };
    private static int randomColor() {
        switch (DroidConfigurationActivity.ColorCurrent) {
            case 0: {
                return Color.WHITE;
            }
            case 1: {
                return Color.GREEN;
            }
            case 2: {
                return Color.YELLOW;
            }
            case 3: {
                DroidConfigurationActivity.ColorCurrent = 0;
                return Color.RED;
            }
            default: {
                return Color.WHITE;
            }
        }
    }
    public static void updateViews(Context context, String batteryLevel ) {
       // Log.d("DroidBattery", "updateViews()" );
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        views.setTextViewText(R.id.batteryText, batteryLevel);
      //  views.setTextColor(R.id.batteryText, randomColor());
        ComponentName componentName = new ComponentName(context, DroidWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(componentName, views);
    }
}
