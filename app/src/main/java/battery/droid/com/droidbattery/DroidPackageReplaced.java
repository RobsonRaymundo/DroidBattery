package battery.droid.com.droidbattery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Robson on 03/05/2017.
 */

public class DroidPackageReplaced extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DroidCommon.updateViewsSizeBattery(context);
        DroidService.StopStartService(context);
        Log.d("DroidBattery", "DroidPackageReplaced - onReceive ");
    }
}
