package com.mica.ar.cleveroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sarah on 18/06/2015.
 */
public class AddPreferences extends Activity {
    public final static String Nom_pref = "com.mica.ar.cleveroom.Nom_pref";
    EditText name_user = null;
    EditText name_pref = null;
    Button ok = null;
    Button annuler = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_preferences);

        name_user = (EditText)findViewById(R.id.name_user);
        name_user.addTextChangedListener(textWatcher);

        name_pref = (EditText)findViewById(R.id.name_pref);
        name_pref.addTextChangedListener(textWatcher);

        ok = (Button)findViewById(R.id.ok);
        ok.setOnClickListener(okListener);

        annuler = (Button)findViewById(R.id.annuler);
        annuler.setOnClickListener(annulerListener);


    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private View.OnClickListener okListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String nom = name_pref.getText().toString();

            //Données à envoyer au serveur

            //....


            //Retour à l'activité précedente
            AddPreferences.this.finish();
        }
    };

    private View.OnClickListener annulerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Toast.makeText(AddPreferences.this, "Operation annulee", Toast.LENGTH_SHORT).show();
            AddPreferences.this.finish();


        }
    };

}
