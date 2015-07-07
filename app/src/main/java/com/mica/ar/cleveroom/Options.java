package com.mica.ar.cleveroom;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

/**
 * Created by sarah on 07/07/2015.
 */
public class Options extends Activity {
    private CheckBox Sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);
        Sms = (CheckBox) findViewById(R.id.options_sms);
        Sms.setText(R.string.options_sms);
        Sms.setOnClickListener(checkedListener);

    }

    private View.OnClickListener checkedListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Preferences pref = Preferences.getInstance(getApplicationContext());

            if (((CheckBox) v).isChecked()) {
                pref.setSms(true);
            } else {
                pref.setSms(false);
            }
        }
    };


}
