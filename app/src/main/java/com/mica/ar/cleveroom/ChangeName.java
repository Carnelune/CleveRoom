package com.mica.ar.cleveroom;


        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.view.View.OnClickListener;
        import android.widget.Toast;

        import com.philips.lighting.hue.listener.PHLightListener;
        import com.philips.lighting.hue.sdk.PHHueSDK;
        import com.philips.lighting.model.PHBridge;
        import com.philips.lighting.model.PHBridgeResource;
        import com.philips.lighting.model.PHHueError;
        import com.philips.lighting.model.PHLight;

        import java.util.List;
        import java.util.Map;

/**
 * Created by sarah on 15/06/2015.
 *
 */

public class ChangeName extends Activity {

    EditText name = null;
    Button ok = null;
    Button annuler = null;
    public static final String TAG = "CleveRoom";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupchangename);

        name = (EditText)findViewById(R.id.new_name);
        name.addTextChangedListener(textWatcher);

        ok = (Button)findViewById(R.id.ok);
        ok.setOnClickListener(okListener);

        annuler = (Button)findViewById(R.id.annuler);
        annuler.setOnClickListener(annulerListener);


    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private OnClickListener okListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String nom = name.getText().toString();
            if ((nom.equals("")) || nom.length()>32) {
                Toast.makeText(ChangeName.this, "Error name", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(ChangeName.this, "Nom modifie", Toast.LENGTH_SHORT).show();
                PHHueSDK phHueSDK;
                phHueSDK = PHHueSDK.create();
                PHBridge bridge = phHueSDK.getSelectedBridge();
                Preferences prefs = Preferences.getInstance(getApplicationContext());
                String lampe_id = prefs.getLightChose();

                List<PHLight> allLights = bridge.getResourceCache().getAllLights();

                for (PHLight light : allLights) {
                    if(light.getIdentifier().equals(lampe_id)) {
                       light.setName(nom);
                       bridge.updateLight(light,listener);
                    }
                }
                ChangeName.this.finish();
            }
        }
    };

    private OnClickListener annulerListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            Toast.makeText(ChangeName.this, "Operation annulee", Toast.LENGTH_SHORT).show();



            ChangeName.this.finish();


        }
    };

    PHLightListener listener = new PHLightListener() {

        @Override
        public void onSuccess() {
        }

        @Override
        public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {
            Log.w(TAG, "Light has updated");
        }

        @Override
        public void onError(int arg0, String arg1) {
        }

        @Override
        public void onReceivingLightDetails(PHLight arg0) {
        }

        @Override
        public void onReceivingLights(List<PHBridgeResource> arg0) {
        }

        @Override
        public void onSearchComplete() {
        }
    };

}
