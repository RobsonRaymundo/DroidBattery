package battery.droid.com.droidbattery;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Locale;

/**
 * Created by Robson on 02/05/2017.
 */

public class DroidService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static boolean loopingBattery;
    private Context context;
    public static Intent mServiceIntent;

    public static void StopService(Context context) {
        if (isMyServiceRunning(context)) {
            Intent intentService = new Intent(context, DroidService.class);
            try {
                context.stopService(intentService);
            } catch (Exception ex) {
                Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
            }
        }
    }

    public static void StartService(Context context) {
        if (!isMyServiceRunning(context)) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
            Intent intentService = new Intent(context, DroidService.class);
            try {
                context.startService(intentService);
            } catch (Exception ex) {
            }
        }
    }

    private void SetStatusBattery(Intent batteryStatus) {
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean charging = status == BatteryManager.BATTERY_STATUS_CHARGING;
        boolean chargingFull = status == BatteryManager.BATTERY_STATUS_FULL;
        DroidCommon.isCharging = charging || chargingFull;
        if (charging) {
            DroidCommon.updateViewsColorBattery(context, Color.GREEN);
        } else if (chargingFull) {
            DroidCommon.updateViewsColorBattery(context, Color.BLUE);
        } else {
            DroidCommon.updateViewsColorBattery(context, Color.WHITE);
        }
        //Toast.makeText(context, String.valueOf(DroidCommon.isCharging), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        mServiceIntent = intent;
        SetStatusBattery(registerReceiver(DroidInfoBattery.batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED)));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        try {
            context = getBaseContext();
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        //Toast.makeText(this, "DroidBattery - DroidService - onDestroy ", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    private static boolean isMyServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (DroidService.class.getName().equals(service.service.getClassName())) {
                Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " true");
                return true;
            }
        }
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " false");
        return false;
    }

}
