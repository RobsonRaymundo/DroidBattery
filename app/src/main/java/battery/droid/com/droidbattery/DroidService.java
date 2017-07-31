package battery.droid.com.droidbattery;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

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
    private Context context;

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        registerReceiver( DroidInfoBattery.batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED) );
        Log.d("DroidBattery", "DroidService - onStart " );
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("DroidBattery", "DroidService - onCreate " );
        try {
            context = getBaseContext();
            tts = new TextToSpeech(context, this);
            tts.setLanguage(Locale.getDefault());
        }
        catch (Exception ex)
        {
            Log.d("DroidBattery", "DroidService - onCreate - Erro: " + ex.getMessage() );
        }
    }

    @Override
    public void onDestroy() {
        Log.d("DroidBattery", "DroidService - onDestroy " );
        //Toast.makeText(this, "DroidBattery - DroidService - onDestroy ", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("DroidBattery", "DroidService - onUnbind " );
        //Toast.makeText(this, "DroidBattery - DroidService - onUnbind ", Toast.LENGTH_LONG).show();
        return super.onUnbind(intent);
    }

    @Override
    public void onLowMemory() {
        Log.d("DroidBattery", "DroidService - onLowMemory " );
        //Toast.makeText(this, "DroidBattery - DroidService - onLowMemory ", Toast.LENGTH_LONG).show();
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



}
