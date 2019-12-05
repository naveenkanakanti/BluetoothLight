package com.example.bluelight;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 2 ;
    private Button bt_central;
    private Button bt_peripheral;
    private Button bt_observer;
    private Button bt_transmitter;
    private static  final String TAG = "BluetoothLight";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_central = findViewById(R.id.bt_central);
        bt_peripheral = findViewById(R.id.bt_peripheral);
        bt_observer = findViewById(R.id.bt_observer);
        bt_transmitter = findViewById(R.id.bt_transmitter);
        bt_central.setOnClickListener( new centralOnClickListner());
        bt_peripheral.setOnClickListener(new peripherlaOnClickListner());
        IntentFilter i = new IntentFilter();
        i.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.registerReceiver(mReceiver,i);
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"Requesting BluetoothPermission");
        // Request Location Permission
        requestLocationPermission();
        bt_central.setEnabled(false);
        bt_peripheral.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mReceiver);
    }

    @Override
    protected void onActivityResult (int requestCode,
                                     int resultCode,
                                     Intent data) {

    }

    class centralOnClickListner implements View.OnClickListener{
        @Override
        public void onClick(View V){
            Intent intent = new Intent(bt_central.getContext(), CentralActivity.class);
            startActivity(intent);
        }
    }
    class peripherlaOnClickListner implements View.OnClickListener{
        @Override
        public void onClick(View V){
            Intent intent = new Intent(bt_peripheral.getContext(),PeripheralActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // Location-related task you need to do.
                    turnOnBluetooth(true);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Benstokes!!! Need Location permisssion", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

        } else {
            turnOnBluetooth(true);
        }
    }

    private void turnOnBluetooth(Boolean enable){
        // Turning on the Bluetooth
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        } else {
            if (enable) {
                if (!bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.enable();
                } else{
                    bt_central.setEnabled(true);
                    bt_peripheral.setEnabled(true);
                }
            } else {
                if (!bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.disable();
                }
            }
        }
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == BluetoothAdapter.ACTION_STATE_CHANGED) {
                if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.STATE_OFF) == BluetoothAdapter.STATE_ON) {
                    Toast.makeText(context,"Bluetooth Turned On",Toast.LENGTH_LONG).show();
                    bt_central.setEnabled(true);
                    bt_peripheral.setEnabled(true);
                }
            }
        }
    };
}
