package battery.droid.com.droidbattery;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

    private static boolean NaoPertube(Context context) {

        boolean informarBateriaCarregada = true;
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            boolean naoPertubeAtivado = sp.getBoolean("quiet", false);

            if (naoPertubeAtivado) {
                String startTime = sp.getString("startTime", "23:00");
                String stopTime = sp.getString("stopTime", "09:00");

                SimpleDateFormat sdfDate = new SimpleDateFormat("H:mm");
                String currentTimeStamp = sdfDate.format(new Date());
                int currentHour = Integer.parseInt(currentTimeStamp.split("[:]+")[0]);
                int currentMinute = Integer.parseInt(currentTimeStamp.split("[:]+")[1]);

                int startHour = Integer.parseInt(startTime.split("[:]+")[0]);
                int startMinute = Integer.parseInt(startTime.split("[:]+")[1]);

                int stopHour = Integer.parseInt(stopTime.split("[:]+")[0]);
                int stopMinute = Integer.parseInt(stopTime.split("[:]+")[1]);

                if (startHour < stopHour && currentHour > startHour && currentHour < stopHour)
                    informarBateriaCarregada = false;
                else if (startHour > stopHour && (currentHour > startHour || currentHour < stopHour))
                    informarBateriaCarregada = false;
                else if (currentHour == startHour && currentMinute >= startMinute)
                    informarBateriaCarregada = false;
                else if (currentHour == stopHour && currentMinute <= stopMinute)
                    informarBateriaCarregada = false;

                return informarBateriaCarregada;
            }
        } catch (Exception ex) {
            Log.d("DroidInfoBattery", ex.getMessage());
        }
        return informarBateriaCarregada;
    }


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
                        DroidCommon.TimeSleep(10);
                        DroidCommon.updateViewsInfoBattery(context, i.toString());
                    }
                } else {
                    DroidCommon.updateViewsInfoBattery(context, battery);
                }
                if (NaoPertube(context)) {
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
                //   DroidConfigurationActivity.ColorCurrent = DroidConfigurationActivity.ColorCurrent + 1;
            }
        }

    };

    @Override
    public void onInit(int status) {


    }


}
