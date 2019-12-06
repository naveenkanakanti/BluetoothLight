package com.example.bluelight;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.List;

public class DeviceListAdapter extends BaseAdapter {
    private TextView tv_name;
    private TextView tv_add;
    private List<BluetoothDevice> device_list;
    private View v;
    private static  final String TAG = "DeviceListAdapter";
    private  LayoutInflater mLayoutInflater;
    private  int mresource;
    public DeviceListAdapter(Context context, int resource , List<BluetoothDevice> myList) {
        Log.d(TAG, "Constructor");
        device_list = myList;
        mresource = resource;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG,"Position: " + position + "Name :" + device_list.get(position));
        v = mLayoutInflater.inflate(R.layout.device_view,null);
        tv_name = v.findViewById(R.id.device_name);
        tv_add = v.findViewById(R.id.device_address);
       tv_name.setText(device_list.get(position).getName());
       tv_add.setText(device_list.get(position).getAddress());
       return v;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void updateList(List<BluetoothDevice> list) {
        device_list = list;
        Log.d(TAG, "size:" + device_list.size());
        notifyDataSetChanged();
    }
}
