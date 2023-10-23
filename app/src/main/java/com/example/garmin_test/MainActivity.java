package com.example.garmin_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    //https://chat.openai.com/share/f158f07d-b43c-4710-883b-0ba49b434977

    BluetoothAdapter bluetoothAdapter;
    private static final int LOCATION_PERMISSION_CODE = 101;
    private static final int BLUETOOTH_ADMIN_PERMISSION_CODE = 102;
    private static final int BLUETOOTH_SCAN_PERMISSION_CODE = 103;
    private static final int REQUEST_ENABLE_BT = 104;
    private boolean locationPermission = false;
    private boolean bluetoothAdminPermission = false;
    private boolean bluetoothScanPermission = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Locations and Bluetooth permissions
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_CODE
            );
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_ADMIN
        ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    BLUETOOTH_ADMIN_PERMISSION_CODE
            );
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
        ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    BLUETOOTH_SCAN_PERMISSION_CODE
            );
        }

        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            // Handle this case
        }
        //Enabling Bluetooth
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermission = true;
            } else {
                locationPermission = false;
            }
        }
        if (requestCode == BLUETOOTH_ADMIN_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bluetoothAdminPermission = true;
            } else {
                bluetoothAdminPermission = false;
            }
        }

        if (requestCode == BLUETOOTH_SCAN_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bluetoothScanPermission = true;
            } else {
                bluetoothScanPermission = false;
            }
        }
    }

    //Enabling Bluetooth
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                // Bluetooth is enabled by the user
            } else if (resultCode == RESULT_CANCELED) {
                // User denied the request to enable Bluetooth, handle this accordingly
            }
        }
    }



}