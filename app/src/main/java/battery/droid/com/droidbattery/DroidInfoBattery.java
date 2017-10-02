package battery.droid.com.droidbattery;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Robson on 02/05/2017.
 */

public class DroidInfoBattery {

    public static BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("DroidBattery", "DroidInfoBattery - BroadcastReceiver ");
            int level = intent.getIntExtra("level", 0);
            String battery = String.valueOf(level);
            if (DroidWidget.onAppWidgetOptionsChanged || (!DroidConfigurationActivity.BatteryCurrent.contains(battery))) {
                DroidWidget.onAppWidgetOptionsChanged = false;
                DroidConfigurationActivity.BatteryCurrent = battery;
                if (DroidService.loopingBattery) {
                    DroidService.loopingBattery = false;
                    Integer totalBattery = Integer.parseInt(battery);
                    for (Integer i = 0; i <= totalBattery; i++) {
                        DroidCommon.TimeSleep(1);
                        DroidCommon.updateViewsInfoBattery(context, i.toString());
                    }
                } else {
                    DroidCommon.updateViewsInfoBattery(context, battery);
                }
                if (DroidCommon.isCharging) {
                    if (battery.equals("100")) {
                        if (DroidCommon.InformarBateriaCarregada(context)) {
                            try {
                                Intent intentTTS = new Intent(context, DroidTTS.class);
                                context.startService(intentTTS);
                            } catch (Exception ex) {
                                Log.d("DroidBattery", "DroidInfoBattery - BroadcastReceiver - Erro: " + ex.getMessage());
                            }
                        }
                    }
                }
            }
        }

    };

}
