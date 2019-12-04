package com.example.bluelight;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;


import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PeripheralActivity extends AppCompatActivity {
    private Button bt_startAdvertise;
    private TextView tv_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peripheral);
        bt_startAdvertise = findViewById(R.id.startAdvertisement);
        tv_name = findViewById(R.id.peripheralName);
        bt_startAdvertise.setOnClickListener(new startadvOnClickListner());
    }

    class startadvOnClickListner implements View.OnClickListener{
        @Override
        public void onClick(View V){

        }
    }

}