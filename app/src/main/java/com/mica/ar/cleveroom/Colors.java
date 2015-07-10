package com.mica.ar.cleveroom;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
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

/*
 * Change the 4 settings of the lamp's light
 */
public class Colors extends Activity{
    private SeekBar seekBar_Color;
    private SeekBar seekBar_Saturation;
    private SeekBar seekBar_Brightness;
    private SeekBar seekBar_Contrast;

    private TextView change_color;
    private TextView change_saturation;
    private TextView change_brightness;
    private TextView change_contrast;

    public static final String TAG = "CleveRoom";
    public int color;
    public int saturation;
    public int brightness;
    public int contrast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.change_color);

        seekBar_Color = (SeekBar) findViewById(R.id.seekBar1);
        seekBar_Saturation = (SeekBar) findViewById(R.id.seekBar3);
        seekBar_Brightness = (SeekBar) findViewById(R.id.seekBar4);
        //seekBar_Contrast = (SeekBar) findViewById(R.id.seekBar2);

        change_color = (TextView) findViewById(R.id.color);
        change_saturation = (TextView) findViewById(R.id.saturation);
        change_brightness = (TextView) findViewById(R.id.brightness);
        //change_contrast = (TextView) findViewById(R.id.contrast);


        change_color.setText(R.string.color);
        change_saturation.setText(R.string.saturation);
        change_brightness.setText(R.string.brightness);
        //change_contrast.setText(R.string.contrast);

        seekBar_Color.setProgress(getColor());
        seekBar_Saturation.setProgress(getSaturation());
        seekBar_Brightness.setProgress(getBrightness());
        //seekBar_Contrast.setProgress(getContrast());

        //Light nuance
        seekBar_Color.setOnSeekBarChangeListener(
                new OnSeekBarChangeListener() {
                    int progress = 0;

                    @Override
                    public void onProgressChanged(SeekBar seekBar1, int progresValue, boolean fromUser) {
                        progress = progresValue;

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

        //Light saturation
        seekBar_Saturation.setOnSeekBarChangeListener(
                new OnSeekBarChangeListener() {
                    int progress = 0;

                    @Override
                    public void onProgressChanged(SeekBar seekBar2, int progresValue, boolean fromUser) {
                        progress = progresValue;

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

        //Light brightness
        seekBar_Brightness.setOnSeekBarChangeListener(
                new OnSeekBarChangeListener() {
                    int progress = 0;

                    @Override
                    public void onProgressChanged(SeekBar seekBar2, int progresValue, boolean fromUser) {
                        progress = progresValue;

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
        /*seekBar_Contrast.setOnSeekBarChangeListener(
                new OnSeekBarChangeListener() {
                    int progress = 0;

                    @Override
                    public void onProgressChanged(SeekBar seekBar2, int progresValue, boolean fromUser) {
                        progress = progresValue;

                        PHHueSDK phHueSDK = PHHueSDK.create();
                        PHBridge bridge = phHueSDK.getSelectedBridge();
                        Preferences prefs = Preferences.getInstance(getApplicationContext());
                        final String lampe_id = prefs.getLightChose();
                        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

                        for (PHLight light : allLights) {
                            if (light.getIdentifier().equals(lampe_id)) {
                                PHLightState lightState = new PHLightState();
                                lightState.setCt(progress);
                                lightState.setOn(true);
                                bridge.updateLightState(light, lightState, listener);
                                System.out.println(light.getLastKnownLightState().getCt());
                            }
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });*/
    }

    PHLightListener listener = new PHLightListener() {
        @Override
        public void onSuccess() {                        }

        @Override
        public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {
            Log.w(TAG, "Light has updated");
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

    public int getColor(){

        PHHueSDK phHueSDK = PHHueSDK.create();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        Preferences prefs = Preferences.getInstance(getApplicationContext());
        final String lampe_id = prefs.getLightChose();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        for (PHLight light : allLights) {
            if (light.getIdentifier().equals(lampe_id)) {
                this.color = light.getLastKnownLightState().getHue();

            }
        }

        return this.color;
    }

    public int getSaturation(){

        PHHueSDK phHueSDK = PHHueSDK.create();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        Preferences prefs = Preferences.getInstance(getApplicationContext());
        final String lampe_id = prefs.getLightChose();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        for (PHLight light : allLights) {
            if (light.getIdentifier().equals(lampe_id)) {
                this.saturation = light.getLastKnownLightState().getSaturation();

            }
        }

        return this.saturation;
    }

    public int getBrightness(){

        PHHueSDK phHueSDK = PHHueSDK.create();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        Preferences prefs = Preferences.getInstance(getApplicationContext());
        final String lampe_id = prefs.getLightChose();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        for (PHLight light : allLights) {
            if (light.getIdentifier().equals(lampe_id)) {
                this.brightness = light.getLastKnownLightState().getBrightness();

            }
        }

        return this.brightness;
    }

    public int getContrast() {
        PHHueSDK phHueSDK = PHHueSDK.create();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        Preferences prefs = Preferences.getInstance(getApplicationContext());
        final String lampe_id = prefs.getLightChose();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        for (PHLight light : allLights) {
            if (light.getIdentifier().equals(lampe_id)) {
                this.brightness = light.getLastKnownLightState().getCt();

            }
        }
        return this.contrast;
    }
}

