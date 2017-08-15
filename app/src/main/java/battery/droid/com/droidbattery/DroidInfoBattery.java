package battery.droid.com.droidbattery;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Robson on 02/05/2017.
 */

public class DroidInfoBattery implements TextToSpeech.OnInitListener {
    private TextToSpeech tts;


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
                    if (DroidCommon.NaoPertube(context)) {
                        if (battery.equals("100")) {
                            DroidCommon.updateViewsColorBattery(context, Color.BLUE);
                            if (DroidCommon.InformarBateriaCarregada(context)) {
                                try {
                                    DroidService.tts.speak("Bateria carregada, você já pode desconectar do carregador.", TextToSpeech.QUEUE_FLUSH, null);

                                } catch (Exception ex) {
                                    Log.d("DroidBattery", "DroidInfoBattery - BroadcastReceiver - Erro: " + ex.getMessage());
                                }
                            }
                        }
                    }
                }
            }
        }

    };

    @Override
    public void onInit(int status) {


    }


}
