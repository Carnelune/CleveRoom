package com.mica.ar.cleveroom;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.philips.lighting.model.PHLight;


/**
 * List of lamps connected to the selected bridge
 */
public class ListLampAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    List<PHLight> allLights;

    class ListLampItem {
        private TextView lampName;
    }

    public ListLampAdapter(Context context, List<PHLight> allLights) {
        mInflater = LayoutInflater.from(context);
        this.allLights = allLights;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ListLampItem item ;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.select_lamp, null);

            item = new ListLampItem();
            item.lampName = (TextView) convertView.findViewById(R.id.lamp_name);

            convertView.setTag(item);
        } else {
            item = (ListLampItem) convertView.getTag();
        }
        PHLight light = allLights.get(position);
        item.lampName.setTextColor(Color.BLACK);
        item.lampName.setText(light.getName());

        return convertView;
    }

    public int getCount(){ return allLights.size() ;}

    public long getItemId(int position) {
        return 0;
    }

    public Object getItem(int position) {return allLights.get(position);  }

    public void updateData(List<PHLight> allLights) {
        this.allLights = allLights;
        notifyDataSetChanged();
    }
}
