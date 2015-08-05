package com.mica.ar.cleveroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHLight;

/**
 * Allows to select a light
 */
public class ListLightActivity extends Activity implements AdapterView.OnItemClickListener {
    private PHHueSDK phHueSDK;
    private ListLampAdapter listLights;
    public static final String TAG = "HueMica";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lightlistlinear);

        phHueSDK = PHHueSDK.create();
        phHueSDK.setAppName("HueMicaApp");
        phHueSDK.setDeviceName(android.os.Build.MODEL);

        listLights = new ListLampAdapter(getApplicationContext(), phHueSDK.getSelectedBridge().getResourceCache().getAllLights());
        ListView lightList = (ListView) findViewById(R.id.light_list);
        lightList.setOnItemClickListener(this);
        lightList.setAdapter(listLights);
    }


    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Preferences prefs = Preferences.getInstance(getApplicationContext());
        PHLight selectedLight = (PHLight) listLights.getItem(position);
        prefs.setLightChose(selectedLight.getIdentifier());
        startMainActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        phHueSDK.disableAllHeartbeat();
    }

    public void startMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            intent.addFlags(0x8000);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(ListLightActivity.this, HomeBridgeActivity.class);
        startActivity(intent);
    }
}
