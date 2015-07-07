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

public class Colors extends Activity{
    private SeekBar seekBar1;
    private SeekBar seekBar2;
    private SeekBar seekBar3;
    private SeekBar seekBar4;
    private TextView change_color;
    private TextView change_saturation;
    private TextView change_contrast;
    private TextView change_brightness;
    public static final String TAG = "CleveRoom";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_color);
        seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
        seekBar2 = (SeekBar) findViewById(R.id.seekBar2);
        seekBar3 = (SeekBar) findViewById(R.id.seekBar3);
        seekBar4 = (SeekBar) findViewById(R.id.seekBar4);
        change_color = (TextView) findViewById(R.id.color);
        change_contrast = (TextView) findViewById(R.id.contrast);
        change_saturation = (TextView) findViewById(R.id.saturation);
        change_brightness = (TextView) findViewById(R.id.brightness);
        // Initialize the textview with '0'
        change_color.setText(R.string.color);
        change_contrast.setText(R.string.contrast);
        change_saturation.setText(R.string.saturation);
        change_brightness.setText(R.string.brightness);


        seekBar1.setOnSeekBarChangeListener(
                new OnSeekBarChangeListener() {
                    //PHHueSDK phHueSDK;
                    //PHBridge bridge = phHueSDK.getSelectedBridge();
                    //List<PHLight> allLights = bridge.getResourceCache().getAllLights();

                    int progress = 0;

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

                    @Override
                    public void onProgressChanged(SeekBar seekBar1,
                                                  int progresValue, boolean fromUser) {
                        progress = progresValue;
                        PHHueSDK phHueSDK = PHHueSDK.create();
                        PHBridge bridge = phHueSDK.getSelectedBridge();
                        Preferences prefs = Preferences.getInstance(getApplicationContext());
                        final String lampe_id = prefs.getLightChose();
                        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
                        for (PHLight light : allLights) {
                            if (light.getIdentifier().equals(lampe_id)) {
                                PHLightState lightState = new PHLightState();
                                lightState.setOn(true);
                                lightState.setHue(progress-1);
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

        seekBar2.setOnSeekBarChangeListener(
                new OnSeekBarChangeListener() {


                    int progress = 0;

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

                    @Override
                    public void onProgressChanged(SeekBar seekBar2,
                                                  int progresValue, boolean fromUser) {
                        progress = progresValue;
                        PHHueSDK phHueSDK = PHHueSDK.create();
                        PHBridge bridge = phHueSDK.getSelectedBridge();
                        Preferences prefs = Preferences.getInstance(getApplicationContext());
                        final String lampe_id = prefs.getLightChose();
                        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
                        for (PHLight light : allLights) {
                            if (light.getIdentifier().equals(lampe_id)) {
                                PHLightState lightState = new PHLightState();
                                lightState.setOn(true);
                                lightState.setCt(153 + (progress));
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

        seekBar3.setOnSeekBarChangeListener(
                new OnSeekBarChangeListener() {


                    int progress = 0;

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

                    @Override
                    public void onProgressChanged(SeekBar seekBar2,
                                                  int progresValue, boolean fromUser) {
                        progress = progresValue;
                        PHHueSDK phHueSDK = PHHueSDK.create();
                        PHBridge bridge = phHueSDK.getSelectedBridge();
                        Preferences prefs = Preferences.getInstance(getApplicationContext());
                        final String lampe_id = prefs.getLightChose();
                        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
                        for (PHLight light : allLights) {
                            if (light.getIdentifier().equals(lampe_id)) {
                                PHLightState lightState = new PHLightState();
                                lightState.setOn(true);
                                lightState.setSaturation(200 + progress);
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
        seekBar4.setOnSeekBarChangeListener(
                new OnSeekBarChangeListener() {


                    int progress = 0;

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

                    @Override
                    public void onProgressChanged(SeekBar seekBar2,
                                                  int progresValue, boolean fromUser) {
                        progress = progresValue;
                        PHHueSDK phHueSDK = PHHueSDK.create();
                        PHBridge bridge = phHueSDK.getSelectedBridge();
                        Preferences prefs = Preferences.getInstance(getApplicationContext());
                        final String lampe_id = prefs.getLightChose();
                        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
                        for (PHLight light : allLights) {
                            if (light.getIdentifier().equals(lampe_id)) {
                                PHLightState lightState = new PHLightState();
                                lightState.setOn(true);
                                lightState.setBrightness(progress -1);
                                bridge.updateLightState(light, lightState, listener);
                            }

                        }

                    }


                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {


                    }



                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // Display the value in textview




                    }
                });

    }
}

