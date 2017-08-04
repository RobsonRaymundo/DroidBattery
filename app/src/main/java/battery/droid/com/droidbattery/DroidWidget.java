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
      //  context.stopService(new Intent(context, DroidService.class));
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

    public void ListenerOnClick(Context context, AppWidgetManager appWidgetManager) {
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

    private void IHateSamsung(Context context, Intent intent){
        Log.d("DroidBattery", "DroidWidget - onReceive - IHateSamsung ");
        if (intent == null || intent.getAction() == null)
            return;

        if (intent.getAction().contentEquals("com.sec.android.widgetapp.APPWIDGET_RESIZE") &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            handleTouchWiz(context, intent);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
     //   IHateSamsung(context, intent);
        super.onReceive(context, intent);
        Log.d("DroidBattery", "DroidWidget - onReceive ");
        onAppWidgetOptionsChanged = true;

        if (ACTION_BATTERY_UPDATE.equals(intent.getAction())) {
            updateViewsColorBattery(context, Color.RED);
            DroidService.loopingBattery = true;
            DroidService.StopStartService(context);
            Vibrar(context, 100);
            updateViewsColorBattery(context, Color.WHITE);
        }
    }


    @TargetApi(16)
    private void handleTouchWiz(Context context, Intent intent) {
        Log.d("DroidBattery", "DroidWidget - handleTouchWiz ");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        int appWidgetId = intent.getIntExtra("widgetId", 0);
        int widgetSpanX = intent.getIntExtra("widgetspanx", 0);
        int widgetSpanY = intent.getIntExtra("widgetspany", 0);

        if (appWidgetId > 0 && widgetSpanX > 0 && widgetSpanY > 0) {
            Bundle newOptions = new Bundle();
            // We have to convert these numbers for future use
            newOptions.putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, widgetSpanY * 74);
            newOptions.putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, widgetSpanX * 74);

            onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
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

    private void Vibrar(Context context, int valor) {
        try {
            TimeSleep(100);
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(valor);
            TimeSleep(100);
        } catch (Exception ex) {
            Log.d("DroidBattery", "Vibrar: " + ex.getMessage());
        }
    }

    private void TimeSleep(Integer seg) {
        try {
            Thread.sleep(seg);
        } catch (Exception ex) {
        }
    }

    private void updateViewsColorBattery(Context context, int color) {
        Log.d("DroidBattery", "DroidWidget - updateViews()");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        views.setTextColor(R.id.batteryText, color);

        ComponentName componentName = new ComponentName(context, DroidWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(componentName, views);
    }
}
