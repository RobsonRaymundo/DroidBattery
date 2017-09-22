package battery.droid.com.droidbattery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Robson on 22/09/2017.
 */

public class DroidAlertDialogActivity extends AppCompatActivity
{
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try {
            Calendar calendar = Calendar.getInstance();
            String diaAtual = DateFormat.format("dd/MM/yyyy", calendar.getTime()).toString();
            String horaAtual = DateFormat.format("kk:mm:ss", calendar.getTime()).toString();
            String dataHoraAtual = "O dispositivo foi reiniciado\n dia " + diaAtual + " às " + horaAtual;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("ATENÇÃO");
            builder.setMessage(dataHoraAtual);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    alertDialog.dismiss();
                    finish();
                }
            });
            alertDialog = builder.create();
            alertDialog.show();
        }
        catch (Exception ex)
        {
            Log.d("DroidInfoBoot", ex.getMessage()  );
        }
    }
}
