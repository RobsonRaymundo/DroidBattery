package battery.droid.com.droidbattery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Robson on 13/08/2017.
 */

public class DroidRestartService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentService = new Intent(context, DroidServiceScreen.class);
        context.startService(intentService);
        DroidCommon.TimeSleep(1000);
        Log.d("DroidBattery", "DroidRestartService - onReceive ");

        DroidWidget.onAppWidgetOptionsChanged = true;
        DroidService.StopService(context);
        DroidCommon.TimeSleep(1000);
        DroidService.StartService(context);

    }
}
