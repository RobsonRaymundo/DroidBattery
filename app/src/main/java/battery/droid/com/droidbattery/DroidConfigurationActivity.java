package battery.droid.com.droidbattery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;
import android.widget.Toast;

import java.util.prefs.Preferences;

/**
 * Created by Robson on 02/05/2017.
 */

public class DroidConfigurationActivity extends PreferenceActivity {
    private Context context;
    private Preference start;
    private Preference stop;
    private Preference falaBateriaCarregada;
    private Preference dispositivoConectado;
    private Preference dispositivoDesconectado;
    private ListPreference percentualAtingido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = getBaseContext();
        Log.d("DroidBattery", "DroidConfigurationActivity - onCreate: ");
        super.onCreate(savedInstanceState);
        SetPreference();
        //  DroidService.StopStartService(context);
        Intent intent = new Intent(context, DroidServiceScreen.class);
        startService(intent);
    }

    private void SetPreference() {
        addPreferencesFromResource(R.xml.preferences);
        Toast.makeText(this, "Widget Droid Battery está pronto para ser adicionado!",
                Toast.LENGTH_LONG).show();

        Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (preference.getKey().equals(start.getKey()) || preference.getKey().equals(stop.getKey())) {
                    preference.setSummary(DroidCommon.handleTime(context, newValue.toString()));
                }
                if (preference.getKey().equals(falaBateriaCarregada.getKey())) {
                    preference.setSummary(newValue.toString());
                }
                if (preference.getKey().equals(dispositivoConectado.getKey())) {
                    preference.setSummary(newValue.toString());
                }
                if (preference.getKey().equals(dispositivoDesconectado.getKey())) {
                    preference.setSummary(newValue.toString());
                }

                return true;
            }
        };

        ListPreference.OnPreferenceChangeListener listListener = new ListPreference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                preference.setSummary(o.toString() + " por cento");
                return true;
            }
        };


        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        start = (Preference) findPreference("startTime");
        start.setSummary(DroidCommon.handleTime(context, mPrefs.getString("startTime", "23:00")));
        start.setOnPreferenceChangeListener(listener);

        stop = (Preference) findPreference("stopTime");
        stop.setSummary(DroidCommon.handleTime(context, mPrefs.getString("stopTime", "09:00")));
        stop.setOnPreferenceChangeListener(listener);

        falaBateriaCarregada = (Preference) findPreference("falaBateriaCarregada");
        falaBateriaCarregada.setSummary(DroidCommon.PreferenceFalaBateriaCarregada(context));
        falaBateriaCarregada.setOnPreferenceChangeListener(listener);

        dispositivoConectado = (Preference) findPreference("dispositivoConectado");
        dispositivoConectado.setSummary(DroidCommon.PreferenceDispositivoConectado(context));
        dispositivoConectado.setOnPreferenceChangeListener(listener);

        dispositivoDesconectado = (Preference) findPreference("dispositivoDesconectado");
        dispositivoDesconectado.setSummary(DroidCommon.PreferenceDispositivoDesconectado(context));
        dispositivoDesconectado.setOnPreferenceChangeListener(listener);

        percentualAtingido = (ListPreference) findPreference("percentualAtingido");
        percentualAtingido.setSummary(DroidCommon.PreferencePercentualAtingido(context) + " por cento" );
        percentualAtingido.setOnPreferenceChangeListener(listListener);

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
