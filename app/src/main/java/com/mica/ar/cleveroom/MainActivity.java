package com.mica.ar.cleveroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Fichier de l'activité principale
 */
public class MainActivity extends Activity {
    private PHHueSDK phHueSDK;
    private static final int MAX_HUE = 65535;
    public static final String TAG = "QuickStart";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_main);
        phHueSDK = PHHueSDK.create();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        Preferences prefs = Preferences.getInstance(getApplicationContext());
        String lampe_id = prefs.getLightChose();
        String lampe = "";
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        for (PHLight light : allLights) {
            if(light.getIdentifier().equals(lampe_id)) {
                lampe=light.getName();
            }
        }
        TextView t1 = (TextView) findViewById(R.id.lightname);
        t1.setText(lampe);
        Button randomButton;
        randomButton = (Button) findViewById(R.id.buttonRand);
        randomButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                randomLights();
            }

        });
        Button onButton;
        onButton = (Button) findViewById(R.id.buttonOn);
        onButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                lightOn();
            }

        });
        Button offButton;
        offButton = (Button) findViewById(R.id.buttonOff);
        offButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                lightOff();
            }

        });

        Intent i = getIntent();
        String name = i.getStringExtra(ChangeName.newName);
        if(name != "") {
            ChangeName(name);
        }
    }

    public void ChangeName(String name){
        PHBridge bridge = phHueSDK.getSelectedBridge();
        Preferences prefs = Preferences.getInstance(getApplicationContext());
        String lampe_id = prefs.getLightChose();

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        for (PHLight light : allLights) {
            if(light.getIdentifier().equals(lampe_id)) {
                light.setName(name);
            }
        }

    }
//lol
    public void randomLights() {
        PHBridge bridge = phHueSDK.getSelectedBridge();

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        Random rand = new Random();

        for (PHLight light : allLights) {
            PHLightState lightState = new PHLightState();
            lightState.setHue(rand.nextInt(MAX_HUE));
            bridge.updateLightState(light, lightState, listener);
        }
    }

    public void lightOn() {
        PHBridge bridge = phHueSDK.getSelectedBridge();

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        for (PHLight light : allLights) {
            PHLightState lightState = new PHLightState();
            lightState.setOn(true);
            bridge.updateLightState(light, lightState, listener);
        }
    }

    public void lightOff() {
        PHBridge bridge = phHueSDK.getSelectedBridge();

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        for (PHLight light : allLights) {
            PHLightState lightState = new PHLightState();
            lightState.setOn(false);
            bridge.updateLightState(light, lightState, listener);
        }
        //OJFohe
    }

    PHLightListener listener = new PHLightListener() {

        @Override
        public void onSuccess() {
        }

        @Override
        public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {
            Log.w(TAG, "Light has updated");
        }

        @Override
        public void onError(int arg0, String arg1) {
        }

        @Override
        public void onReceivingLightDetails(PHLight arg0) {
        }

        @Override
        public void onReceivingLights(List<PHBridgeResource> arg0) {
        }

        @Override
        public void onSearchComplete() {
        }
    };

    protected void onDestroy() {
        PHBridge bridge = phHueSDK.getSelectedBridge();
        if (bridge != null) {

            if (phHueSDK.isHeartbeatEnabled(bridge)) {
                phHueSDK.disableHeartbeat(bridge);
            }

            phHueSDK.disconnect(bridge);
            super.onDestroy();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changeName:
                Intent intent = new Intent(MainActivity.this, ChangeName.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
}