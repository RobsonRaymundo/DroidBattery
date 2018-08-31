package battery.droid.com.droidbattery;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Robson on 04/08/2017.
 */

public class DroidCommon {

    public static boolean isCharging;
    public static String BatteryCurrent = "";
    public static boolean DispositivoConectado;
    public static boolean DispositivoDesConectado;

    public static String handleTime(Context context, String time) {
        String[] timeParts = time.split(":");
        int lastHour = Integer.parseInt(timeParts[0]);
        int lastMinute = Integer.parseInt(timeParts[1]);

        boolean is24HourFormat = DateFormat.is24HourFormat(context);

        if (is24HourFormat) {
            return ((lastHour < 10) ? "0" : "")
                    + Integer.toString(lastHour)
                    + ":" + ((lastMinute < 10) ? "0" : "")
                    + Integer.toString(lastMinute);
        } else {
            int myHour = lastHour % 12;
            return ((myHour == 0) ? "12" : ((myHour < 10) ? "0" : "") + Integer.toString(myHour))
                    + ":" + ((lastMinute < 10) ? "0" : "")
                    + Integer.toString(lastMinute)
                    + ((lastHour >= 12) ? " PM" : " AM");
        }
    }

    public static void onUpdateDroidWidget(Context context) {
        try {
            DroidWidget droidWidget = new DroidWidget();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            droidWidget.onUpdate(context, appWidgetManager, null);
        } catch (Exception ex) {
            Log.d("DroidBattery", "DroidBootComplete - onReceive - Erro: " + ex.getMessage());
        }
    }

    public static void updateViewsSizeBattery(Context context) {
        Log.d("DroidBattery", "DroidInfoBattery - updateViews()");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        Integer min_width = DroidPreferences.GetInteger(context, "MIN_WIDTH");

        if (min_width > 110) {
            views.setTextViewTextSize(R.id.batteryText, TypedValue.COMPLEX_UNIT_DIP, 50);
        } else {
            views.setTextViewTextSize(R.id.batteryText, TypedValue.COMPLEX_UNIT_DIP, 30);
        }

        ComponentName componentName = new ComponentName(context, DroidWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(componentName, views);
    }

    public static boolean PreferenceBateriaCarregada(final Context context) {
        boolean spf = false;
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            spf = sp.getBoolean("spf_bateriaCarregada", true);
        } catch (Exception ex) {
            Log.d("DroidInfoBattery", ex.getMessage());
        }
        return spf;
    }


    public static String PreferenceDispositivoConectado(final Context context) {
        String spf = "";
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            spf = sp.getString("dispositivoConectado", context.getString(R.string.txt_dispositivo_conectado)  );
        } catch (Exception ex) {
            Log.d("DroidInfoBattery", ex.getMessage());
        }
        return spf;
    }

    public static String PreferenceDispositivoDesconectado(final Context context) {
        String spf = "";
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            spf = sp.getString("dispositivoDesconectado", context.getString(R.string.txt_dispositivo_desconectado)  );
        } catch (Exception ex) {
            Log.d("DroidInfoBattery", ex.getMessage());
        }
        return spf;
    }

    private static String ObtemTextoPercentual(String valor)
    {
        return "";

    }

    public static String PreferencePercentualAtingido(final Context context) {
        String spf = "";
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            spf = sp.getString("percentualAtingido", ""  );
        } catch (Exception ex) {
            Log.d("DroidInfoBattery", ex.getMessage());
        }
        return spf;
    }


    public static String PreferenceFalaBateriaCarregada(final Context context) {
        String spf = "";
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            spf = sp.getString("falaBateriaCarregada", context.getString(R.string.spf_fala_bateria_carregada)  );
        } catch (Exception ex) {
            Log.d("DroidInfoBattery", ex.getMessage());
        }
        return spf;
    }


    public static void updateViewsInfoBattery(Context context, String batteryLevel) {
        Log.d("DroidBattery", "DroidInfoBattery - updateViews()");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        views.setTextViewText(R.id.batteryText, batteryLevel);

        ComponentName componentName = new ComponentName(context, DroidWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(componentName, views);
    }

    public static void TimeSleep(Integer seg) {
        try {
            Thread.sleep(seg);
        } catch (Exception ex) {
        }
    }

    public static void Vibrar(Context context, int valor) {
        try {
            TimeSleep(100);
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(valor);
            TimeSleep(100);
        } catch (Exception ex) {
            Log.d("DroidBattery", "Vibrar: " + ex.getMessage());
        }
    }

    public static void updateViewsColorBattery(Context context, int color) {
        Log.d("DroidBattery", "DroidWidget - updateViews()");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        views.setTextColor(R.id.batteryText, color);
        Integer min_width = DroidPreferences.GetInteger(context, "MIN_WIDTH");
        if (min_width > 110) {
            views.setTextViewTextSize(R.id.batteryText, TypedValue.COMPLEX_UNIT_DIP, 50);
        } else {
            views.setTextViewTextSize(R.id.batteryText, TypedValue.COMPLEX_UNIT_DIP, 30);
        }
        ComponentName componentName = new ComponentName(context, DroidWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(componentName, views);
    }

    public static boolean NaoPertube(Context context) {

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

    public static boolean InformarBateriaCarregada(Context context) {
        return DroidCommon.PreferenceBateriaCarregada(context) &&
                DroidCommon.NaoPertube(context) &&
                DroidCommon.isCharging &&
                DroidCommon.BatteryCurrent.equals("100");
    }

    public static boolean InformarPercentualAtingido(Context context)
    {
        return DroidCommon.PreferenceBateriaCarregada(context) &&
                DroidCommon.NaoPertube(context) &&
                DroidCommon.isCharging &&
                DroidCommon.BatteryCurrent.equals(PreferencePercentualAtingido(context));
    }


}
