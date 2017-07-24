package battery.droid.com.droidbattery;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.Toast;

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
        Toast.makeText(this, "Widget Droid Battery está pronto para ser adicionado!",
                Toast.LENGTH_LONG).show();
        Intent intentService = new Intent(context, DroidService.class);
        try {
            context.stopService(intentService);
        }
        catch (Exception ex)
        {
            Log.d("DroidBattery", "DroidConfigurationActivity - Erro: " + ex.getMessage() );
        }
        context.startService(intentService);
        finish();
    }
}
