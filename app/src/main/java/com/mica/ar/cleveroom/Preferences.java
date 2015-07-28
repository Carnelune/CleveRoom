package com.mica.ar.cleveroom;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.connection.impl.PHBridgeInternal;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Preferences of the user
 *      Selected lamp
 */

public class Preferences {
    private static final String PREFERENCES_STORE = "HueSharedPrefs";
    private static final String LAST_CONNECTED_USERNAME      = "LastConnectedUsername";
    private static final String LAST_CONNECTED_IP            = "LastConnectedIP";
    private static final String LIGHT_CHOSE            = "LastChosenLight";
    private static final String COCHE_SMS = "false";
    private static Preferences instance = null;
    private static SharedPreferences mSharedPreferences = null;
    private SharedPreferences.Editor mSharedPreferencesEditor = null;

    public static Preferences getInstance(Context ctx) {
        if (instance == null)
            instance = new Preferences(ctx);
        return instance;
    }

    private Preferences(Context appContext) {
        mSharedPreferences = appContext.getSharedPreferences(PREFERENCES_STORE, 0); // 0 - for private mode
        mSharedPreferencesEditor = mSharedPreferences.edit();
    }

   public String getUsername() {
        String username = mSharedPreferences.getString(LAST_CONNECTED_USERNAME, "");
        if (username==null || username.equals("")) {
            username = PHBridgeInternal.generateUniqueKey();
            setUsername(username);
        }
        return username;
    }

    public static int getColor(){
        int color = 0;

        PHHueSDK phHueSDK = PHHueSDK.create();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        final String lampe_id = getLightChose();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        for (PHLight light : allLights) {
            if (light.getIdentifier().equals(lampe_id)) {
                color = light.getLastKnownLightState().getHue();

            }
        }

        return color;
    }

    public static int getSaturation(){
        int saturation = 0;

        PHHueSDK phHueSDK = PHHueSDK.create();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        final String lampe_id = getLightChose();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        for (PHLight light : allLights) {
            if (light.getIdentifier().equals(lampe_id)) {
                saturation = light.getLastKnownLightState().getSaturation();

            }
        }

        return saturation;
    }

    public static int getBrightness(){
        int brightness = 0;

        PHHueSDK phHueSDK = PHHueSDK.create();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        final String lampe_id = getLightChose();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        for (PHLight light : allLights) {
            if (light.getIdentifier().equals(lampe_id)) {
                brightness = light.getLastKnownLightState().getBrightness();

            }
        }

        return brightness;
    }

    public static void applyLampStates(int color, int brightness, int saturation) {
        String lampe_id = getLightChose();
        PHHueSDK phHueSDK = PHHueSDK.create();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();

        for (PHLight light : allLights) {
            if (light.getIdentifier().equals(lampe_id)) {
                PHLightState lightState = new PHLightState();
                lightState.setHue(color);
                lightState.setBrightness(brightness);
                lightState.setSaturation(saturation);
                lightState.setOn(true);
                bridge.updateLightState(light, lightState);
            }
        }
    }

    public boolean setUsername(String username) {
        mSharedPreferencesEditor.putString(LAST_CONNECTED_USERNAME, username);
        return (mSharedPreferencesEditor.commit());
    }

    public String getLastConnectedIPAddress() {
        return mSharedPreferences.getString(LAST_CONNECTED_IP, "");
    }

    public boolean setLastConnectedIPAddress(String ipAddress) {
        mSharedPreferencesEditor.putString(LAST_CONNECTED_IP, ipAddress);
        return (mSharedPreferencesEditor.commit());
    }

    public static String getLightChose(){
        return mSharedPreferences.getString(LIGHT_CHOSE, "");
    }

    public boolean setLightChose(String idLight){
        mSharedPreferencesEditor.putString(LIGHT_CHOSE, idLight);
        return (mSharedPreferencesEditor.commit());
    }

    public boolean setSms(Boolean Coche){
        mSharedPreferencesEditor.putBoolean(COCHE_SMS, Coche);
        return (mSharedPreferencesEditor.commit());
    }

    public Boolean getSms(){
        return mSharedPreferences.getBoolean(COCHE_SMS,false);
    }

}
