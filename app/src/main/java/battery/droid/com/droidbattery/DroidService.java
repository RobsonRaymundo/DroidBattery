package battery.droid.com.droidbattery;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Robson on 02/05/2017.
 */

public class DroidService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        registerReceiver( DroidInfoBattery.batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED) );
    }
}
