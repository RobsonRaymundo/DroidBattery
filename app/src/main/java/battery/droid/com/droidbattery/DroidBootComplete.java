package battery.droid.com.droidbattery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Robson on 03/05/2017.
 */

public class DroidBootComplete extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DroidService.StopStartService(context);
        Log.d("DroidBattery", "DroidBootComplete - onReceive ");
    }
}
