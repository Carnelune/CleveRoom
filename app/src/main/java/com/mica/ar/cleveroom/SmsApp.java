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
 * Lamp blinks when an SMS is received
 */

public class SmsApp extends BroadcastReceiver {
    // Get the object of SmsManager
     private final String ACTION_RECEIVE_SMS = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive (Context context, Intent intent)  {
        Preferences prefs = Preferences.getInstance(context);
        //The current color of the light is saved in variable color
        int color = Preferences.getColor();
        //The current saturation of the light is saved in variable saturation
        int saturation = Preferences.getSaturation();
        //The current brightness of the light is saved in variable brightness
        int brightness = Preferences.getBrightness();
        //on is true if the lamp is currently on
        boolean on = Preferences.getOn();
        //The system checks if an SMS has been received AND if the CheckBox_SMS is checked
        if ((intent.getAction().equals(ACTION_RECEIVE_SMS)) && prefs.getSms()) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                PHHueSDK phHueSDK;
                phHueSDK = PHHueSDK.create();
                PHBridge bridge = phHueSDK.getSelectedBridge();
                String lampe_id = prefs.getLightChose();
                List<PHLight> allLights = bridge.getResourceCache().getAllLights();

                for (PHLight light : allLights) {
                    if(light.getIdentifier().equals(lampe_id)) {
                        PHLightState lightState = new PHLightState();
                        //light flashes in green when the user receives an sms
                        lightState.setHue(28000);
                        lightState.setOn(true);
                        lightState.setAlertMode(PHLight.PHLightAlertMode.ALERT_LSELECT);
                        bridge.updateLightState(light, lightState);
                        try {
                            //We make the light blinks for 5 seconds
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        lightState.setOn(false);
                        bridge.updateLightState(light, lightState);
                    }
                }
            }
            //At the end we put back the previous states of the light
            Preferences.applyLampStates(color,brightness,saturation);
            Preferences.setLamp(on);
        }
    }
}