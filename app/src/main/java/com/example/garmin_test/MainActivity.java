package com.example.garmin_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //https://chat.openai.com/share/f158f07d-b43c-4710-883b-0ba49b434977

    BluetoothAdapter bluetoothAdapter;
    private static final int LOCATION_PERMISSION_CODE = 101;
    private static final int BLUETOOTH_ADMIN_PERMISSION_CODE = 102;
    private static final int BLUETOOTH_SCAN_PERMISSION_CODE = 103;
    private static final int BLUETOOTH_CONNECT_PERMISSION_CODE = 104;
    private static final int REQUEST_ENABLE_BT = 104;
    private boolean locationPermission = false;
    private boolean bluetoothAdminPermission = false;
    private boolean bluetoothScanPermission = false;
    private boolean bluetoothConnectPermission = false;
    private Button discoveryButton;
    private ListView deviceListView;
    private ArrayAdapter<String> deviceListAdapter;
    private boolean isDiscovering = false;
    private ArrayList<BluetoothDevice> connectableDevices = new ArrayList<BluetoothDevice>();
    private ArrayList<BluetoothDevice> deviceToConnect = new ArrayList<BluetoothDevice>();

    private final BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                if(ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED) {
                    // A new device has been discovered, get the device object
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Handle the discovered device, such as adding it to a list or displaying it in your app's UI
                    if(!connectableDevices.contains(device)){
                        if(device.getName() != null) {
                            connectableDevices.add(device);
                            System.out.println(device.getName());
                            deviceListAdapter.add(device.getName());
                        }
                    }
                }
            }
        }
    };


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
                Manifest.permission.BLUETOOTH_SCAN
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.BLUETOOTH_SCAN},
                    BLUETOOTH_SCAN_PERMISSION_CODE
            );
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                    BLUETOOTH_CONNECT_PERMISSION_CODE
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
        Context context = this;
        deviceListView = findViewById(R.id.deviceListView);
        discoveryButton = findViewById(R.id.discoveryButton);
        deviceListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        deviceListView.setAdapter(deviceListAdapter);
        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDeviceName = deviceListAdapter.getItem(position);
                // Handle the selected device (e.g., store it for connection)
                // You might also update the UI to show the selected device
                for(int i = 0; i < connectableDevices.size(); i++){
                    if(ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.BLUETOOTH_CONNECT
                    ) == PackageManager.PERMISSION_GRANTED) {
                        if (connectableDevices.get(i).getName() == selectedDeviceName) {
                            deviceToConnect.add(connectableDevices.get(i));
                        }
                    }
                }
            }
        });
        discoveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                if (isDiscovering) {
                    // Stop the discovery process
                    if(ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.BLUETOOTH_SCAN
                    ) == PackageManager.PERMISSION_GRANTED){
                        bluetoothAdapter.cancelDiscovery();
                    }
                    discoveryButton.setText("Start Discovery");
                    isDiscovering = false;
                } else {
                    // Start the discovery process
                    if(ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.BLUETOOTH_SCAN
                    ) == PackageManager.PERMISSION_GRANTED){
                        bluetoothAdapter.startDiscovery();
                    }
                    discoveryButton.setText("Stop Discovery");
                    isDiscovering = true;
                }
            }
        });


        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(discoveryReceiver, filter);

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

        if (requestCode == BLUETOOTH_CONNECT_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bluetoothConnectPermission = true;
            } else {
                bluetoothConnectPermission = false;
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(discoveryReceiver);
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