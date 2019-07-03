package com.example.commbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothManager implements Transceiver {


    private FrameProcessor frameProcessor = null;
    private Transceiver listener = null;
    private UUID uuidSpp = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private bluetooth_state state;
    private ConnectedThread connectedThread;

    public enum bluetooth_state {
        STATE_NONE,
        STATE_CONNECTING,
        STATE_CONNECTED
    }

    public void attachFrameProcessor(FrameProcessor frameProcessor) {

    }
    public void detachFrameProcessor(FrameProcessor frameProcessor) {

    }



    public void setState(bluetooth_state state) {
        this.state = state;
        if(listener instanceof TransceiverEventListener)
        {
            ((TransceiverEventListener) listener).onTransceiverEvent();
        }
    }

    public BluetoothManager()
    {
        state = bluetooth_state.STATE_NONE;
    }

    public void connect(String pop)
    {
        startBtLinkManagement(pop);
    }
    private void startBtLinkManagement(String pop)
    {
        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(pop);
        ConnectThread cthread = new ConnectThread(device, this);
        cthread.start();
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket socket;
        private final BluetoothDevice device;
        private final BluetoothManager manager;

        public ConnectThread(BluetoothDevice bDevice, BluetoothManager manage)
        {
            BluetoothSocket tmp = null;
            manager = manage;
            device = bDevice;
            try
            {
                tmp = device.createRfcommSocketToServiceRecord(uuidSpp);
            } catch (IOException e) {
            }
            socket = tmp;

        }

        public void run()
        {
            android.bluetooth.BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
            manager.setState(bluetooth_state.STATE_CONNECTING);
            try {
                socket.connect();
            } catch (IOException e)
            {
                manager.setState(bluetooth_state.STATE_NONE);
                try {
                    socket.close();
                } catch (IOException closeException) {
                }
                return;

            }

            connectedThread = new ConnectedThread(socket, manager);
            connectedThread.start();
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
            }
            manager.setState(bluetooth_state.STATE_NONE);
        }

    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private final BluetoothManager manager;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket, BluetoothManager manage) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            manager = manage;

            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes;
            manager.setState(bluetooth_state.STATE_CONNECTED);
            while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);


                } catch (IOException e) {
                    manager.setState(bluetooth_state.STATE_NONE);
                    break;
                }
            }
        }

        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }
}
