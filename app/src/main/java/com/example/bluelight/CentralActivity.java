package com.example.bluelight;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

public class CentralActivity extends AppCompatActivity {
    private Button bt_scan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);
        bt_scan = findViewById(R.id.scan);
        bt_scan.setOnClickListener(new scanOnClickListner());
    }
    class scanOnClickListner implements View.OnClickListener{
        @Override
        public void onClick(View V){

        }
    }

}
