package battery.droid.com.droidbattery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

/**
 * Created by Robson on 02/05/2017.
 */

public class DroidConfigurationActivity extends PreferenceActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = getBaseContext();
        Log.d("DroidBattery", "DroidConfigurationActivity - onCreate: ");
        super.onCreate(savedInstanceState);
        SetPreference();
        //  DroidService.StopStartService(context);
        Intent intent = new Intent(context, DroidServiceScreen.class);
        startService(intent);

        //DroidServiceScreen.StopServiceScreen(context); // Teste para ver o restart do service screen
    }

    private void SetPreference() {
        addPreferencesFromResource(R.xml.preferences);
        Toast.makeText(this, "Widget Droid Battery está pronto para ser adicionado!",
                Toast.LENGTH_LONG).show();

        Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(DroidCommon.handleTime(context, newValue.toString()));
                return true;
            }
        };
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Preference start = (Preference) findPreference("startTime");
        start.setSummary(DroidCommon.handleTime(context, mPrefs.getString("startTime", "23:00")));
        start.setOnPreferenceChangeListener(listener);

        Preference stop = (Preference) findPreference("stopTime");
        stop.setSummary(DroidCommon.handleTime(context, mPrefs.getString("stopTime", "09:00")));
        stop.setOnPreferenceChangeListener(listener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            MenuInflater inflater = getMenuInflater();

        } catch (Exception ex) {
        }

        return true;
    }

}
