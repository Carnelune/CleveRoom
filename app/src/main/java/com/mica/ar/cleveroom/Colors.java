package com.mica.ar.cleveroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
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

/*
 * Change the 3 settings of the lamp's light: The color, the brightness and the saturation
 * launch the AddScene activity by pressing the Add_Scene button.
 */
public class Colors extends Activity{
    private SeekBar seekBar_Color;
    private SeekBar seekBar_Saturation;
    private SeekBar seekBar_Brightness;


    private TextView change_color;
    private TextView change_saturation;
    private TextView change_brightness;

    private Button add_scene;
    private Button OK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.change_color);

        seekBar_Color = (SeekBar) findViewById(R.id.seekBar1);
        seekBar_Saturation = (SeekBar) findViewById(R.id.seekBar3);
        seekBar_Brightness = (SeekBar) findViewById(R.id.seekBar4);


        change_color = (TextView) findViewById(R.id.color);
        change_saturation = (TextView) findViewById(R.id.saturation);
        change_brightness = (TextView) findViewById(R.id.brightness);

        add_scene = (Button) findViewById(R.id.add_scene);
        add_scene.setOnClickListener(add_sceneListener);

        OK = (Button) findViewById(R.id.OK);
        OK.setOnClickListener(OKListener);

        change_color.setText(R.string.color);
        change_saturation.setText(R.string.saturation);
        change_brightness.setText(R.string.brightness);

        //When the Colors activity is launched, the three seekbars are updated following the current color, saturation and brightness of the light
        seekBar_Color.setProgress(Preferences.getColor());
        seekBar_Saturation.setProgress(Preferences.getSaturation());
        seekBar_Brightness.setProgress(Preferences.getBrightness());


        seekBar_Color.setOnSeekBarChangeListener(
                //When the user will modify the seekBar, the color of the light will be updated according to the position of the button on the seekbar
                new OnSeekBarChangeListener() {
                    int progress = 0;

                    @Override
                    public void onProgressChanged(SeekBar seekBar1, int progresValue, boolean fromUser) {
                        progress = progresValue;
                        //The progresValue goes from 0 to 65536 so as the parameter of the setHue function which applies a color to the lamp.
                        PHHueSDK phHueSDK = PHHueSDK.create();
                        PHBridge bridge = phHueSDK.getSelectedBridge();
                        Preferences prefs = Preferences.getInstance(getApplicationContext());
                        final String lampe_id = prefs.getLightChose();
                        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

                        for (PHLight light : allLights) {
                            if (light.getIdentifier().equals(lampe_id)) {
                                PHLightState lightState = new PHLightState();
                                lightState.setHue(progress);
                                lightState.setOn(true);
                                bridge.updateLightState(light, lightState, listener);
                            }
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar1) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar1) {
                    }
                });


        seekBar_Saturation.setOnSeekBarChangeListener(
                //When the user will modify the seekBar, the saturation of the light will be updated according to the position of the button on the seekbar
                new OnSeekBarChangeListener() {
                    int progress = 0;

                    @Override
                    public void onProgressChanged(SeekBar seekBar2, int progresValue, boolean fromUser) {
                        progress = progresValue;
                        //The progresValue goes from 0 to 250 so as the parameter of the setSaturation function which applies a saturation to the lamp.

                        PHHueSDK phHueSDK = PHHueSDK.create();
                        PHBridge bridge = phHueSDK.getSelectedBridge();
                        Preferences prefs = Preferences.getInstance(getApplicationContext());
                        final String lampe_id = prefs.getLightChose();
                        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

                        for (PHLight light : allLights) {
                            if (light.getIdentifier().equals(lampe_id)) {
                                PHLightState lightState = new PHLightState();
                                lightState.setSaturation(progress);
                                lightState.setOn(true);
                                bridge.updateLightState(light, lightState, listener);
                            }
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });


        seekBar_Brightness.setOnSeekBarChangeListener(
                //When the user will modify the seekBar, the brightness of the light will be updated according to the position of the button on the seekbar
                new OnSeekBarChangeListener() {
                    int progress = 0;

                    @Override
                    public void onProgressChanged(SeekBar seekBar2, int progresValue, boolean fromUser) {
                        progress = progresValue;
                        //The progresValue goes from 0 to 255 so as the parameter of the setBrightness function which applies a brightness to the lamp.

                        PHHueSDK phHueSDK = PHHueSDK.create();
                        PHBridge bridge = phHueSDK.getSelectedBridge();
                        Preferences prefs = Preferences.getInstance(getApplicationContext());
                        final String lampe_id = prefs.getLightChose();
                        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

                        for (PHLight light : allLights) {
                            if (light.getIdentifier().equals(lampe_id)) {
                                PHLightState lightState = new PHLightState();
                                lightState.setBrightness(progress );
                                lightState.setOn(true);
                                bridge.updateLightState(light, lightState, listener);
                            }
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
    }

    //By pressing the OK button, the current activity is closed
    private View.OnClickListener OKListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Colors.this.finish();
        }
    };

    //By pressing the add_scene button, the AddScene activity is launched
    private View.OnClickListener add_sceneListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Colors.this, AddScene.class);
            startActivity(intent);
        }
    };

    PHLightListener listener = new PHLightListener() {
        @Override
        public void onSuccess() {                        }

        @Override
        public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {
            Log.w(MainActivity.TAG, "Light has updated");
        }

        @Override
        public void onError(int arg0, String arg1) {                        }

        @Override
        public void onReceivingLightDetails(PHLight arg0) {                        }

        @Override
        public void onReceivingLights(List<PHBridgeResource> arg0) {                        }

        @Override
        public void onSearchComplete() {                        }
    };
}

