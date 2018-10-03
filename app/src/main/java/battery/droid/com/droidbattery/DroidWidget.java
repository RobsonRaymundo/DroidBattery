package battery.droid.com.droidbattery;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;

import java.util.Locale;

/**
 * Created by Robson on 02/05/2017.
 */

public class DroidWidget extends AppWidgetProvider {
    private static final String ACTION_BATTERY_UPDATE = "battery.droid.com.droidbattery.UPDATE";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        try {
            RemoteViews updateViews =
                    new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            String msg =
                    String.format(Locale.getDefault(),
                            "%d-%d",
                            newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT),
                            newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH));

            //updateViews.setTextViewText(R.id.batteryText, msg);

            DroidCommon.SetInteger(context, "MIN_WIDTH", newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH));

            if (newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH) > 110) {
                updateViews.setTextViewTextSize(R.id.batteryText, TypedValue.COMPLEX_UNIT_DIP, 50);
            } else {
                updateViews.setTextViewTextSize(R.id.batteryText, TypedValue.COMPLEX_UNIT_DIP, 30);
            }

            appWidgetManager.updateAppWidget(appWidgetId, updateViews);
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        ListenerOnClick(context, appWidgetManager);
    }

    private void ListenerOnClick(Context context, AppWidgetManager appWidgetManager) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        try {
            RemoteViews remoteViews;
            ComponentName watchWidget;

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            watchWidget = new ComponentName(context, DroidWidget.class);

            remoteViews.setOnClickPendingIntent(R.id.batteryText, getPendingSelfIntent(context, ACTION_BATTERY_UPDATE));
            appWidgetManager.updateAppWidget(watchWidget, remoteViews);

        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }

    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        try {
            if (ACTION_BATTERY_UPDATE.equals(intent.getAction())) {
                DroidCommon.Vibrar(context, 50);
                DroidMainService.loopingBattery = true;
                DroidCommon.updateViewsInfoBattery(context, "0");
                DroidMainService.AtualizaBateria(context);
                DroidMainService.ChamaSinteseVoz(context);
            }
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
    }
}
