package com.example.micha.bigstylenotification;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create Intent
        Intent serviceIntent = new Intent(this, MyService.class);
        // Start service with intent
        this.startService(serviceIntent);
    }
}
