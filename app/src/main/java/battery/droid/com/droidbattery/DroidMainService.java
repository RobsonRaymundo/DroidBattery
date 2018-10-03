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
    public static boolean loopingBattery;

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
            boolean ativarSinteseVoz = DroidCommon.PreferenceAtivarSinteseVoz(context);
            boolean naoPerturbe = DroidCommon.NaoPertube(context);
            String msg = "";
            if (ativarSinteseVoz && naoPerturbe) {
                if (DroidCommon.InformarBateriaCarregada(context)) {
                    VozBateriaCarregada(context);
                } else if (DroidCommon.InformarPercentualAtingidoMultiSelectPreference(context)) {
                    VozPercentualAgingidoMultiSelectPreference(context);
                } else {
                    if (DroidCommon.InformaDispositivoConectado) {
                        if (DroidCommon.DispositivoConectado) {
                            VozDispositivoConectado(context);
                        } else if (DroidCommon.DispositivoDesConectado) {
                            VozDispositivoDesConectado(context);
                        }
                    }
                }
            } else {
                if (!ativarSinteseVoz) msg = "Sintese de Voz não ativado";
                if (!naoPerturbe) {
                    if (!msg.isEmpty()) {
                        msg = msg + " e ";
                    }
                    msg = msg + "Não perturbe ativado";
                }
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
    }

    public static void AtualizaBateria(Context context) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " " + DroidCommon.BatteryStatus);
        if (DroidMainService.loopingBattery) {
            DroidMainService.loopingBattery = false;
            Integer totalBattery = Integer.parseInt(DroidCommon.BatteryCurrent);
            for (Integer i = 0; i <= totalBattery; i++) {
                DroidCommon.TimeSleep(1);
                DroidCommon.updateViewsInfoBattery(context, i.toString());
            }
        } else {
            DroidCommon.updateViewsInfoBattery(context, DroidCommon.BatteryCurrent);
        }
        if (DroidCommon.DispositivoConectado) {
            if (DroidCommon.BatteryStatus == BatteryManager.BATTERY_STATUS_FULL) {
                DroidCommon.updateViewsColorBattery(context, Color.GREEN);
            } else DroidCommon.updateViewsColorBattery(context, Color.BLUE);
        } else {
            Integer totalBattery = Integer.parseInt(DroidCommon.BatteryCurrent);
            if (totalBattery <= 20) {
                DroidCommon.updateViewsColorBattery(context, Color.RED);
            } else {
                DroidCommon.updateViewsColorBattery(context, Color.WHITE);
            }
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
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        Toast.makeText(context, texto, Toast.LENGTH_SHORT).show();
        tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null);
        AguardandoFalar();
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
                DroidCommon.BatteryStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " " + battery + " " + DroidCommon.BatteryStatus + " " + DroidCommon.BatteryFull);
                if ((!DroidCommon.BatteryCurrent.contains(battery)) ||
                        (DroidCommon.BatteryStatus == BatteryManager.BATTERY_STATUS_FULL && !DroidCommon.BatteryFull)) {
                    if (DroidCommon.BatteryStatus == BatteryManager.BATTERY_STATUS_FULL) {
                        DroidCommon.BatteryFull = true;
                    } else DroidCommon.BatteryFull = false;
                    DroidCommon.BatteryCurrent = battery;
                    AtualizaBateria(context);
                    ChamaSinteseVoz(context);
                }
            } catch (Exception ex) {
                Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
            }
        }
    };
}
