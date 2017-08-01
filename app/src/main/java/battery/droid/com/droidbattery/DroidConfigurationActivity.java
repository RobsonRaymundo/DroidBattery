package battery.droid.com.droidbattery;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Robson on 02/05/2017.
 */

public class DroidConfigurationActivity extends PreferenceActivity {
    private Context context;

    public static int ColorCurrent = 0;
    public static String BatteryCurrent = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = getBaseContext();
        Log.d("DroidBattery", "DroidConfigurationActivity - onCreate: " );
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        Toast.makeText(this, "Widget Droid Battery está pronto para ser adicionado!",
                Toast.LENGTH_LONG).show();

        Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(handleTime(newValue.toString()));
                return true;
            }
        };
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Preference start = (Preference) findPreference("startTime");
        start.setSummary(handleTime(mPrefs.getString("startTime", "23:00")));
        start.setOnPreferenceChangeListener(listener);

        Preference stop  = (Preference) findPreference("stopTime");
        stop.setSummary(handleTime(mPrefs.getString("stopTime", "09:00")));
        stop.setOnPreferenceChangeListener(listener);

        DroidService.StopStartService(context);

    }

    private String handleTime(String time) {
        String[] timeParts=time.split(":");
        int lastHour=Integer.parseInt(timeParts[0]);
        int lastMinute=Integer.parseInt(timeParts[1]);

        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        if(is24HourFormat) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try
        {
            MenuInflater inflater = getMenuInflater();

        }
        catch(Exception ex)
        {
        }

        return true;
    }

}
