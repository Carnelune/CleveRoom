package com.mica.ar.cleveroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sarah on 18/06/2015.
 */
public class ApplyPreferences extends Activity {

    ListView liste = null;
    List<String> preferences_list = new ArrayList<String>();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_preferences);
        liste = (ListView) findViewById(R.id.listView);
        Intent i = getIntent();


        preferences_list.add("Matin");
        preferences_list.add("Cafe");
        preferences_list.add("Soir");
        preferences_list.add("Aprem");
        preferences_list.add("Fatigue");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, preferences_list);
        liste.setAdapter(adapter);
    }




}
