package com.freespirit.speedmeter.repositories;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.InputStream;
import java.util.UUID;

import androidx.lifecycle.MutableLiveData;

import com.freespirit.speedmeter.model.Speed;

public class SpeedRepo {
    MutableLiveData<Speed> speedData = new MutableLiveData<>();
    private static SpeedRepo instance;
    private Speed speed;

    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket  btSocket  = null;
    private BluetoothAdapter btAdapter = null;
    private BluetoothDevice  hc05      = null;

    private InputStream inputStream;

    public static SpeedRepo getInstance() {
        if(instance == null) return instance = new SpeedRepo();
        return instance;
    }

    public MutableLiveData<Speed> getSpeed() {
        setSpeed("0.0");
        speedData.setValue(speed);
        return speedData;
    }
    private void setSpeed(String v) {
        speed = new Speed();
        speed.setSpeed(v);
        speedData.postValue(speed);
    }

    public void readHC05SpeedData() {
        while(true) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                inputStream = btSocket.getInputStream();
                inputStream.skip(inputStream.available());
                char b;
                String speed = "";
                while((b = (char) inputStream.read()) != '\n') {
                    System.out.print(b);
                    speed += b;
                }
                setSpeed(speed);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void initHC05Communication() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        hc05      = btAdapter.getRemoteDevice("00:21:13:01:26:EC");
        System.out.println("Devices: " + btAdapter.getBondedDevices());
        System.out.println("Device Name: " + hc05.getName());

        int attempts = 0;
        do {
            try {
                btSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
                System.out.println("Socket: " + btSocket);
                btSocket.connect(); attempts++;
                System.out.println("Socket Connection " + btSocket.isConnected());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }while(!btSocket.isConnected() && attempts < 5);
    }

    public void terminateHC05Communication() {
        try {
            btSocket.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
