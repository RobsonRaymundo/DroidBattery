package battery.droid.com.droidbattery;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

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
        //Log.d("DroidBattery", "onCreate: " );
        super.onCreate(savedInstanceState);
        Intent intentService = new Intent(context, DroidService.class);
        startService(intentService);
        finish();
    }
}
