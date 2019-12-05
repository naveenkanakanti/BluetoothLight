package com.example.bluelight;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class CentralActivity extends AppCompatActivity {
    private String TAG = "CentralActivity";
    private Button bt_scan;
    private LinearLayout ll_DeviceList;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);
        bt_scan = findViewById(R.id.scan);
        bt_scan.setOnClickListener(new scanOnClickListner());
        ll_DeviceList = findViewById(R.id.deviceList);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();

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

        }
    };

    private void updatelist(){

    }

}
