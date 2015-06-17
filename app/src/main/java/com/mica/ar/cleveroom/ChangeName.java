package com.mica.ar.cleveroom;


        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.view.View.OnClickListener;
        import android.widget.Toast;

/**
 * Created by sarah on 15/06/2015.
 */


public class ChangeName extends Activity {

    public final static String newName = "com.philips.lighting.quickstar.newName";

    EditText name = null;
    Button ok = null;
    Button annuler = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupchangename);

        name = (EditText)findViewById(R.id.new_name);
        name.addTextChangedListener(textWatcher);

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

    private OnClickListener okListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String nom = name.getText().toString();
            if (nom.equals("")) {
                Toast.makeText(ChangeName.this, "Error name", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(ChangeName.this, "Nom modifie", Toast.LENGTH_SHORT).show();




                Intent retour = new Intent(ChangeName.this, MainActivity.class);
                // On rajoute un extra
                retour.putExtra(newName, nom);

                // Puis on lance l'intent !
                startActivity(retour);
            }
        }
    };

    private OnClickListener annulerListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String nom = "";

            Toast.makeText(ChangeName.this, "Operation annulee", Toast.LENGTH_SHORT).show();


            Intent retour = new Intent(ChangeName.this, MainActivity.class);
            retour.putExtra(newName, nom);

            // Puis on lance l'intent !
            startActivity(retour);
        }
    };
}
