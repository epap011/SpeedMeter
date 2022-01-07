package com.freespirit.speedmeter.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.freespirit.speedmeter.R;
import com.freespirit.speedmeter.model.Speed;
import com.freespirit.speedmeter.viewmodel.SpeedViewModel;

public class MainActivity extends AppCompatActivity {
    private TextView speedTextView;
    private TextView statusTextView;
    private SpeedViewModel speedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speedTextView  = findViewById(R.id.speedTextView);
        statusTextView = findViewById(R.id.statusTextView);

        speedTextView.setTextColor(Color.parseColor("#36A899"));
        statusTextView.setTextColor(Color.parseColor("#1176C3"));

        statusTextView.setText("HC05 Connection: Established");

        speedViewModel = new ViewModelProvider(this).get(SpeedViewModel.class);
        speedViewModel.init();

        final Observer<Speed> speedObserver               = speed -> speedTextView.setText(speed.getSpeed() + " km/h");

        speedViewModel.getSpeedData().observe(this, speedObserver);
    }
}