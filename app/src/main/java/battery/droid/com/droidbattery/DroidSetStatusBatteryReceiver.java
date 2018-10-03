package battery.droid.com.droidbattery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Robson on 03/05/2017.
 */

public class DroidSetStatusBatteryReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) ||
                intent.getAction().equals(Intent.ACTION_MY_PACKAGE_REPLACED)) {
            DroidMainService.StartService(context);
        }
        else {
            DroidCommon.DispositivoConectado = intent.getAction().equals(Intent.ACTION_POWER_CONNECTED);
            DroidCommon.DispositivoDesConectado = intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED);

            if (DroidCommon.DispositivoConectado || DroidCommon.DispositivoDesConectado) {
                try {
                    DroidCommon.InformaDispositivoConectado = true;
                    DroidCommon.updateViewsSizeBattery(context);
                    DroidMainService.loopingBattery = true;
                    DroidCommon.onUpdateDroidWidget(context);
                    DroidMainService.AtualizaBateria(context);
                    DroidMainService.ChamaSinteseVoz(context );
                } catch (Exception ex) {
                    Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
                }
                finally {
                    DroidCommon.InformaDispositivoConectado = false;
                }
            }
        }
    }
}
