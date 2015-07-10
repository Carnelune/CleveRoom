package com.mica.ar.cleveroom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;

import java.util.List;

/**
 * Options of the app
 */
public class Options extends Activity {
    private CheckBox CheckBox_Sms;
    private Button Button_Ok;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
        CheckBox_Sms = (CheckBox) findViewById(R.id.options_sms);
        Button_Ok = (Button) findViewById(R.id.button_ok);
        Preferences prefs = Preferences.getInstance(getApplicationContext());

        CheckBox_Sms.setText(R.string.options_sms);
        Button_Ok.setText(R.string.options_ok);

        CheckBox_Sms.setChecked(prefs.getSms());


        CheckBox_Sms.setOnClickListener(checkedListener);

        Button_Ok.setOnClickListener(Button_OkListener);

    }

  private View.OnClickListener checkedListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Preferences pref = Preferences.getInstance(getApplicationContext());

            if (((CheckBox) v).isChecked()) {
                pref.setSms(true);

            }
             else {
                pref.setSms(false);
            }
        }
    };

   private View.OnClickListener Button_OkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Options.this.finish();

        }
    };
}
