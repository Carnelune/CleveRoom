package com.mica.ar.cleveroom;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.philips.lighting.hue.sdk.PHAccessPoint;

/**
 * List of bridges connected on the network
 * Bridges are identified by two addresses: MAC and IP
 */

public class AccessPointListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<PHAccessPoint> accessPoints;

    class BridgeListItem {
        private TextView bridgeIp;
        private TextView bridgeMac;
    }

    public AccessPointListAdapter(Context context, List<PHAccessPoint> accessPoints) {
        mInflater = LayoutInflater.from(context);
        this.accessPoints = accessPoints;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        BridgeListItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.select_bridge, null);

            item = new BridgeListItem();
            item.bridgeMac = (TextView) convertView.findViewById(R.id.bridge_mac);
            item.bridgeIp = (TextView) convertView.findViewById(R.id.bridge_ip);

            convertView.setTag(item);
        } else {
            item = (BridgeListItem) convertView.getTag();
        }
        PHAccessPoint accessPoint = accessPoints.get(position);
        item.bridgeIp.setTextColor(Color.BLACK);
        item.bridgeIp.setText(accessPoint.getIpAddress());
        item.bridgeMac.setTextColor(Color.DKGRAY);
        item.bridgeMac.setText(accessPoint.getMacAddress());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        return accessPoints.size();
    }

    @Override
    public Object getItem(int position) {
        return accessPoints.get(position);
    }

    public void updateData(List<PHAccessPoint> accessPoints) {
        this.accessPoints = accessPoints;
        notifyDataSetChanged();
    }
}