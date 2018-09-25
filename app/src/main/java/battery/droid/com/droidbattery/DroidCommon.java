package battery.droid.com.droidbattery;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Robson on 04/08/2017.
 */

public class DroidCommon {

    public static String TAG = "DroidInfoBattery";
    public static boolean isCharging;
    public static String BatteryCurrent = "";
    public static boolean DispositivoConectado;
    public static boolean DispositivoDesConectado;
    public static List<String> MultiSelectPreference;


    public static String getLogTagWithMethod(Throwable stack) {
        StackTraceElement[] trace = stack.getStackTrace();
        return trace[0].getClassName() + "." + trace[0].getMethodName() + ":" + trace[0].getLineNumber();
    }

    public static String handleTime(Context context, String time) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        String retorno = "";
        try {
            String[] timeParts = time.split(":");
            int lastHour = Integer.parseInt(timeParts[0]);
            int lastMinute = Integer.parseInt(timeParts[1]);

            boolean is24HourFormat = DateFormat.is24HourFormat(context);

            if (is24HourFormat) {
                retorno = ((lastHour < 10) ? "0" : "")
                        + Integer.toString(lastHour)
                        + ":" + ((lastMinute < 10) ? "0" : "")
                        + Integer.toString(lastMinute);
            } else {
                int myHour = lastHour % 12;
                retorno = ((myHour == 0) ? "12" : ((myHour < 10) ? "0" : "") + Integer.toString(myHour))
                        + ":" + ((lastMinute < 10) ? "0" : "")
                        + Integer.toString(lastMinute)
                        + ((lastHour >= 12) ? " PM" : " AM");
            }
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
        return retorno;
    }

    public static void onUpdateDroidWidget(Context context) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        try {
            DroidWidget droidWidget = new DroidWidget();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            droidWidget.onUpdate(context, appWidgetManager, null);
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
    }

    public static void updateViewsSizeBattery(Context context) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        try {
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

        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
    }

    public static boolean PreferenceAtivarSinteseVoz(final Context context) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        boolean spf = false;
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            spf = sp.getBoolean("spf_ativarSinteseVoz", true);
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
        return spf;
    }


    public static String PreferenceDispositivoConectado(final Context context) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        String spf = "";
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            spf = sp.getString("dispositivoConectado", context.getString(R.string.txt_dispositivo_conectado));
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
        return spf;
    }

    public static String PreferenceDispositivoDesconectado(final Context context) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        String spf = "";
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            spf = sp.getString("dispositivoDesconectado", context.getString(R.string.txt_dispositivo_desconectado));
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
        return spf;
    }

    public static String MultSelectPreferencePercentualAtingido(final Context context) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        String spf = "";
        try {

            for (String retval : DroidCommon.MultiSelectPreference) {
                if (DroidCommon.BatteryCurrent.equals(retval)) {
                    spf = retval.toString();
                    break;
                }
            }

        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
        return spf;
    }


    public static String PreferenceFalaBateriaCarregada(final Context context) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        String spf = "";
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            spf = sp.getString("falaBateriaCarregada", context.getString(R.string.txt_fala_bateria_carregada));
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
        return spf;
    }


    public static void updateViewsInfoBattery(Context context, String batteryLevel) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        try {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            views.setTextViewText(R.id.batteryText, batteryLevel);

            ComponentName componentName = new ComponentName(context, DroidWidget.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(componentName, views);
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
    }

    public static void TimeSleep(Integer seg) {
        try {
            Thread.sleep(seg);
        } catch (Exception ex) {
        }
    }

    public static void Vibrar(Context context, int valor) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        try {
            TimeSleep(100);
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(valor);
            TimeSleep(100);
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
    }

    public static void updateViewsColorBattery(Context context, int color) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        try {
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
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
    }

    public static boolean NaoPertube(Context context) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
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
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
        return informarBateriaCarregada;
    }

    public static boolean InformarBateriaCarregada(Context context) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        return DroidCommon.PreferenceAtivarSinteseVoz(context) &&
                DroidCommon.NaoPertube(context) &&
                DroidCommon.isCharging &&
                DroidCommon.BatteryCurrent.equals("100");
    }

    public static boolean InformarPercentualAtingidoMultiSelectPreference(Context context) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        return DroidCommon.PreferenceAtivarSinteseVoz(context) &&
                DroidCommon.NaoPertube(context) &&
                DroidCommon.isCharging &&
                PercentualAtingidoMultiSelectPreference(context);
    }


    public static boolean PercentualAtingidoMultiSelectPreference(Context context) {
        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        boolean retorno = false;
        try
        {
        if (MultiSelectPreference != null) {
            for (String retval : DroidCommon.MultiSelectPreference) {
                retorno = DroidCommon.BatteryCurrent.equals(retval);
                if (retorno) break;
            }
        }
        } catch (Exception ex) {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());
        }
        return retorno;
    }

}
