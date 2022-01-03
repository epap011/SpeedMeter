package com.freespirit.speedmeter.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.freespirit.speedmeter.model.Speed;
import com.freespirit.speedmeter.repositories.SpeedRepo;

public class SpeedViewModel extends ViewModel {
    private MutableLiveData<Speed> speedData;
    private MutableLiveData<Boolean> bluetoothConnection;
    private SpeedRepo speedRepo;

    public void init() {
        if(speedData != null) return;
        speedRepo = SpeedRepo.getInstance();
        speedRepo.initHC05Communication();

        Runnable readingHC05 = new Runnable() {
            @Override
            public void run() {
                    speedRepo.readHC05SpeedData();
            }
        };
        Thread th1 = new Thread(readingHC05);
        th1.start();

        speedData = speedRepo.getSpeed();
    }

    public LiveData<Speed> getSpeedData() {
        if(speedData == null) speedData = new MutableLiveData<>();
        return speedData;
    }

    public LiveData<Boolean> isBluetoothConnectionEstablished() {
        return bluetoothConnection;
    }

}
