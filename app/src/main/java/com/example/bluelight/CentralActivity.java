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


import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    private boolean mScan;
    private final ParcelUuid mOpServiceUUID = ParcelUuid.fromString(/*"3F1BA650-8F85-4C45-9673-833E6DE03E1B"*/"00000118-0000-1000-8000-00805f9b34fb");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);
        bt_scan = findViewById(R.id.scan);
        bt_scan.setOnClickListener(new scanOnClickListner());
        lv_DeviceList = findViewById(R.id.deviceList);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        deviceList = new ArrayList<BluetoothDevice>();
        mDeviceList = new DeviceListAdapter(this,R.layout.device_view,deviceList);
        lv_DeviceList.setAdapter(mDeviceList);
        lv_DeviceList.setOnItemClickListener(mItemClickListner);
        mScan = false;
    }

    class scanOnClickListner implements View.OnClickListener{
        @Override
        public void onClick(View V){
            if(mScan){
                Log.d(TAG, "stop Scanning");
                stopScanning();
                mScan = false;
                bt_scan.setText(R.string.start_scan);
            } else {
                Log.d(TAG, "start Scanning");
                deviceList.clear();
                updatelist();
                byte[] fiterdata = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
                byte[] fitermark = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
                List<ScanFilter> bleScanFilter = new ArrayList<>();
                bleScanFilter.add(new ScanFilter.Builder().setServiceUuid(mOpServiceUUID).build());
                mBluetoothLeScanner.startScan(bleScanFilter, new ScanSettings.Builder().
                        setScanMode(ScanSettings.SCAN_MODE_BALANCED).build(), mScanCallback);
                bt_scan.setText(R.string.stop_scan);
                mScan = true;
            }

        }
    }
    private void stopScanning(){
        mBluetoothLeScanner.stopScan(mScanCallback);
    }
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {

            Log.d(TAG,"scan result: " + result.getDevice().getAddress() + " rssi "
                    + result.getRssi() + " name " + result.getDevice().getName() + "UUID: Service DATA: " + toHexString(result.getScanRecord().getServiceData(mOpServiceUUID)));
            //if(result.getDevice().getName() != null){
                if(!deviceList.contains(result.getDevice())){
                    deviceList.add(result.getDevice());
                    updatelist();
                } else{
                    Log.d(TAG,"Device already added");
                }
            /*} else{
                Log.d(TAG,"Name is not available");
            }*/

        }
    };


    private void updatelist(){

        mDeviceList.updateList(deviceList);
    }

    AdapterView.OnItemClickListener mItemClickListner = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d(TAG ,"position: "+ position + "device:" + deviceList.get(position));
        }
    };

    @Override
    protected void onStop() {
        if(mBluetoothLeScanner != null) {
            mBluetoothLeScanner.stopScan(mScanCallback);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(mBluetoothLeScanner != null) {
            mBluetoothLeScanner.stopScan(mScanCallback);
        }
        deviceList.clear();
        super.onDestroy();
    }
    public String toHexString(byte[] data) {
        if (data == null) {
            return null;
        } else {
            int len = data.length;
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < len; i++) {
                sb.append("0x");
                if ((data[i] & 0xFF) < 16) {
                    sb.append("0" + Integer.toHexString(data[i] & 0xFF));
                } else {
                    sb.append(Integer.toHexString(data[i] & 0xFF));
                }
                sb.append(", ");
            }
            return sb.toString().toUpperCase();
        }
    }
}
