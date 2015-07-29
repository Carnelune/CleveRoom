package com.mica.ar.cleveroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Add the current settings as a new Scene
 * The user has to write the name of his new scene on the EditText and then press the OK button.
 */
public class AddScene extends Activity {
    public final static String NOM = "com.mica.ar.cleveroom.NOM";
    EditText name_pref = null;
    Button ok = null;
    Button cancel = null;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_scene);

        name_pref = (EditText)findViewById(R.id.name_scene);
        name_pref.addTextChangedListener(textWatcher);

        ok = (Button)findViewById(R.id.ok);
        ok.setOnClickListener(okListener);

        cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(cancelListener);
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
            //We save the edited text in the String NOM and we check if it is a valid name
            String nom = name_pref.getText().toString();
            //If NOM is null or has more than 32 characters, a short message "Error name" appears on the screen
            if ((nom.equals("")) || nom.length()>32) {
                Toast.makeText(AddScene.this, "Error name", Toast.LENGTH_SHORT).show();

            }
            //Otherwise, the name is saved in the Extra of the intent and the ApplyScene activity is launched
            else {

                Intent intent = new Intent(AddScene.this, ApplyScene.class);
                intent.putExtra(NOM, nom);

                startActivity(intent);
            }

        }
    };

    private View.OnClickListener cancelListener = new View.OnClickListener() {
        //When the cancel button is pressed, a short message "Transaction canceled" appears on the screen
        //The AddScene activity is closed
        @Override
        public void onClick(View v) {
            Toast.makeText(AddScene.this, "Transaction canceled", Toast.LENGTH_SHORT).show();
            AddScene.this.finish();
        }
    };

    public void onBackPressed(){
        //When the Back button of the smartphone is pressed, tje AddScene activity is closed
        //And the system goes back to the MainActivity.
        Intent intent = new Intent(AddScene.this, (MainActivity.class));
        startActivity(intent);
    }


}
