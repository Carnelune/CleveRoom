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


    //List<String> preferences_list = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_scene);
        liste = (ListView) findViewById(R.id.listView);
        int nb_scenes;
        FileOutputStream output = null;
        FileOutputStream out = null;
        FileInputStream input = null;
        ObjectInputStream in = null;

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
                output = openFileOutput(LIGHTS, MODE_PRIVATE);
                output.write(new_scene.getBytes());

                output.write("\n".getBytes());
                if(output != null)
                    output.close();

                out = openFileOutput(COLORS, MODE_PRIVATE);


                out.write(Preferences.getBrightness());
                System.out.println("La luminosite est : " + Preferences.getBrightness());
                out.write("\n".getBytes());
                out.write(Preferences.getSaturation());
                System.out.println("La saturation est : " + Preferences.getSaturation());
                out.write("\n".getBytes());
                out.write(Preferences.getColor());
                System.out.println("La couleur est : " + Preferences.getColor());
                out.write("\n".getBytes());

                if(out !=null)
                    out.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        try {
            input = openFileInput(LIGHTS);
            int value;
            while((value = input.read()) != -1) {
                StringBuffer lu = new StringBuffer();
                while((value!= 10)){
                    //System.out.println(value);
                    lu.append((char)value);
                    value = input.read();
                }
                nom = lu.toString();
                List_name.add(nom);
            }

            if(input != null)
                input.close();


            System.out.println("Je vais ouvrir le fichier");
            in = new ObjectInputStream(new FileInputStream(COLORS));
            System.out.println("fichier ouvert");
            long v;
            while((v = in.readLong()) != -1) {
                //StringBuffer lu = new StringBuffer();
                while((v!= 10)){
                    System.out.println("v vaut: " + v);
                    //lu.append(v);
                    //lu.append((Long) v);
                    //System.out.println("lu vaut: " + lu);
                    //number = Long.parseLong(lu.toString());
                    v = in.read();
                }
                //System.out.println(number);
                //v = in.read();

                //List_colors.add(number);
                //System.out.println(number);
            }
            if(in != null)
                in.close();

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

                applyLampStates(Preferences.getList(position + 2 * position), Preferences.getList(position + 2 * position + 1), Preferences.getList(position + 2 * position + 2));

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

    public static void ecrireFichierI(File dir,String nomFichier, String monText) {

        BufferedWriter writer = null;

        try {

            File newfile = new File(dir.getAbsolutePath() + File.separator + nomFichier);

            newfile.createNewFile();
            writer = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(newfile)));
            writer.write(monText);

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (writer != null) {

                try {

                    writer.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }
            }
        }
    }


    public static String lireFichierI(File dir, String nomFichier) {

        File newfile = new File(dir.getAbsolutePath() + File.separator + nomFichier);

        String monText = "";

        BufferedReader input = null;

        try {

            input = new BufferedReader(new InputStreamReader(
                    new FileInputStream(newfile)

            ));

            String line;

            StringBuffer buffer = new StringBuffer();

            while ((line = input.readLine()) != null) {

                buffer.append(line);

            }

            monText = buffer.toString();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (input != null) {

                try {

                    input.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }

        return monText;

    }

}
