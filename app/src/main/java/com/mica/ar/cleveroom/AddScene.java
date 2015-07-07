package com.mica.ar.cleveroom;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Add the current settings as a new Scene
 */
public class AddScene extends Activity {
    EditText name_user = null;
    EditText name_pref = null;
    Button ok = null;
    Button annuler = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_scene);

        name_user = (EditText)findViewById(R.id.name_user);
        name_user.addTextChangedListener(textWatcher);

        name_pref = (EditText)findViewById(R.id.name_scene);
        name_pref.addTextChangedListener(textWatcher);

        ok = (Button)findViewById(R.id.ok);
        ok.setOnClickListener(okListener);

        annuler = (Button)findViewById(R.id.annuler);
        annuler.setOnClickListener(annulerListener);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {        }

        @Override
        public void afterTextChanged(Editable s) {        }
    };

    private View.OnClickListener okListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String nom = name_pref.getText().toString();

            //....

            AddScene.this.finish();
        }
    };

    private View.OnClickListener annulerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(AddScene.this, "Operation annulee", Toast.LENGTH_SHORT).show();
            AddScene.this.finish();
        }
    };

}
