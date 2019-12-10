package com.example.bluelight;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;


import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PeripheralActivity extends AppCompatActivity {
    private Button bt_startAdvertise;
    private EditText tv_name;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    private String TAG = "PeripheralActivity";
    private boolean mAdvertise;
    private final ParcelUuid mOpServiceUUID = ParcelUuid.fromString("00000118-0000-1000-8000-00805f9b34fc");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peripheral);
        bt_startAdvertise = findViewById(R.id.startAdvertisement);
        tv_name = findViewById(R.id.peripheralName);
        bt_startAdvertise.setOnClickListener(new startadvOnClickListner());
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        /*tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_name.setText("");
            }
        });*/
        mAdvertise = false;
    }

    class startadvOnClickListner implements View.OnClickListener{
        @Override
        public void onClick(View V){
            if(mAdvertise){
                stopAdvertise();
                bt_startAdvertise.setText(R.string.start_advertisement);
            } else {
                String peripheralName = tv_name.getText().toString();
                startAdvertise(peripheralName);
            }

        }
    }

    AdvertiseCallback mAdvertiseCallback  = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            Log.d(TAG,"Advertising Success");
            mAdvertise = true;
            bt_startAdvertise.setText(R.string.stop_advertisement);
        }

        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            Log.d(TAG,"Advertising failed ,error code :"+ errorCode);
        }
    };

    @Override
    protected void onStop() {
        if(mBluetoothLeAdvertiser != null){
            mBluetoothLeAdvertiser.stopAdvertising(mAdvertiseCallback);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(mBluetoothLeAdvertiser != null){
            mBluetoothLeAdvertiser.stopAdvertising(mAdvertiseCallback);
        }
        super.onDestroy();
    }

    private void startAdvertise(String peripheralname){
        Log.d(TAG,"PeripheralName : " + peripheralname);
        AdvertiseSettings.Builder mSettingsBuilder = new AdvertiseSettings.Builder();
        mSettingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED);
        mSettingsBuilder.setConnectable(true);
        mSettingsBuilder.setTimeout(0);
        AdvertiseSettings mSettings  = mSettingsBuilder.build();
        AdvertiseData.Builder mDataBuilder = new AdvertiseData.Builder();
        mDataBuilder.setIncludeDeviceName(false);
        mDataBuilder.setIncludeTxPowerLevel(true);
        mDataBuilder.addServiceUuid(mOpServiceUUID);
        AdvertiseData mData = mDataBuilder.build();
        mBluetoothLeAdvertiser.startAdvertising(mSettings,mData,mAdvertiseCallback);
    }
    private void stopAdvertise() {
        mAdvertise = false;
        mBluetoothLeAdvertiser.stopAdvertising(mAdvertiseCallback);
    }
}