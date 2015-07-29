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

/**
 * Main Activity
 * Actions on the selected lamp
 *      On
 *      Off
 *      Change light
 *      Create scene
 */
public class MainActivity extends Activity {
    private PHHueSDK phHueSDK;
    public static final String TAG = "CleveRoom";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_main);
        phHueSDK = PHHueSDK.create();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        Preferences prefs = Preferences.getInstance(getApplicationContext());

        final String lampe_id = prefs.getLightChose();
        String lampe = "";

        for (PHLight light : allLights) {
            if(light.getIdentifier().equals(lampe_id))
                lampe=light.getName();
        }

        TextView t1 = (TextView) findViewById(R.id.lightname);
        t1.setText(lampe);

        Button onButton;
        onButton = (Button) findViewById(R.id.buttonOn);
        onButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                lightOn(lampe_id);
            }

        });

        Button offButton;
        offButton = (Button) findViewById(R.id.buttonOff);
        offButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                lightOff(lampe_id);
            }

        });

        Button colorButton;
        colorButton = (Button) findViewById(R.id.changeColor);
        colorButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changeColor();
            }

        });

        Button prefButton;
        prefButton = (Button) findViewById(R.id.scene);
        prefButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                doScene();
            }
        });
    }

    public void lightOn(String id) {
        PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        for (PHLight light : allLights) {
            if(light.getIdentifier().equals(id)) {
                PHLightState lightState = new PHLightState();
                lightState.setOn(true);
                bridge.updateLightState(light, lightState, listener);
            }
        }
    }

    public void lightOff(String id) {
        PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        for (PHLight light : allLights) {
            if(light.getIdentifier().equals(id)) {
                PHLightState lightState = new PHLightState();
                lightState.setOn(false);
                bridge.updateLightState(light, lightState, listener);
            }
        }
    }

    public void changeColor() {
        Intent intent = new Intent(MainActivity.this, Colors.class);
        startActivity(intent);
    }

    public void doScene(){
        Intent intent = new Intent(MainActivity.this, ApplyScene.class);
        startActivity(intent);
    }

    PHLightListener listener = new PHLightListener() {
        @Override
        public void onSuccess() {        }

        @Override
        public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {
            Log.w(TAG, "Light has updated");
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

    protected void onDestroy() {
        PHBridge bridge = phHueSDK.getSelectedBridge();
        if (bridge != null) {
            if (phHueSDK.isHeartbeatEnabled(bridge))
                phHueSDK.disableHeartbeat(bridge);
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
                break;
            case R.id.spotLight:
                doClignoter();
                break;
            case R.id.options:
                Intent options = new Intent(MainActivity.this, Options.class);
                startActivity(options);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void doClignoter() {
        Preferences prefs = Preferences.getInstance(getApplicationContext());
        final String lampe_id = prefs.getLightChose();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        int i;
        int color = Preferences.getColor();
        int brightness = Preferences.getBrightness();
        int saturation = Preferences.getSaturation();
        boolean on = Preferences.getOn();
        for(i=0; i<65280; i=i+6528) {
            for (PHLight light : allLights) {
                if(light.getIdentifier().equals(lampe_id)){
                    PHLightState lightState = new PHLightState();
                    lightState.setOn(true);
                    lightState.setHue(i);
                    lightState.setBrightness(200);
                    lightState.setSaturation(200);
                    bridge.updateLightState(light, lightState, listener);
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lightOff(lampe_id);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Preferences.applyLampStates(color, brightness, saturation);
        Preferences.setOn(on);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(MainActivity.this, ListLightActivity.class);
        startActivity(intent);
    }
}