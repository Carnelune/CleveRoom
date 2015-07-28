package com.mica.ar.cleveroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Toast;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.io.*;

/**
 * Apply a scene on the current light
 */
public class ApplyScene extends Activity{
    ListView liste = null;
    Boolean ajout = false;
    final String LIGHTS = "Name_scenes.txt";
    final String COLORS = "Lights_configuration.txt";
    List<String> List_name = new ArrayList<>();
    List<Integer> List_colors = new ArrayList<>();
    ArrayAdapter<String> adapter = null;
    String nom;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_scene);
        liste = (ListView) findViewById(R.id.listView);
        FileOutputStream outName= null;
        FileInputStream inName = null;
        String test = new String();
        PHHueSDK phHueSDK;
        phHueSDK = PHHueSDK.create();
        PHBridge bridge = phHueSDK.getSelectedBridge();
        Preferences prefs = Preferences.getInstance(getApplicationContext());
        String lampe_id = prefs.getLightChose();
        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        for (PHLight light : allLights) {
            if (light.getIdentifier().equals(lampe_id)) {

                if (this.getIntent().getExtras() != null) {
                    ajout = true;
                    Intent i = getIntent();
                    String new_scene = i.getStringExtra(AddScene.NOM);

                    try {
                        // le mode MODE_APPEND fait en sorte de concatener les elements du fichier au lieu de les remplacer
                        outName = openFileOutput(LIGHTS, MODE_APPEND);
                        outName.write(new_scene.getBytes());
                        outName.write("\n".getBytes());
                        if (outName != null)
                            outName.close();

                        OutputStreamWriter outColor = new OutputStreamWriter(openFileOutput(COLORS, MODE_APPEND));
                        test = Integer.toString(Preferences.getColor());
                        outColor.write(test + "\n");
                        test = Integer.toString(Preferences.getBrightness());
                        outColor.write(test + "\n");
                        test = Integer.toString(Preferences.getSaturation());
                        outColor.write(test + "\n");


                        if (outColor != null)
                            outColor.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    inName = openFileInput(LIGHTS);
                    int value;
                    while ((value = inName.read()) != -1) {
                        StringBuffer lu = new StringBuffer();
                        while ((value != 10)) {
                            lu.append((char) value);
                            value = inName.read();
                        }
                        nom = lu.toString();
                        List_name.add(nom);
                    }

                    if (inName != null)
                        inName.close();

                    InputStream inColor = openFileInput(COLORS);
                    InputStreamReader inputreader = new InputStreamReader(inColor);
                    BufferedReader buffreader = new BufferedReader(inputreader);

                    String line;
                    while ((line = buffreader.readLine()) != null) {
                        List_colors.add(Integer.parseInt(line));
                    }
                    inColor.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, List_name);
                liste.setAdapter(adapter);
                registerForContextMenu(liste);

                liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Preferences.applyLampStates(List_colors.get(3 * position), List_colors.get(3 * position + 1), List_colors.get(3 * position + 2));
                    }
                });
            }
        }

    }

    private void delete(int position){
        try {
            List_name.remove(List_name.get(position));
            List_colors.remove(List_colors.get(3* position));
            List_colors.remove(List_colors.get(3*position ));
            List_colors.remove(List_colors.get(3* position));
            FileOutputStream outName2 = openFileOutput(LIGHTS, MODE_PRIVATE);
            OutputStreamWriter outColor2 = new OutputStreamWriter(openFileOutput(COLORS, MODE_PRIVATE));
            String test2;
            if(List_name.size()==0){
                outName2.write("".getBytes());
                outName2.close();
                outColor2.write("");
                outColor2.close();
            }
            else{
                outName2.write(List_name.get(0).getBytes());
                outName2.write("\n".getBytes());
                outName2.close();
                test2 = Integer.toString(List_colors.get(0));
                outColor2.write(test2 + "\n");
                test2 = Integer.toString(List_colors.get(1));
                outColor2.write(test2 + "\n");
                test2 = Integer.toString(List_colors.get(2));
                outColor2.write(test2 + "\n");
                outColor2.close();
                FileOutputStream outName3 = openFileOutput(LIGHTS, MODE_APPEND);
                OutputStreamWriter outColor3 = new OutputStreamWriter(openFileOutput(COLORS, MODE_APPEND));
                String test3;
                int i = 1;
                while(i<List_name.size()){
                    outName3.write(List_name.get(i).getBytes());
                    outName3.write("\n".getBytes());
                    test3 = Integer.toString(List_colors.get(3*i));
                    outColor3.write(test3 + "\n");
                    test3 = Integer.toString(List_colors.get(3*i + 1));
                    outColor3.write(test3 + "\n");
                    test3 = Integer.toString(List_colors.get(3*i + 2));
                    outColor3.write(test3 + "\n");
                    i++;
                }
                outName3.close();
                outColor3.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_apply_scene, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
                delete((int)info.id);
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void onBackPressed(){
        Intent intent = new Intent(ApplyScene.this, (MainActivity.class));
        startActivity(intent);
    }
}