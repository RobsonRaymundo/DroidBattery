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
        DroidCommon.updateViewsSizeBattery(context);
        DroidService.StartService(context);
        Log.d("DroidBattery", "DroidBootComplete - onReceive ");
        DroidCommon.onUpdateDroidWidget(context);
    }


}
