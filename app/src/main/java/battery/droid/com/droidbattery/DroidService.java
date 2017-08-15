package battery.droid.com.droidbattery;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * Created by Robson on 02/05/2017.
 */

public class DroidService extends Service implements TextToSpeech.OnInitListener {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static TextToSpeech tts;
    public static boolean loopingBattery;
    private Context context;
    public static Intent mServiceIntent;

    public static void StopStartService(Context context) {
        Intent intentService = new Intent(context, DroidService.class);
        try {
            context.stopService(intentService);
            Log.d("DroidBattery", "DroidService - StopService ");
        } catch (Exception ex) {
            Log.d("DroidBattery", "DroidService - StopService - Erro " + ex.getMessage());
        }
        try {
            context.startService(intentService);
            Log.d("DroidBattery", "DroidService - StartService ");

        } catch (Exception ex) {
            Log.d("DroidBattery", "DroidService - StartService - Erro " + ex.getMessage());
        }
    }


    public static void StopService(Context context) {
        if (isMyServiceRunning(context)) {
            Intent intentService = new Intent(context, DroidService.class);
            try {
                context.stopService(intentService);
            } catch (Exception ex) {
            }
            Log.d("DroidBattery", "DroidService - StopService ");
        }
    }

    public static void StartService(Context context) {
        if (!isMyServiceRunning(context)) {
            Intent intentService = new Intent(context, DroidService.class);
            try {
                context.startService(intentService);
            } catch (Exception ex) {
            }
            Log.d("DroidBattery", "DroidService - StartService ");
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        mServiceIntent = intent;
        registerReceiver(DroidInfoBattery.batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        Log.d("DroidBattery", "DroidService - onStart ");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("DroidBattery", "DroidService - onCreate ");
        try {
            context = getBaseContext();
            tts = new TextToSpeech(context, this);
            tts.setLanguage(Locale.getDefault());
        } catch (Exception ex) {
            Log.d("DroidBattery", "DroidService - onCreate - Erro: " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.d("DroidBattery", "DroidService - onDestroy ");
        //Toast.makeText(this, "DroidBattery - DroidService - onDestroy ", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("DroidBattery", "DroidService - onUnbind ");
        //Toast.makeText(this, "DroidBattery - DroidService - onUnbind ", Toast.LENGTH_LONG).show();
        DroidCommon.updateViewsInfoBattery(context, "Unbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onLowMemory() {
        Log.d("DroidBattery", "DroidService - onLowMemory ");
        //Toast.makeText(this, "DroidBattery - DroidService - onLowMemory ", Toast.LENGTH_LONG).show();
        DroidCommon.updateViewsInfoBattery(context, "LowMemory");
        super.onLowMemory();
    }

    @Override
    public boolean stopService(Intent name) {
        tts.shutdown();
        return super.stopService(name);

    }

    @Override
    public void onInit(int status) {

    }

    @Override
    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        DroidCommon.updateViewsInfoBattery(context, "dump");
        super.dump(fd, writer, args);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        DroidCommon.updateViewsInfoBattery(context, "TaskRemoved");
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onTrimMemory(int level) {
        DroidCommon.updateViewsInfoBattery(context, "TrimMemory");
        super.onTrimMemory(level);
    }


    public static boolean isMyServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (DroidService.class.getName().equals(service.service.getClassName())) {
                Log.d("DroidBattery", "isMyServiceRunning - true ");
                return true;
            }
        }
        Log.d("DroidBattery", "isMyServiceRunning - false ");
        return false;
    }

}
