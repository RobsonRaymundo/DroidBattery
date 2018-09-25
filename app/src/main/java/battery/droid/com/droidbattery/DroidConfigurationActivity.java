package battery.droid.com.droidbattery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private MultiSelectListPreference multiSelectListPreference = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = getBaseContext();

        Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()));
        super.onCreate(savedInstanceState);
        SetPreference();
        //  DroidService.StopStartService(context);
        Intent intent = new Intent(context, DroidServiceScreen.class);
        startService(intent);
    }


    private void SetPreference() {
        try {
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

            MultiSelectListPreference.OnPreferenceChangeListener multiSelectListListener = new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {

                    List<String> newValues = new ArrayList<>((HashSet<String>) o);

                    if (newValues.size() == 0) {
                        preference.setSummary("Nenhum percentual selecionado");
                    } else {
                        Collections.sort(newValues);
                        DroidCommon.MultiSelectPreference = newValues;
                        preference.setSummary(DroidCommon.MultiSelectPreference.toString() + " por cento");
                    }
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

            multiSelectListPreference = (MultiSelectListPreference) findPreference("multiSelectListPreference");
            multiSelectListPreference.setEntries(R.array.arrayPercentualAtingido);
            multiSelectListPreference.setEntryValues(R.array.arrayPercentualAtingidoValues);
            multiSelectListPreference.setDefaultValue(R.array.arrayPercentualAtingidoDefaultValues);
            multiSelectListPreference.setOnPreferenceChangeListener(multiSelectListListener);
            multiSelectListListener.onPreferenceChange(multiSelectListPreference, multiSelectListPreference.getValues());
        }
        catch (Exception ex)
        {
            Log.d(DroidCommon.TAG, DroidCommon.getLogTagWithMethod(new Throwable()) + " Erro: " + ex.getMessage());

        }


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
