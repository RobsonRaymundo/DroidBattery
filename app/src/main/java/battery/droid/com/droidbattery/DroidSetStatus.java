package battery.droid.com.droidbattery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;

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

        try {
            Intent intentTTS = new Intent(context, DroidTTS.class);
            if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
                DroidCommon.DispositivoConectado = true;
            }
            if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
                DroidCommon.DispositivoDesConectado = true;
            }

            context.startService(intentTTS);
        } catch (Exception ex) {
            Log.d("DroidBattery", "DroidInfoBattery - BroadcastReceiver - Erro: " + ex.getMessage());
        }

        Log.d("DroidBattery", "DroidSetStatus - onReceive ");
    }
}
