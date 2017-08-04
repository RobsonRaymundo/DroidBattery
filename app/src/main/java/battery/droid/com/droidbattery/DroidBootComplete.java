package battery.droid.com.droidbattery;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;

/**
 * Created by Robson on 03/05/2017.
 */

public class DroidBootComplete extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        updateViewsSizeBattery(context);
        DroidService.StopStartService(context);
        Log.d("DroidBattery", "DroidBootComplete - onReceive ");

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

    private void updateViewsSizeBattery(Context context) {
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
}
