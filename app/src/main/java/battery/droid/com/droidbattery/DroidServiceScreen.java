package battery.droid.com.droidbattery;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Robson on 12/08/2017.
 */

public class DroidServiceScreen extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        try {
            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            BroadcastReceiver mReceiver = new DroidScreenOnOff();
            registerReceiver(mReceiver, filter);
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        try {
            DroidCommon.updateViewsColorBattery(getBaseContext(), Color.GRAY);
            Intent broadcastIntent = new Intent("battery.droid.com.droidbattery.ACTION_RESTART_SERVICE");
            sendBroadcast(broadcastIntent);
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));

        super.onStartCommand(intent, flags, startId);
        DroidCommon.TimeSleep(2000);
        return START_STICKY;
    }
}
