package battery.droid.com.droidbattery;

import android.app.ActivityManager;
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

    public static void StopService(Context context) {
        if (isMyServiceRunning(context)) {
            Intent intentService = new Intent(context, DroidServiceScreen.class);
            try {
                context.stopService(intentService);
                DroidCommon.TimeSleep(1000);
            } catch (Exception ex) {
                Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
            }
        }
    }


    public static void StartService(Context context) {
        if (!isMyServiceRunning(context)) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
            Intent intentService = new Intent(context, DroidServiceScreen.class);
            try {
                context.startService(intentService);
            } catch (Exception ex) {
            }
        }
    }

    private static boolean isMyServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (DroidServiceScreen.class.getName().equals(service.service.getClassName())) {
                Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " true");
                return true;
            }
        }
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " false");
        return false;
    }
}
