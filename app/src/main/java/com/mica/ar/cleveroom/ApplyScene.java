package com.mica.ar.cleveroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
    String nom;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_scene);
        liste = (ListView) findViewById(R.id.listView);
        int nb_scenes;
        FileOutputStream outName= null;
        FileInputStream inName = null;
        String test = new String();

        if (this.getIntent().getExtras() != null) {
            ajout = true;
            Intent i = getIntent();
            String new_scene = i.getStringExtra(AddScene.NOM);
            Preferences.IncrementerNbScenes();
            nb_scenes = Preferences.getNbScenes();
            Preferences.addList(new_scene);
            //Preferences.addList(nb_scenes);

            Preferences.addList(Preferences.getColor());
            Preferences.addList(Preferences.getBrightness());
            Preferences.addList(Preferences.getSaturation());

            try {
                // le mode MODE_APPEND fait en sorte de concatener les elements du fichier au lieu de les remplacer
                outName = openFileOutput(LIGHTS, MODE_APPEND);
                outName.write(new_scene.getBytes());
                outName.write("\n".getBytes());
                if(outName != null)
                    outName.close();

                OutputStreamWriter outColor = new OutputStreamWriter(openFileOutput(COLORS, MODE_APPEND));
                test = Integer.toString(Preferences.getColor());
                outColor.write(test + "\n");
                System.out.println("La couleur est : " + Preferences.getColor());
                test = Integer.toString(Preferences.getBrightness());
                outColor.write(test + "\n");
                System.out.println("La luminosite est : " + Preferences.getBrightness());
                test = Integer.toString(Preferences.getSaturation());
                outColor.write(test + "\n");
                System.out.println("La saturation est : " + Preferences.getSaturation());


                if(outColor !=null)
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
            while((value = inName.read()) != -1) {
                StringBuffer lu = new StringBuffer();
                while((value!= 10)){
                    lu.append((char)value);
                    value = inName.read();
                }
                nom = lu.toString();
                List_name.add(nom);
            }

            if(inName != null)
                inName.close();

            InputStream inColor = openFileInput(COLORS);
            InputStreamReader inputreader = new InputStreamReader(inColor);
            BufferedReader buffreader = new BufferedReader(inputreader);

            String line;
            while (( line = buffreader.readLine()) != null) {
                List_colors.add(Integer.parseInt(line));
                System.out.println(Integer.parseInt(line));
            }
            inColor.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, List_name);
        liste.setAdapter(adapter);

        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //int i = 0;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                applyLampStates(List_colors.get(position + 2 * position), List_colors.get(position + 2 * position + 1), List_colors.get(position + 2 * position + 2));

            }
        });
    }

    private void applyLampStates(int color, int brightness, int saturation) {
        Preferences prefs = Preferences.getInstance(getApplicationContext());
        final String lampe_id = prefs.getLightChose();
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
                bridge.updateLightState(light, lightState, listener);
            }
        }
    }

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

    public void onBackPressed(){
        Intent intent = new Intent(ApplyScene.this, (MainActivity.class));
        startActivity(intent);
    }


    public byte[] toBytes(int i)
    {
        byte[] result = new byte[4];

        result[0] = (byte) (i >> 24);
        result[1] = (byte) (i >> 16);
        result[2] = (byte) (i >> 8);
        result[3] = (byte) (i /*>> 0*/);

        return result;
    }


}
