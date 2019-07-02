package com.example.commbluetooth;

import android.Manifest;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.security.KeyStore;
import java.security.Permission;

import static android.os.Build.VERSION.SDK_INT;


public class MainActivity extends AppCompatActivity {

    final static int NO_ADAPTER = 0;
    final static int PERMISSIONS_GRANTED = 1;
    final static int USER_REQUEST = 2;

    final static int BT_ACTIVATION_REQUEST_CODE = 0;
    final static int BT_ACTIVATION = 0;
    private static final int BT_CONNECT_CODE = 42;

    public BluetoothAdapter mBluetoothAdapter;

    final static String[] PERMISSIONS = new String[]{Manifest.permission.BLUETOOTH,Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int PERMISSIONS_REQUEST_CODE = 12;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItem = item.getItemId();
        switch (menuItem){
            case R.id.bluetooth_id:
                switch ((BluetoothRight())){
                    case NO_ADAPTER:
                        Toast.makeText(this, "No Bluetooth adapter", Toast.LENGTH_LONG).show();
                        break;
                    case PERMISSIONS_GRANTED:
                        onBluetoothConfigRequest(true);
                        break;
                    //case USER_REQUEST:
                }
        }
        return true;
    }

   private void onBluetoothConfigRequest(boolean postActivationCall) {
        Intent BTactivation,BTConnect;

        if(!mBluetoothAdapter.isEnabled()){
            if(!postActivationCall){
                BTactivation= new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(BTactivation, BT_ACTIVATION_REQUEST_CODE);
            }
        } else {
            BTConnect = new Intent(  this, BTConnectActivity.class);
            startActivityForResult(BTConnect, BT_CONNECT_CODE);
        }

    }

    private int BluetoothRight() {
        if (BluetoothAdapter.getDefaultAdapter() == null) {
            return NO_ADAPTER;
        }
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (SDK_INT >= Build.VERSION_CODES.N){
            if (!checkMultiplePermissions(PERMISSIONS)) {
                requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE);
                return USER_REQUEST;
            }
        }
        return PERMISSIONS_GRANTED;
    }

    private boolean checkMultiplePermissions(String[] permissions) {
        if (permissions != null && permissions.length != 0){
            for (String permission : permissions){
                if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE){
            for (int result : grantResults){
                if (result == PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this, "Autorisation necessary", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            onBluetoothConfigRequest(false);
        }
    }
}



