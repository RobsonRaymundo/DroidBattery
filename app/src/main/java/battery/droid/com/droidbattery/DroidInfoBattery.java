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
                updateViewsInfoBattery(context, battery);

                if (NaoPertube(context)) {
                    if (battery.equals("100")) {
                        if (InformarBateriaCarregada(context)) {
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

    public static boolean InformarBateriaCarregada(final Context context) {
        boolean spf = false;
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            spf = sp.getBoolean("spf_bateriaCarregada", true);
        } catch (Exception ex) {
            Log.d("DroidInfoBattery", ex.getMessage());
        }
        return spf;

    }


    private static int randomColor() {
        switch (DroidConfigurationActivity.ColorCurrent) {
            case 0: {
                return Color.WHITE;
            }
            case 1: {
                return Color.GREEN;
            }
            case 2: {
                return Color.YELLOW;
            }
            case 3: {
                DroidConfigurationActivity.ColorCurrent = 0;
                return Color.RED;
            }
            default: {
                return Color.WHITE;
            }
        }
    }

    public static void updateViewsInfoBattery(Context context, String batteryLevel) {
        Log.d("DroidBattery", "DroidInfoBattery - updateViews()");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        views.setTextViewText(R.id.batteryText, batteryLevel);
        //  views.setTextColor(R.id.batteryText, randomColor());
        //views.setTextViewTextSize(R.id.batteryText, TypedValue.COMPLEX_UNIT_DIP , 34);

        ComponentName componentName = new ComponentName(context, DroidWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(componentName, views);
    }

    @Override
    public void onInit(int status) {


    }
}
