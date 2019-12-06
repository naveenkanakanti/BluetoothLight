package com.example.bluelight;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class CentralActivity extends AppCompatActivity {
    private String TAG = "CentralActivity";
    private Button bt_scan;
    private ListView lv_DeviceList;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private List<BluetoothDevice> deviceList;
    private DeviceListAdapter mDeviceList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);
        bt_scan = findViewById(R.id.scan);
        bt_scan.setOnClickListener(new scanOnClickListner());
        lv_DeviceList = findViewById(R.id.deviceList);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        mDeviceList = new DeviceListAdapter(this,R.layout.device_view,deviceList);
        deviceList = new ArrayList<BluetoothDevice>();
        lv_DeviceList.setAdapter(mDeviceList);
    }

    class scanOnClickListner implements View.OnClickListener{
        @Override
        public void onClick(View V){
            List<ScanFilter> bleScanFilter = new ArrayList<>();
            mBluetoothLeScanner.startScan(bleScanFilter , new ScanSettings.Builder().
                    setScanMode(ScanSettings.SCAN_MODE_BALANCED).build() ,mScanCallback);

        }
    }

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {

            Log.d(TAG,"scan result: " + result.getDevice().getAddress() + " rssi "
                    + result.getRssi() + " name " + result.getDevice().getName());
            deviceList.add(result.getDevice());
            updatelist();
        }
    };


    private void updatelist(){
        mDeviceList.updateList(deviceList);
    }

}
