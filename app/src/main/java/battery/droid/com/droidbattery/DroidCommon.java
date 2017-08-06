package battery.droid.com.droidbattery;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;

/**
 * Created by Robson on 04/08/2017.
 */

public class DroidCommon {

    public static String handleTime(Context context, String time) {
        String[] timeParts=time.split(":");
        int lastHour=Integer.parseInt(timeParts[0]);
        int lastMinute=Integer.parseInt(timeParts[1]);

        boolean is24HourFormat = DateFormat.is24HourFormat(context);

        if(is24HourFormat) {
            return ((lastHour < 10) ? "0" : "")
                    + Integer.toString(lastHour)
                    + ":" + ((lastMinute < 10) ? "0" : "")
                    + Integer.toString(lastMinute);
        } else {
            int myHour = lastHour % 12;
            return ((myHour == 0) ? "12" : ((myHour < 10) ? "0" : "") + Integer.toString(myHour))
                    + ":" + ((lastMinute < 10) ? "0" : "")
                    + Integer.toString(lastMinute)
                    + ((lastHour >= 12) ? " PM" : " AM");
        }
    }

    public static void onUpdateDroidWidget(Context context)
    {
        try {
            DroidWidget droidWidget = new DroidWidget();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            droidWidget.onUpdate(context, appWidgetManager, null);
        }
        catch (Exception ex)
        {
            Log.d("DroidBattery", "DroidBootComplete - onReceive - Erro: " + ex.getMessage());
        }
    }

    public static void updateViewsSizeBattery(Context context) {
        Log.d("DroidBattery", "DroidInfoBattery - updateViews()");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        Integer min_width = DroidPreferences.GetInteger(context, "MIN_WIDTH");

        if (min_width > 110)
        {
            views.setTextViewTextSize(R.id.batteryText, TypedValue.COMPLEX_UNIT_DIP , 50);
        }
        else
        {
            views.setTextViewTextSize(R.id.batteryText, TypedValue.COMPLEX_UNIT_DIP , 30);
        }

        ComponentName componentName = new ComponentName(context, DroidWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(componentName, views);
    }

    public static boolean InformarBateriaCarregada(final Context context) {
        boolean spf = false;
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            spf = sp.getBoolean("spf_bateriaCarregada", true);
        } catch (Exception ex) {
            Log.d("DroidInfoBattery", ex.getMessage());
        }
        return spf;
    }

    public static void updateViewsInfoBattery(Context context, String batteryLevel) {
        Log.d("DroidBattery", "DroidInfoBattery - updateViews()");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        views.setTextViewText(R.id.batteryText, batteryLevel);

        ComponentName componentName = new ComponentName(context, DroidWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(componentName, views);
    }

    public static void TimeSleep(Integer seg) {
        try {
            Thread.sleep(seg);
        } catch (Exception ex) {
        }
    }

    public static void Vibrar(Context context, int valor) {
        try {
            TimeSleep(100);
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(valor);
            TimeSleep(100);
        } catch (Exception ex) {
            Log.d("DroidBattery", "Vibrar: " + ex.getMessage());
        }
    }

    public static void updateViewsColorBattery(Context context, int color) {
        Log.d("DroidBattery", "DroidWidget - updateViews()");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        views.setTextColor(R.id.batteryText, color);
        Integer min_width = DroidPreferences.GetInteger(context, "MIN_WIDTH");
        if (min_width > 110)
        {
            views.setTextViewTextSize(R.id.batteryText, TypedValue.COMPLEX_UNIT_DIP , 50);
        }
        else
        {
            views.setTextViewTextSize(R.id.batteryText, TypedValue.COMPLEX_UNIT_DIP , 30);
        }
        ComponentName componentName = new ComponentName(context, DroidWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(componentName, views);
    }
}
