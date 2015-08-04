package com.mica.ar.cleveroom;

import android.app.Activity;
import android.content.ClipData;
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
 * Apply a scene by clicking on one of the items on the screen
 */
public class ApplyScene extends Activity{
    ListView liste = null;
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
        // Scenes names and lights configurations will be saved in two files:
        // "Name_scenes.txt" and "Lights_configuration"
        //Then, these two files will be copied in two lists:
        //List_name, a String list for the names, and List_colors, an Integer list for the light configurations.
        FileOutputStream outName= null;
        FileInputStream inName = null;
        String test = new String();


        //First, the system checks if the ApplyScene activity
        //has received an Intent from the AddScene activity
        //The Extra would be the name of a new scene to add to the scenes list
        if (this.getIntent().getExtras() != null) {
            Intent i = getIntent();
            String new_scene = i.getStringExtra(AddScene.NOM);
            //new_scene is the name of the new scene to add to the ListView liste

            try {
                // The APPEND mode concatenates the file elements instead of replacing them
                outName = openFileOutput(LIGHTS, MODE_APPEND);
                outName.write(new_scene.getBytes());
                outName.write("\n".getBytes());
                //The name is added to the names file, followed by a line break
                if (outName != null)
                    outName.close();


                OutputStreamWriter outColor = new OutputStreamWriter(openFileOutput(COLORS, MODE_APPEND));
                //The current color of the light is cast into String and saved in test
                test = Integer.toString(Preferences.getColor());
                //then test is added in the configurations file, followed by a line break
                outColor.write(test + "\n");
                //Same for the brightness and saturation
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

        //Now, we read the files in order to save their items in the lists
        try {
            inName = openFileInput(LIGHTS);
            int value;
            while ((value = inName.read()) != -1) {
                StringBuffer lu = new StringBuffer();
                //A name is separated from another one by a line break.
                while ((value != 10)) {
                    lu.append((char) value);
                    value = inName.read();
                }
                nom = lu.toString();
                //The name is added in List_name
                List_name.add(nom);
            }

            if (inName != null)
                inName.close();

            InputStream inColor = openFileInput(COLORS);
            InputStreamReader inputreader = new InputStreamReader(inColor);
            BufferedReader buffreader = new BufferedReader(inputreader);

            String line;
            //Here we read line by line each number and add it in List_colors after casting it into Integer.
            while ((line = buffreader.readLine()) != null) {
                List_colors.add(Integer.parseInt(line));
            }
            inColor.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //We create the adapter, by precising that the layout is a list of single choice items.
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, List_name);

        //We set the adapter to the ListView liste
        liste.setAdapter(adapter);

        //We register the ListView to which the context menu will be associated
        //when the user will make a long press on an item, the floating menu will appear.
        registerForContextMenu(liste);


        //Here we see if one the scenes is applied to make its item checked by comparing the current light with the light settings saved in List_colors
        int i = 0;
        while(i<List_name.size()) {
            if (Preferences.getColor() == List_colors.get(3 * i) && Preferences.getBrightness() == List_colors.get(3 * i + 1) && Preferences.getSaturation() == List_colors.get(3 * i + 2)) {
                liste.setItemChecked(i, true);
                i = List_name.size();
            }
            else{
                i++;
            }
        }
        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //When the user clicks on an item, we apply its light settings by calling the applyLampsStates function
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Preferences.applyLampStates(List_colors.get(3 * position), List_colors.get(3 * position + 1), List_colors.get(3 * position + 2));
            }
        });
      }

    private void delete(int position){
        //In order to delete an item from the ListView, we need to remove its name from List_name,
        //remove its color, brightness and saturation from List_colors
        //and then remove them from the two files.
        try {
            List_name.remove(List_name.get(position));
            List_colors.remove(List_colors.get(3* position));
            List_colors.remove(List_colors.get(3*position ));
            List_colors.remove(List_colors.get(3* position));
            //We open the files on a private mode to only keep the first item
            //This first item will then be replaced if another writing is made
            FileOutputStream outName2 = openFileOutput(LIGHTS, MODE_PRIVATE);
            OutputStreamWriter outColor2 = new OutputStreamWriter(openFileOutput(COLORS, MODE_PRIVATE));
            String test2;
            //If List_name is null, we delete the item of the files by writing "".
            if(List_name.size()==0){
                outName2.write("".getBytes());
                outName2.close();
                outColor2.write("");
                outColor2.close();
            }
            else{
                //If the lists are not null, we copy the first items of the lists
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
                //Then, we open againthe files on MODE_APPEND in order to concatenate the items, and then we copy the lists.
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
        //Here we create a floating menu that will appear when the user makes a long click on an item.
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_apply_scene, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            //Here when the user clicks on the delete button, the delete function is called.
            case R.id.delete:
                delete((int)info.id);
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    //When the Back button of the Smartphone is pressed, the ApplyScene activity is closed
    //And the system goes back to the MainActivity.
    public void onBackPressed(){
        Intent intent = new Intent(ApplyScene.this, (MainActivity.class));
        startActivity(intent);
    }
}