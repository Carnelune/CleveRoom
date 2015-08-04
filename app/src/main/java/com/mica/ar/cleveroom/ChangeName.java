package com.mica.ar.cleveroom;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Change the name of the current light
 */

public class ChangeName extends Activity {
    EditText name = null;
    Button ok = null;
    Button cancel = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupchangename);

        name = (EditText)findViewById(R.id.new_name);
        name.addTextChangedListener(textWatcher);

        ok = (Button)findViewById(R.id.ok);
        ok.setOnClickListener(okListener);

        cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(cancelListener);
    }

    //Function which returns true if name belongs in liste.
    public Boolean belongs(List<String> liste, String name){
        boolean equal = false;
        int i=0;
        while(i<liste.size() && !equal){
            if(liste.get(i).equals(name)){
                equal = true;
            }
            else {
                i++;
            }
        }
        return equal;
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {        }

        @Override
        public void afterTextChanged(Editable s) {        }
    };

    private OnClickListener okListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //We save the edited text in the String NOM and we check if it is a valid name
            String nom = name.getText().toString();
            List<String> List_name = new ArrayList<>();

            if ((nom.equals("")) || nom.length()>32) {
                //If NOM is null or has more than 32 characters, a short message "Error name" appears on the screen
                Toast.makeText(ChangeName.this, "Error name", Toast.LENGTH_SHORT).show();
            }
            else{
                //Then we check if the name given is not already assigned to another lamp
                PHHueSDK phHueSDK;
                phHueSDK = PHHueSDK.create();
                PHBridge bridge = phHueSDK.getSelectedBridge();
                Preferences prefs = Preferences.getInstance(getApplicationContext());
                String lampe_id = prefs.getLightChose();
                List<PHLight> allLights = bridge.getResourceCache().getAllLights();
                for (PHLight light : allLights) {
                    if (!(light.getIdentifier().equals(lampe_id))) {
                        List_name.add(light.getName());

                    }
                }
                //List_name contains the names of all the other lamps of the bridge

                if (belongs(List_name, nom)) {
                    //If the name NOM belongs to List_name, a short message appears.
                    Toast.makeText(ChangeName.this, "Name already assigned to another lamp", Toast.LENGTH_LONG).show();
                }
                else {
                    //Otherwise we set the new name to the light, show a message and go back to the MainActivity
                    for (PHLight light : allLights) {
                        if (light.getIdentifier().equals(lampe_id)) {
                            light.setName(nom);
                            bridge.updateLight(light, listener);
                        }
                    }

                    Toast.makeText(ChangeName.this, "Name modified", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChangeName.this, (MainActivity.class));
                    startActivity(intent);
                }
            }
        }
    };

    private OnClickListener cancelListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //By pressing the cancel button the ChangeName activity is closed.
            Toast.makeText(ChangeName.this, "Transaction canceled", Toast.LENGTH_SHORT).show();
            ChangeName.this.finish();
        }
    };

    PHLightListener listener = new PHLightListener() {
        @Override
        public void onSuccess() {        }

        @Override
        public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {
            Log.w(MainActivity.TAG, "Light has updated");
        }

        @Override
        public void onError(int arg0, String arg1) {        }

        @Override
        public void onReceivingLightDetails(PHLight arg0) {        }

        @Override
        public void onReceivingLights(List<PHBridgeResource> arg0) {        }

        @Override
        public void onSearchComplete() {        }
    };
}