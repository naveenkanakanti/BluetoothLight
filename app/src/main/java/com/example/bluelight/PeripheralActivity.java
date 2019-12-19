package com.example.bluelight;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.UUID;



public class PeripheralActivity extends AppCompatActivity {
    private Button bt_startAdvertise;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager mBluetoothManager;
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    private BluetoothGattServer mGattServer;
    private BluetoothGattService mGattService;
    private String TAG = "PeripheralActivity";
    private boolean mAdvertise;
    private final ParcelUuid mOpServiceUUID = ParcelUuid.fromString("00000118-0000-1000-8000-00805f9b34fb");
    private static final UUID SERVICE_UUID = UUID.fromString("00009999-0000-1000-8000-00805f9b34fb");
    private static final UUID READ_CHARACTERISTIC_UUID = UUID.fromString("00009955-0000-1000-8000-00805f9b34fb");
    private static final UUID WRITE_CHARACTERISTIC_UUID = UUID.fromString("00009956-0000-1000-8000-00805f9b34fb");
    private static final UUID DESCRIPTOR_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private int modelId = 0x0000000000002011;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peripheral);
        mBluetoothManager = (BluetoothManager) getSystemService(this.BLUETOOTH_SERVICE);
        bt_startAdvertise = findViewById(R.id.startAdvertisement);
        bt_startAdvertise.setOnClickListener(new startadvOnClickListner());
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        mGattServer = mBluetoothManager.openGattServer(this,mCallbacks);
        mAdvertise = false;
        // add the service
        mGattService = addPeripheralService();
    }

    class startadvOnClickListner implements View.OnClickListener{
        @Override
        public void onClick(View V){
            if(mAdvertise){
                stopAdvertise();
                bt_startAdvertise.setText(R.string.start_advertisement);
            } else {
                startAdvertise();
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
        // Remove the service
        super.onDestroy();
    }

    private void startAdvertise(){
        AdvertiseSettings.Builder mSettingsBuilder = new AdvertiseSettings.Builder();
        mSettingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED);
        mSettingsBuilder.setConnectable(true);
        mSettingsBuilder.setTimeout(0);
        AdvertiseSettings mSettings  = mSettingsBuilder.build();
        AdvertiseData.Builder mDataBuilder = new AdvertiseData.Builder();
        mDataBuilder.setIncludeDeviceName(true);
        mDataBuilder.setIncludeTxPowerLevel(true);
        ByteBuffer modelIdBytes = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(
                modelId);
        byte[] fastPairServiceData = Arrays.copyOfRange(modelIdBytes.array(), 0, 8);
        // mDataBuilder.addServiceData(mOpServiceUUID,fastPairServiceData);
        mDataBuilder.addServiceUuid(mOpServiceUUID);
        AdvertiseData mData = mDataBuilder.build();
        mBluetoothLeAdvertiser.startAdvertising(mSettings,mData,mAdvertiseCallback);
    }

    private BluetoothGattService addPeripheralService() {
        // Adding the GattPeripheralService
        BluetoothGattService service = new BluetoothGattService(SERVICE_UUID,BluetoothGattService.SERVICE_TYPE_PRIMARY);
        BluetoothGattCharacteristic writeChareteristic = new BluetoothGattCharacteristic(WRITE_CHARACTERISTIC_UUID,BluetoothGattDescriptor.PERMISSION_WRITE,BluetoothGattCharacteristic.PROPERTY_WRITE);
        BluetoothGattCharacteristic notifyCharacteristic = new BluetoothGattCharacteristic(READ_CHARACTERISTIC_UUID,BluetoothGattCharacteristic.PERMISSION_READ,BluetoothGattCharacteristic.PROPERTY_NOTIFY);
        BluetoothGattDescriptor descriptor = new BluetoothGattDescriptor(DESCRIPTOR_UUID,0x11);
        notifyCharacteristic.addDescriptor(descriptor);
        service.addCharacteristic(writeChareteristic);
        service.addCharacteristic(notifyCharacteristic);
        return service;
    }



    private void stopAdvertise() {
        Log.d(TAG,"Stop Advertise");
        mAdvertise = false;
        mBluetoothLeAdvertiser.stopAdvertising(mAdvertiseCallback);
    }

    private final BluetoothGattServerCallback mCallbacks = new BluetoothGattServerCallback() {
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            super.onConnectionStateChange(device, status, newState);
        }

        @Override
        public void onServiceAdded(int status, BluetoothGattService service) {
            super.onServiceAdded(status, service);
        }

        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
        }

        @Override
        public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor) {
            super.onDescriptorReadRequest(device, requestId, offset, descriptor);
        }

        @Override
        public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);
        }

        @Override
        public void onNotificationSent(BluetoothDevice device, int status) {
            super.onNotificationSent(device, status);
        }

        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
        }
    };
};

