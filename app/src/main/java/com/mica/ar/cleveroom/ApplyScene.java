package com.mica.ar.cleveroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Apply a scene on the current light
 */
public class ApplyScene extends Activity {
    ListView liste = null;
    List<String> preferences_list = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_scene);
        liste = (ListView) findViewById(R.id.listView);

        preferences_list.add("Matin");
        preferences_list.add("Cafe");
        preferences_list.add("Soir");
        preferences_list.add("Aprem");
        preferences_list.add("Fatigue");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, preferences_list);
        liste.setAdapter(adapter);
    }
}
