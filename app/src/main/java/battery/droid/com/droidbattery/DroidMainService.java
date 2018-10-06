package battery.droid.com.droidbattery;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Robson on 12/08/2017.
 */

public class DroidMainService extends Service implements TextToSpeech.OnInitListener {

    private static TextToSpeech tts;
    private Context context;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onInit(int i) {

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        try {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        try {
            try {
                context = getBaseContext();
                tts = new TextToSpeech(context, this);
                tts.setLanguage(Locale.getDefault());
            } catch (Exception ex) {
                Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
            }
            registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        try {
            if (batteryReceiver != null) {
                unregisterReceiver(batteryReceiver);
            }
            if (tts != null) {
                tts.stop();
                tts.shutdown();
            }
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
            Intent intentService = new Intent(context, DroidMainService.class);
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
            Intent intentService = new Intent(context, DroidMainService.class);
            try {
                context.startService(intentService);
                DroidCommon.TimeSleep(1000);
            } catch (Exception ex) {
            }
        }
    }

    public static void ChamaSinteseVoz(Context context) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        try {
            boolean dispositivoConectado = DroidCommon.ObtemStatusDispositivoConectado(context);
            boolean dispositivoDesconectado = DroidCommon.ObtemStatusDispositivoDesconectado(context);
            if (DroidCommon.InformaDispositivoConectadoDesconectado) {
                if (dispositivoConectado) {
                    VozDispositivoConectado(context);
                } else if (dispositivoDesconectado) {
                    VozDispositivoDesConectado(context);
                }
            }
            if (dispositivoConectado) {
                if (DroidCommon.InformarBateriaCarregada(context)) {
                    VozBateriaCarregada(context);
                } else if (DroidCommon.InformarPercentualAtingidoMultiSelectPreference(context)) {
                    VozPercentualAgingidoMultiSelectPreference(context);
                }
            }
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
    }




    private static boolean isMyServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (DroidMainService.class.getName().equals(service.service.getClassName())) {
                Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " true");
                return true;
            }
        }
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " false");
        return false;
    }

    public static void VozBateriaCarregada(Context context) {
        Fala(context, DroidCommon.PreferenceFalaBateriaCarregada(context));
    }

    public static void VozPercentualAgingidoMultiSelectPreference(Context context) {
        Fala(context, DroidCommon.MultSelectPreferencePercentualAtingido(context) + " por cento");
    }

    public static void VozDispositivoConectado(Context context) {
        Fala(context, DroidCommon.PreferenceDispositivoConectado(context));
    }

    public static void VozDispositivoDesConectado(Context context) {
        Fala(context, DroidCommon.PreferenceDispositivoDesconectado(context));
    }

    private static void Fala(Context context, String texto) {
        if (DroidCommon.SinteseVozNaoPerturbeAtivado(context)) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
            Toast.makeText(context, texto, Toast.LENGTH_SHORT).show();
            tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null);
            AguardandoFalar();
        }
        //stopSelf();
    }

    private static void AguardandoFalar() {
        while (tts.isSpeaking()) {
            DroidCommon.TimeSleep(500);
        }
        //stopSelf();
    }

    public static BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                int level = intent.getIntExtra("level", 0);
                String battery = String.valueOf(level);
                Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " " + battery);
                boolean alterouBateria = !DroidCommon.BatteryCurrent.contains(battery);
                boolean bateria100 = battery.equals("100");
                if (alterouBateria || (bateria100 && !DroidCommon.BateriaCarregada)) {
                    DroidCommon.BatteryCurrent = battery;
                    boolean bateriaCarregada = false;
                    if (bateria100) {
                        int statusBateria = DroidCommon.ObtemStatusBateria(context);
                        bateriaCarregada = statusBateria == BatteryManager.BATTERY_STATUS_FULL;
                    }
                    if (alterouBateria || bateriaCarregada) {
                        DroidCommon.AtualizaCorBateria(context);
                        ChamaSinteseVoz(context);
                    }
                }
            } catch (Exception ex) {
                Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
            }
        }
    };
}
