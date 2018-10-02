package battery.droid.com.droidbattery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Robson on 03/05/2017.
 */

public class DroidSetStatus extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED) || (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED))) {
            DroidCommon.DispositivoConectado = intent.getAction().equals(Intent.ACTION_POWER_CONNECTED);
            try {
                DroidTTS.StopService(context);
                DroidCommon.updateViewsSizeBattery(context);
                DroidService.loopingBattery = true;
                DroidWidget.onAppWidgetOptionsChanged = true;
                DroidService.StopService(context);
                DroidService.StartService(context);
                DroidCommon.onUpdateDroidWidget(context);
                DroidTTS.StartService(context);
            } catch (Exception ex) {
                Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
            }
        }
    }
}
