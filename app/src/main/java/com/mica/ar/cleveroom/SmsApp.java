package com.mica.ar.cleveroom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;

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
 * Lamp blinks when a SMS is received
 */


public class SmsApp extends BroadcastReceiver {
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    private static PHHueSDK phHueSDK;

    private final String ACTION_RECEIVE_SMS = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive (Context context, Intent intent)  {
        Preferences pref = Preferences.getInstance(context);
        int color = Preferences.getColor();
        int saturation = Preferences.getSaturation();
        int brightness = Preferences.getBrightness();
        if ((intent.getAction().equals(ACTION_RECEIVE_SMS)) && pref.getSms()) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                PHLightListener listener = new PHLightListener() {
                    @Override
                    public void onSuccess() {                    }

                    @Override
                    public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {
                        Log.w(MainActivity.TAG, "Light has updated");
                    }

                    @Override
                    public void onError(int arg0, String arg1) {                    }

                    @Override
                    public void onReceivingLightDetails(PHLight arg0) {                    }

                    @Override
                    public void onReceivingLights(List<PHBridgeResource> arg0) {                    }

                    @Override
                    public void onSearchComplete() {                    }
                };

                PHHueSDK phHueSDK;
                phHueSDK = PHHueSDK.create();
                PHBridge bridge = phHueSDK.getSelectedBridge();
                Preferences prefs = Preferences.getInstance(context);
                String lampe_id = prefs.getLightChose();
                List<PHLight> allLights = bridge.getResourceCache().getAllLights();

                for (PHLight light : allLights) {
                    if(light.getIdentifier().equals(lampe_id)) {
                        PHLightState lightState = new PHLightState();
                        //light flashs in green when the user receives an sms
                        lightState.setHue(28000);
                        lightState.setOn(true);
                        lightState.setAlertMode(PHLight.PHLightAlertMode.ALERT_LSELECT);
                        bridge.updateLightState(light, lightState, listener);
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        lightState.setOn(false);
                        bridge.updateLightState(light, lightState, listener);
                    }
                }
            }
            Preferences prefs = Preferences.getInstance(context);
            final String lampe_id = prefs.getLightChose();
            PHBridge bridge = phHueSDK.getSelectedBridge();
            List<PHLight> allLights = bridge.getResourceCache().getAllLights();

            for (PHLight light : allLights) {
                if (light.getIdentifier().equals(lampe_id)) {
                    PHLightState lightState = new PHLightState();
                    lightState.setHue(color);
                    lightState.setBrightness(brightness);
                    lightState.setSaturation(saturation);
                    lightState.setOn(true);
                    bridge.updateLightState(light, lightState, listener);
                }
            }
        }
    }

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