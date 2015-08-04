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
 *      Apply scene
 *      Change the lamp's name
 *      Spot the light
 *      Options
 */
public class MainActivity extends Activity {
    private PHHueSDK phHueSDK;
    public static final String TAG = "CleveRoom";
    private int count = 0 ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_main);
        phHueSDK = PHHueSDK.create();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        Preferences prefs = Preferences.getInstance(getApplicationContext());
        //We save the id of the current lamp in lamp_id
        final String lampe_id = prefs.getLightChose();
        String lampe = "";

        for (PHLight light : allLights) {
            if(light.getIdentifier().equals(lampe_id))
                //Here we save the current lamp's name in the variable lampe
                lampe=light.getName();
        }
        //lampe is displayed on the screen
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

    //lightOn(String id) turns on the lamp whose identifier is id
    public void lightOn(String id) {
        count ++ ;

        PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        for (PHLight light : allLights) {
            if(light.getIdentifier().equals(id)) {
                PHLightState lightState = new PHLightState();
                if (count == 10) {
                    Intent intent = new Intent(MainActivity.this, Morse.class);
                    startActivity(intent);
                    count = 0 ;
                }
                lightState.setOn(true);
                bridge.updateLightState(light, lightState, listener);
            }
        }
    }
    //lightOff(String id) turns off the lamp whose identifier is id
    public void lightOff(String id) {
        count = 0 ;
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
    //ChangeColor() starts the Colors activity
    public void changeColor() {
        count = 0 ;
        Intent intent = new Intent(MainActivity.this, Colors.class);
        startActivity(intent);
    }

    //doScene() starts the ApplyScene activity
    public void doScene(){
        count = 0 ;
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

    //We create an option Menu that appears when the user clicks on the button Menu of the Smartphone
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        count = 0 ;

        switch (item.getItemId()) {
            //If the user choses to click on the item "change the lamp's name" the ChangeName activity is launched
            case R.id.changeName:
                Intent intent = new Intent(MainActivity.this, ChangeName.class);
                startActivity(intent);
                break;
            //If the user choses to click on the item "Spot the light" the function doClignoter() is called
            case R.id.spotLight:
                doFlash();
                break;
            //If the user choses the item "Options" the Options activity is launched
            case R.id.options:
                Intent options = new Intent(MainActivity.this, Options.class);
                startActivity(options);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //doFlash() makes the current lamp flash in several colors for 10 seconds
    public void doFlash() {
        Preferences prefs = Preferences.getInstance(getApplicationContext());
        final String lampe_id = prefs.getLightChose();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        int i;
        //We save the current state of the lamp
        int color = Preferences.getColor();
        int brightness = Preferences.getBrightness();
        int saturation = Preferences.getSaturation();
        boolean on = Preferences.getOn();
        for(i=0; i<65536; i=i+6553) {
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
        //At the end we put back the previous states of the light
        Preferences.applyLampStates(color, brightness, saturation);
        Preferences.setLamp(on);
    }

    @Override
    //When the Back button of the Smartphone is pressed, MainActivity  is closed
    //And the system goes back to ListLightActivity.
    public void onBackPressed(){
        Intent intent = new Intent(MainActivity.this, ListLightActivity.class);
        startActivity(intent);
    }
}