package battery.droid.com.droidbattery;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
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
        if (DroidCommon.NaoPertube(context)) {

            if (DroidCommon.InformarBateriaCarregada(context)) {
                VozBateriaCarregada();
            }

            if (DroidCommon.DispositivoConectado) {
                VozDispositivoConectado();
            }

            if (DroidCommon.DispositivoDesConectado) {
                VozDispositivoDesConectado();
            }

        } else Toast.makeText(context, "Não perturbe ativado", Toast.LENGTH_SHORT).show();
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
        Fala("Bateria carregada, você já pode desconectar do carregador.");
    }

    private void VozDispositivoConectado() {
        Fala("Dispositivo conectado.");
    }

    private void VozDispositivoDesConectado() {
        Fala("Dispositivo desconectado.");
    }

    private void Fala(String texto) {
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
}
