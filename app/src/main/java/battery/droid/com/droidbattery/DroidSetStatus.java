package battery.droid.com.droidbattery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Robson on 03/05/2017.
 */

public class DroidSetStatus extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DroidCommon.updateViewsSizeBattery(context);
        DroidService.loopingBattery = true;
        DroidWidget.onAppWidgetOptionsChanged = true;
        DroidService.StopService(context);
        DroidCommon.TimeSleep(1000);
        DroidService.StartService(context);
        DroidCommon.onUpdateDroidWidget(context);

        if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
            DroidCommon.DispositivoConectado = true;
            Toast.makeText(context, "Dispositivo conectado", Toast.LENGTH_SHORT).show();
        }
        if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
            DroidCommon.DispositivoDesConectado = true;
            Toast.makeText(context, "Dispositivo desconectado", Toast.LENGTH_SHORT).show();
        }

        if (DroidCommon.DispositivoConectado || DroidCommon.DispositivoDesConectado)
            try {
                Intent intentTTS = new Intent(context, DroidTTS.class);
                try {
                    context.stopService(intentTTS);
                } catch (Exception ex) {
                }
                context.startService(intentTTS);
            } catch (Exception ex) {
                Log.d("DroidBattery", "DroidInfoBattery - BroadcastReceiver - Erro: " + ex.getMessage());
            }

        Log.d("DroidBattery", "DroidSetStatus - onReceive ");
    }
}
