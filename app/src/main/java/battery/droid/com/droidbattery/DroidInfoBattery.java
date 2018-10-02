package battery.droid.com.droidbattery;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.BatteryManager;
import android.util.Log;

/**
 * Created by Robson on 02/05/2017.
 */

public class DroidInfoBattery {

    public static BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                int level = intent.getIntExtra("level", 0);
                String battery = String.valueOf(level);
                DroidCommon.BatteryStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " " + battery + " " + DroidCommon.BatteryStatus + " " + DroidCommon.BatteryFull);
                if (DroidWidget.onAppWidgetOptionsChanged || (!DroidCommon.BatteryCurrent.contains(battery)) || (DroidCommon.BatteryStatus == BatteryManager.BATTERY_STATUS_FULL && !DroidCommon.BatteryFull)) {
                    if (DroidCommon.BatteryStatus == BatteryManager.BATTERY_STATUS_FULL) {
                        DroidCommon.BatteryFull = true;
                    } else DroidCommon.BatteryFull = false;
                    DroidWidget.onAppWidgetOptionsChanged = false;
                    DroidCommon.BatteryCurrent = battery;
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
                    DroidCommon.InformarBateriaCarregada = DroidCommon.InformarBateriaCarregada(context);
                    DroidCommon.InformarPercentualAtingidoMultiSelectPreference = DroidCommon.InformarPercentualAtingidoMultiSelectPreference(context);
                    if (DroidCommon.InformarBateriaCarregada || DroidCommon.InformarPercentualAtingidoMultiSelectPreference || DroidCommon.BatteryFull) {
                        DroidTTS.StopService(context);
                        DroidTTS.StartService(context);
                    }

                    boolean charging = DroidCommon.BatteryStatus == BatteryManager.BATTERY_STATUS_CHARGING;
                    boolean chargingFull = DroidCommon.BatteryStatus == BatteryManager.BATTERY_STATUS_FULL;
                    boolean notcharging = DroidCommon.BatteryStatus == BatteryManager.BATTERY_STATUS_NOT_CHARGING;
                    DroidCommon.isCharging = charging || chargingFull;
                    if (charging) {
                        DroidCommon.updateViewsColorBattery(context, Color.BLUE);
                    } else if (chargingFull) {
                        DroidCommon.updateViewsColorBattery(context, Color.GREEN);
                    } else if (notcharging) {
                        DroidCommon.updateViewsColorBattery(context, Color.YELLOW);
                    } else {
                        Integer totalBattery = Integer.parseInt(battery);
                        if (totalBattery <= 20) {
                            DroidCommon.updateViewsColorBattery(context, Color.RED);
                        } else {
                            DroidCommon.updateViewsColorBattery(context, Color.WHITE);
                        }
                    }

                }
            } catch (Exception ex) {
                Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
            }
        }
    };
}
