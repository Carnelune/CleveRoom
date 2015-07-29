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
import android.widget.Toast;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.List;
import java.util.Map;

/**
 * Created by Test on 28/07/2015.
 */
public class Morse extends Activity {
    public final static String NOM = "com.mica.ar.cleveroom.NOM";
    EditText name_pref = null;
    Button ok = null;
    Button annuler = null;
    private PHHueSDK phHueSDK;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.morse);

        name_pref = (EditText)findViewById(R.id.mot_morse);
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
            String mot = name_pref.getText().toString();
            morse(mot);
            Morse.this.finish();
        }
    }           ;

    private View.OnClickListener annulerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(Morse.this, "Operation annulee", Toast.LENGTH_SHORT).show();
            Morse.this.finish();
        }
    };

    public void onBackPressed() {
        Intent intent = new Intent(Morse.this, (MainActivity.class));
        startActivity(intent);
    }

    public void morse(String mot) {
        Preferences prefs = Preferences.getInstance(getApplicationContext());
        final String lampe_id = prefs.getLightChose();
        phHueSDK = PHHueSDK.create();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        for (PHLight light : allLights) {
            if (light.getIdentifier().equals(lampe_id)) {
                PHLightState lightState = new PHLightState();
                lightState.setOn(false);
                bridge.updateLightState(light, lightState, listener);
                for (int i = 0; i < mot.length(); i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    switch (mot.charAt(i)) {
                        case 'A':
                            point(light);
                            ligne(light);
                            break;
                        case 'B':
                            ligne(light);
                            point(light);
                            point(light);
                            point(light);
                            break;
                        case 'C':
                            ligne(light);
                            point(light);
                            ligne(light);
                            point(light);
                            break;
                        case 'D':
                            ligne(light);
                            point(light);
                            point(light);
                            break;
                        case 'E':
                            point(light);
                            break;
                        case 'F':
                            point(light);
                            point(light);
                            ligne(light);
                            point(light);
                            break;
                        case 'G':
                            ligne(light);
                            ligne(light);
                            point(light);
                            break;
                        case 'H':
                            point(light);
                            point(light);
                            point(light);
                            point(light);
                            break;
                        case 'I':
                            point(light);
                            point(light);
                            break;
                        case 'J':
                            point(light);
                            ligne(light);
                            ligne(light);
                            ligne(light);
                            break;
                        case 'K':
                            ligne(light);
                            point(light);
                            ligne(light);
                            break;
                        case 'L':
                            point(light);
                            ligne(light);
                            point(light);
                            point(light);
                            break;
                        case 'M':
                            ligne(light);
                            ligne(light);
                            break;
                        case 'N':
                            ligne(light);
                            point(light);
                            break;
                        case 'O':
                            ligne(light);
                            ligne(light);
                            ligne(light);
                            break;
                        case 'P':
                            point(light);
                            ligne(light);
                            ligne(light);
                            point(light);
                            break;
                        case 'Q':
                            ligne(light);
                            ligne(light);
                            point(light);
                            ligne(light);
                            break;
                        case 'R':
                            point(light);
                            ligne(light);
                            point(light);
                            break;
                        case 'S':
                            point(light);
                            point(light);
                            point(light);
                            break;
                        case 'T':
                            ligne(light);
                            break;
                        case 'U':
                            point(light);
                            point(light);
                            ligne(light);
                            break;
                        case 'V':
                            point(light);
                            point(light);
                            point(light);
                            ligne(light);
                            break;
                        case 'W':
                            point(light);
                            ligne(light);
                            ligne(light);
                            break;
                        case 'X':
                            ligne(light);
                            point(light);
                            point(light);
                            ligne(light);
                            break;
                        case 'Y':
                            ligne(light);
                            point(light);
                            ligne(light);
                            ligne(light);
                            break;
                        case 'Z':
                            ligne(light);
                            ligne(light);
                            point(light);
                            point(light);
                            break;
                    }
                }
            }
        }
    }

    public void point(PHLight light) {
        PHBridge bridge = phHueSDK.getSelectedBridge();
        PHLightState lightState = new PHLightState();
        lightState.setOn(true);
        bridge.updateLightState(light, lightState, listener);
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        lightState.setOn(false);
        bridge.updateLightState(light, lightState, listener);
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void ligne(PHLight light) {
        PHBridge bridge = phHueSDK.getSelectedBridge();
        PHLightState lightState = new PHLightState();
        lightState.setOn(true);
        bridge.updateLightState(light, lightState, listener);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        lightState.setOn(false);
        bridge.updateLightState(light, lightState, listener);
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

PHLightListener listener = new PHLightListener() {
    @Override
    public void onSuccess() {        }

    @Override
    public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {

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
