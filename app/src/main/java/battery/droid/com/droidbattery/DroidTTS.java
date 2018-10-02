package battery.droid.com.droidbattery;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Robson on 22/09/2017.
 */

public class DroidTTS extends Service implements TextToSpeech.OnInitListener {
    private TextToSpeech tts;
    private Context context;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onInit(int i) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        try {
            boolean ativarSinteseVoz = DroidCommon.PreferenceAtivarSinteseVoz(context);
            boolean naoPerturbe = DroidCommon.NaoPertube(context);
            String msg = "";
            if (ativarSinteseVoz && naoPerturbe) {
                if (DroidCommon.InformarBateriaCarregada(context)) {
                    VozBateriaCarregada();
                } else if (DroidCommon.InformarPercentualAtingidoMultiSelectPreference(context)) {
                    VozPercentualAgingidoMultiSelectPreference();
                } else {
                    if (DroidCommon.DispositivoConectado) {
                        VozDispositivoConectado();
                    } else {
                        VozDispositivoDesConectado();
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

    @Override
    public void onCreate() {
        super.onCreate();
        context = getBaseContext();
        tts = new TextToSpeech(context, this);
        tts.setLanguage(Locale.getDefault());
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    private void VozBateriaCarregada() {
        Fala(DroidCommon.PreferenceFalaBateriaCarregada(context));
    }

    private void VozPercentualAgingidoMultiSelectPreference() {
        Fala(DroidCommon.MultSelectPreferencePercentualAtingido(context) + " por cento");
    }

    private void VozDispositivoConectado() {
        Fala(DroidCommon.PreferenceDispositivoConectado(context));
        //DroidCommon.DispositivoConectado = false;
    }

    private void VozDispositivoDesConectado() {
        Fala(DroidCommon.PreferenceDispositivoDesconectado(context));
        //DroidCommon.DispositivoDesConectado = false;
    }

    private void Fala(String texto) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        Toast.makeText(context, texto, Toast.LENGTH_SHORT).show();
        tts.speak(texto, TextToSpeech.QUEUE_FLUSH, null);
        AguardandoFalar();
        stopSelf();
    }

    private void AguardandoFalar() {
        while (tts.isSpeaking()) {
            DroidCommon.TimeSleep(500);
        }
        stopSelf();
    }

    public static void StopService(Context context) {
        if (isMyServiceRunning(context)) {
            Intent intentService = new Intent(context, DroidTTS.class);
            try {
                context.stopService(intentService);
                DroidCommon.TimeSleep(500);
            } catch (Exception ex) {
                Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
            }
        }
    }

    public static void StartService(Context context) {
        if (!isMyServiceRunning(context)) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
            Intent intentService = new Intent(context, DroidTTS.class);
            try {
                context.startService(intentService);
            } catch (Exception ex) {
            }
        }
    }

    private static boolean isMyServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (DroidTTS.class.getName().equals(service.service.getClassName())) {
                Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " true");
                return true;
            }
        }
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " false");
        return false;
    }
}
