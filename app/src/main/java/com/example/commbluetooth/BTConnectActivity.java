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
import android.widget.*;

import java.util.ArrayList;

public class BTConnectActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ArrayList<BluetoothDevice> pairedDevices= new ArrayList<>();
    private ArrayAdapter NewDeviceAdapter;
    private BluetoothAdapter mAdapter;
    private ListView pairedDevicesListView, newDevicesListView;

        ArrayAdapter newDeviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btconnect);
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        newDeviceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        pairedDevicesListView = (ListView) findViewById(R.id.BTList);
        newDevicesListView=(ListView)findViewById(R.id.BTList);

        //-----------------------------------------------
        newDeviceAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        newDevicesListView.setAdapter(newDeviceAdapter);
        newDeviceAdapter.notifyDataSetChanged();
    }

    BroadcastReceiver bReceive = new BroadcastReceiver( ){
        @Override
        public void onReceive(Context context, Intent intent) {
            switch(intent.getAction()){
                case(BluetoothDevice.ACTION_FOUND):
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    newDeviceAdapter.add(device.getName());
                    pairedDevices.add(device);
                    NewDeviceAdapter.notifyDataSetChanged();
                    break;

                case(BluetoothAdapter.ACTION_DISCOVERY_FINISHED):
                    if (newDeviceAdapter.getCount()==0) {
                        Toast.makeText(BTConnectActivity.this, "No device found", Toast.LENGTH_SHORT).show();
                        newDeviceAdapter.add("No new device found");
                        newDeviceAdapter.notifyDataSetChanged();
                    }
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        setResult(0, getIntent());
    }
}
