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
        Log.d("DroidBattery", "DroidSetStatus - onReceive ");

        if (intent.getAction().contains("android.intent.action.BOOT_COMPLETED") )
        {
            if (DroidCommon.ExibirDialogBootCompletado(context)) {
                Intent i = new Intent(context, DroidAlertDialogActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }

            if (DroidCommon.InformarBootCompletado(context)) {
                Intent intentTTS = new Intent(context, DroidTTS.class);
                DroidTTS.VozDispositivoReiniciado = true;
                context.startService(intentTTS);
            }
        }
    }


}
