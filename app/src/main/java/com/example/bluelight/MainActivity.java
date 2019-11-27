package com.example.bluelight;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button bt_central;
    private Button bt_peripheral;
    private Button bt_observer;
    private Button bt_transmitter;


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
    }

    class centralOnClickListner implements View.OnClickListener{
        @Override
        public void onClick(View V){

        }
    }
    class peripherlaOnClickListner implements View.OnClickListener{
        @Override
        public void onClick(View V){

        }
    }

}
