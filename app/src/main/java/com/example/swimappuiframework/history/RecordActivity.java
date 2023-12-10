package com.example.swimappuiframework.history;

import static com.garmin.android.connectiq.IQApp.IQAppStatus.INSTALLED;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import java.util.Arrays;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.swimappuiframework.R;
import com.garmin.android.connectiq.ConnectIQ;
import com.garmin.android.connectiq.IQApp;
import com.garmin.android.connectiq.IQDevice;
import com.garmin.android.connectiq.exception.InvalidStateException;
import com.garmin.android.connectiq.exception.ServiceUnavailableException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecordActivity extends AppCompatActivity {

    ConnectIQ connectIQ;
    String filepath = "data.txt";
    String MY_APPLICATION_ID = "3e9897c2-5113-4f53-92df-f8c83ff08f67";
    ArrayList <IQDevice> devices = new ArrayList();
    ArrayList <IQApp> apps = new ArrayList();
    Context context;
    private  double[][] X = new double[3][0];
    private  double[][] Y = new double[3][0];
    private  double[][] Z = new double[3][0];
    private  double[][] Roll = new double[3][0];
    private double[][] Pitch = new double[3][0];
    private double[][] Beat = new double[3][0];
    Button btnBack;
    Button btnR1;
    Button btnR2;
    Button btnR3;
    TextView connectionText;
    private int enabled = -1;
    private boolean[] writtenTo = {false, false, false};
    private boolean isConnected = false;
    private boolean registered = false;
    private boolean collectData = false;
    private boolean receiving = false;
    private int clearCnt = 0;
    private int linesWritten = 0;
    private int dataReceived = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        createFile(context, filepath);
        setContentView(R.layout.activity_record);
        btnBack = findViewById(R.id.btnBack);
        btnR1 = findViewById(R.id.btnR1);
        btnR2 = findViewById(R.id.btnR2);
        btnR3 = findViewById(R.id.btnR3);
        connectionText = findViewById(R.id.connectionText);
        connectionText.setText("Connection Status: Not Connected");
        /*
        connectButton = findViewById(R.id.connectButton);
        collectDataButton = findViewById(R.id.collectDataButton);
        clearDataButton = findViewById(R.id.clearDataButton);
        vibeButton = findViewById(R.id.vibeButton);
        dataCounter = findViewById(R.id.dataCounter);
        */

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnR1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(enabled == 0){
                    if(!receiving && X[0].length > 0){
                        //Only disable if it has finished receiving data
                        btnR1.setText("First 50 Data Collected");
                        enabled = -1;
                        writtenTo[0] = true;
                    }
                } else if(writtenTo[0]) {
                    //Do nothing, maybe change text
                } else if(enabled == 1 || enabled == 2){
                    //Do nothing, maybe flash warning
                } else{
                    btnR1.setText("Collection Enabled");
                    enabled = 0;
                }
            }
        });

        btnR2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(enabled == 1){
                    if(!receiving && X[1].length > 0){
                        //Only disable if it has finished receiving
                        btnR2.setText("Second 50 Data Collected");
                        System.out.println(X[1]);
                        enabled = -1;
                        writtenTo[1] = true;
                    }
                } else if(writtenTo[1]) {
                    //Do nothing, maybe change text
                } else if(enabled == 0 || enabled == 2){
                    //Do nothing, maybe flash warning
                } else{
                    btnR2.setText("Collection Enabled");
                    enabled = 1;
                }
            }
        });

        btnR3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(enabled == 2){
                    if(!receiving && X[2].length > 0){
                        //Only disable if it has finished receiving
                        btnR3.setText("Third 50 Data Collected");
                        System.out.println(X[2]);
                        enabled = -1;
                        writtenTo[2] = true;
                    }
                } else if(writtenTo[2]) {
                    //Do nothing, maybe change text
                } else if(enabled == 0 || enabled == 1){
                    //Do nothing, maybe flash warning
                } else{
                    btnR3.setText("Collection Enabled");
                    enabled = 2;
                }
            }
        });

        connectIQ = ConnectIQ.getInstance(this, ConnectIQ.IQConnectType.WIRELESS);
        connectIQ.initialize(context, true, new ConnectIQ.ConnectIQListener() {

            // Called when the SDK has been successfully initialized
            @Override
            public void onSdkReady() {
                // Do any post initialization setup.
                pairDevices();
            }

            // Called when the SDK has been shut down
            @Override
            public void onInitializeError(ConnectIQ.IQSdkErrorStatus iqSdkErrorStatus) {

            }

            // Called when initialization fails.
            @Override
            public void onSdkShutDown() {
                System.out.println("SDK Broken");
            }
        });

    }

    protected void onPause(){
        super.onPause();
        clearCnt = 0;
        dataReceived = 0;
        //updateDataCounter();
        //clearDataButton.setText("Clear Data");
    }

    /*
    protected void updateDataCounter(){
        dataCounter.setText("Registered: " + registered + "\nPackets Received: " + dataReceived + "\nLines Written To File: " + linesWritten+ "\nReceiving: " + receiving);
    }

    // Function to connect or disconnect devices
    public void onConnectButtonClick(View view) {

        if(sdkReady){
            if (isConnected) {
                // Disconnect from devices
                System.out.println("Disconnecting");
                unpairDevices();
                registered = false;
                updateDataCounter();
                connectButton.setText("Connect to Devices");
            } else {
                // Connect to devices
                System.out.println("Looking For Devices");
                pairDevices();
                connectButton.setText("Disconnect From Devices");
            }

            // Toggle connection state
            isConnected = !isConnected;
        }
    }

    public void onCollectDataButtonClick(View view) throws InvalidStateException, ServiceUnavailableException {

        if (collectData) {
            collectDataButton.setText("Collect Data");
        } else {
            collectDataButton.setText("Stop Collecting Data");
        }
        System.out.println(connectIQ.getDeviceStatus(devices.get(0)));

        // Toggle connection state
        collectData = !collectData;
    }

    public void onVibeButtonClick(View view) {
        if(devices.size() > 0){
            try {
                send(new ArrayList<Object>(Collections.singleton("Vibrate")), 0);
            } catch (InvalidStateException e) {
                throw new RuntimeException(e);
            } catch (ServiceUnavailableException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void onClearDataButtonClick(View view) {

        //We Really don't want to accidentally clear this :)
        if (clearCnt == 0) {
            clearDataButton.setText("Clear Data?");
            clearCnt++;
        } else if (clearCnt == 1){
            clearDataButton.setText("Clear Data??");
            clearCnt++;
        } else if (clearCnt == 2){
            clearDataButton.setText("Clear Data???");
            clearCnt++;
        }
        else if (clearCnt == 3){
            clearDataButton.setText("Clear Data????");
            clearCnt++;
        }
        else{
            clearDataButton.setText("Clear Data");
            clearCnt = 0;
            linesWritten = 0;
            updateDataCounter();
            clearFile(context, filepath);
        }
    }
    */

    // Function to unpair devices
    private void unpairDevices() {
        for(int i = 0; i < apps.size(); i++){
            try {
                connectIQ.unregisterForApplicationEvents(devices.get(i), apps.get(i));
            } catch (InvalidStateException e) {
                throw new RuntimeException(e);
            }
        }
        devices = new ArrayList<IQDevice>();
        apps = new ArrayList<IQApp>();
    }

    protected void send(ArrayList<Object> message, int deviceNum) throws InvalidStateException, ServiceUnavailableException {
        connectIQ.sendMessage(devices.get(deviceNum), apps.get(deviceNum), message, new ConnectIQ.IQSendMessageListener() {

            @Override
            public void onMessageStatus( IQDevice device, IQApp app, ConnectIQ.IQMessageStatus status ) {
                Toast.makeText( context, status.name(), Toast.LENGTH_LONG ).show();
                if (status != ConnectIQ.IQMessageStatus.SUCCESS) {
                    System.out.println("Failure");
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        for(int i = 0; i < devices.size(); i++){
            try {
                connectIQ.unregisterForApplicationEvents(devices.get(i), apps.get(i));
            } catch (InvalidStateException e) {
                throw new RuntimeException(e);
            }
        }

    }

    protected void pairDevices(){
        try {
            List<IQDevice> paired = null;
            paired = connectIQ.getKnownDevices();

            if (paired != null && paired.size() > 0) {
                // get the status of the devices
                for (IQDevice device : paired) {
                    IQDevice.IQDeviceStatus status = null;
                    status = connectIQ.getDeviceStatus(device);
                    if (status == IQDevice.IQDeviceStatus.CONNECTED) {
                        devices.add(device);
                        System.out.println("Device Added");
                        getApp(device);
                    }
                }
            }
        }catch (InvalidStateException e) {
            throw new RuntimeException(e);
        } catch (ServiceUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    protected void getApp(IQDevice device) throws InvalidStateException, ServiceUnavailableException {
        System.out.println("Looking for app");
        connectIQ.getApplicationInfo(MY_APPLICATION_ID, device, new ConnectIQ.IQApplicationInfoListener() {
            @Override
            public void onApplicationInfoReceived( IQApp app ) {
                System.out.println("info received");

                if (app != null) {
                    if (app.getStatus() == INSTALLED) {
                        //App is installed
                        apps.add(app);
                        System.out.println("App Found");
                        registerMessageReceiver(device, app);
                    }else{
                        System.out.println("App Not Found");
                    }
                }else{
                    System.out.println("App Null");
                }
            }
            @Override
            public void onApplicationNotInstalled( String applicationId ) {
                // Prompt user with information
                AlertDialog.Builder dialog = new AlertDialog.Builder( context );
                dialog.setTitle( "Missing Application" );
                System.out.println("App Not Found");
                dialog.setMessage( "Corresponding IQ application not installed" );
                dialog.setPositiveButton( android.R.string.ok, null ); dialog.create().show();
            }
        });
    }

    protected void registerMessageReceiver(IQDevice device, IQApp app){
        // Register to receive messages from our application
        try {
            connectIQ.registerForAppEvents(device, app, new ConnectIQ.IQApplicationEventListener() {
                @Override
                public void onMessageReceived(IQDevice device, IQApp app, List<Object> messageData, ConnectIQ.IQMessageStatus status) {
                    // First inspect the status to make sure this
                    // was a SUCCESS. If not then the status will indicate why there
                    // was an issue receiving the message from the Connect IQ application.
                    System.out.println("Received");
                    receiving = true;
                    //updateDataCounter();
                    if (status == ConnectIQ.IQMessageStatus.SUCCESS) {
                        try {
                            send(new ArrayList<Object>(Collections.singleton("Received")), 0);
                        } catch (InvalidStateException e) {
                            throw new RuntimeException(e);
                        } catch (ServiceUnavailableException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println(messageData);
                        // Handle the message.
                        Object data = messageData.get(0);
                        String msg = null;
                        if(data instanceof ArrayList<?>){
                            if(((ArrayList<?>) data).get(0) instanceof ArrayList<?>){
                                convertArrayListToDoubleArrays((ArrayList<ArrayList<?>>) (ArrayList<?>) data);
                                dataReceived++;
                                //updateDataCounter();
                                if(collectData && msg != null){
                                    writeToFile(context, filepath, msg);
                                    linesWritten++;
                                    //updateDataCounter();
                                }
                            }
                            else{
                                receiving = false;
                                //updateDataCounter();
                            }
                        } else{
                            receiving = false;
                            //updateDataCounter();
                        }
                    }
                }
            });
            System.out.println("Registered");
            registered = true;
            connectionText.setText("Connection Status: Connected");
        } catch (InvalidStateException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeToFile(Context context, String fileName, String data) {
        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(fileName, Context.MODE_APPEND);
            data = data + "\n";
            fos.write(data.getBytes());
            //System.out.println("Success");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void clearFile(Context context, String fileName) {
        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            String data = "";
            fos.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public static File createFile(Context context, String fileName) {
        File file = new File(context.getFilesDir(), fileName);

        try {
            if (file.createNewFile()) {
                // File created successfully
                return file;
            } else {
                // File already exists
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void convertArrayListToDoubleArrays(ArrayList<ArrayList<?>> arrayList) {
        try {
            if(enabled >= 0){
                if (arrayList.size() >= 5) {
                    X[enabled] = concatenateArrays(X[enabled], convertToDoubleArray(arrayList.get(0)));
                    Y[enabled] = concatenateArrays(Y[enabled], convertToDoubleArray(arrayList.get(1)));
                    Z[enabled] = concatenateArrays(Z[enabled], convertToDoubleArray(arrayList.get(2)));
                    Roll[enabled] = concatenateArrays(Roll[enabled], convertToDoubleArray(arrayList.get(3)));
                    Pitch[enabled] = concatenateArrays(Pitch[enabled], convertToDoubleArray(arrayList.get(4)));
                }

                // Add the sixth element (Beat) with varying length
                if (arrayList.size() >= 6) {
                    Beat[enabled] = concatenateArrays(Beat[enabled], convertToDoubleArray(arrayList.get(5)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception if needed
        }
    }

    private static double[] convertToDoubleArray(ArrayList<?> list) {
        double[] result = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = ((Number) list.get(i)).doubleValue();
        }
        return result;
    }

    private static double[] concatenateArrays(double[] array1, double[] array2) {
        double[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

}