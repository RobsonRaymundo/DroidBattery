package battery.droid.com.droidbattery;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetProviderInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Created by Robson on 02/05/2017.
 */

public class DroidWidget extends AppWidgetProvider {
    public static boolean onAppWidgetOptionsChanged;
    private static final String ACTION_BATTERY_UPDATE = "battery.droid.com.droidbattery.UPDATE";
    private int batteryLevel = 0;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        DroidService.StopStartService(context);
        Log.d("DroidBattery", "DroidWidget - onEnabled ");
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d("DroidBattery", "DroidWidget - onDesabled ");
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);

        Log.d("DroidBattery", "DroidWidget - onAppWidgetOptionsChanged ");
        onAppWidgetOptionsChanged = true;

        RemoteViews updateViews=
                new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        String msg=
                String.format(Locale.getDefault(),
                        "%d-%d",
                        newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT),
                        newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH));

        //updateViews.setTextViewText(R.id.batteryText, msg);

        DroidPreferences.SetInteger(context, "MIN_WIDTH", newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH));

        if (newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH) > 110)
        {
            updateViews.setTextViewTextSize(R.id.batteryText, TypedValue.COMPLEX_UNIT_DIP, 50);
        }
        else
        {
            updateViews.setTextViewTextSize(R.id.batteryText, TypedValue.COMPLEX_UNIT_DIP, 30);
        }

        appWidgetManager.updateAppWidget(appWidgetId, updateViews);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d("DroidBattery", "DroidWidget - onUpdate ");
        ListenerOnClick(context, appWidgetManager);
    }

    private void ListenerOnClick(Context context, AppWidgetManager appWidgetManager) {
        RemoteViews remoteViews;
        ComponentName watchWidget;

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        watchWidget = new ComponentName(context, DroidWidget.class);

        remoteViews.setOnClickPendingIntent(R.id.batteryText, getPendingSelfIntent(context, ACTION_BATTERY_UPDATE));
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);
        Log.d("DroidBattery", "DroidWidget - ListenerOnClick ");
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
     //   IHateSamsung(context, intent);
        super.onReceive(context, intent);
        Log.d("DroidBattery", "DroidWidget - onReceive ");
        onAppWidgetOptionsChanged = true;

        if (ACTION_BATTERY_UPDATE.equals(intent.getAction())) {
            DroidCommon.updateViewsColorBattery(context, Color.RED);
            DroidService.loopingBattery = true;
            DroidService.StopStartService(context);
            DroidCommon.Vibrar(context, 50);
            DroidCommon.updateViewsColorBattery(context, Color.WHITE);
        }
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
        Log.d("DroidBattery", "DroidWidget - onRestored ");
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d("DroidBattery", "DroidWidget - onRestored ");
    }
}
