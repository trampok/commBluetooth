package com.example.commbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Switch;

public class BTConnectActivity extends AppCompatActivity {

    private BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
        ArrayAdapter newDeviceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btconnect);
    }

    BroadcastReceiver bReceive = new BroadcastReceiver( ){
        @Override
        public void onReceive(Context context, Intent intent) {
            switch(intent.getAction()){
                case(BluetoothDevice.ACTION_FOUND):
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    newDeviceAdapter.add(device.getName());
                    break;

                case(BluetoothAdapter.ACTION_DISCOVERY_FINISHED):

                    break;

                default:
            }
        }
    };

    public void Discovery(View view){
        if (mAdapter.isDiscovering()) {
            mAdapter.cancelDiscovery();
        }
        else if(!mAdapter.isDiscovering()){
            IntentFilter disco = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(bReceive, disco);
            mAdapter.startDiscovery();

        }
    }
}
