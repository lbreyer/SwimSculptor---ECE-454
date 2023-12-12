package com.example.swimappuiframework.workout;

import static com.garmin.android.connectiq.IQApp.IQAppStatus.INSTALLED;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swimappuiframework.MyApp;
import com.example.swimappuiframework.R;
import com.example.swimappuiframework.data.Workout;
import com.example.swimappuiframework.data.WorkoutItem;
import com.example.swimappuiframework.database.DatabaseViewModel;
import com.garmin.android.connectiq.ConnectIQ;
import com.garmin.android.connectiq.IQApp;
import com.garmin.android.connectiq.IQDevice;
import com.garmin.android.connectiq.exception.InvalidStateException;
import com.garmin.android.connectiq.exception.ServiceUnavailableException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ActiveWorkoutActivity extends AppCompatActivity {

    ConnectIQ connectIQ;

    String currentPace;
    int currentCnt;
    int currentItem;
    String filepath = "data.txt";
    String MY_APPLICATION_ID = "3e9897c2-5113-4f53-92df-f8c83ff08f67";
    ArrayList <IQDevice> devices = new ArrayList();
    ArrayList <IQApp> apps = new ArrayList();
    ArrayList <WorkoutItem> workoutItems = new ArrayList<>();
    Context context;
    ArrayList <double[][]> itemList = new ArrayList();
    Button btnBack;
    Button btnFinish;
    TextView connectionText;
    private int currentPackage = 0;
    Workout workout;
    DatabaseViewModel databaseViewModel;
    private boolean isConnected = false;
    private boolean registered = false;
    private boolean receiving = false;
    private int clearCnt = 0;
    private int linesWritten = 0;
    private int dataReceived = 0;

    private RecyclerView mRecyclerView;

    private ActiveWorkoutAdapter mAdapter;

    private List<List<Double>> corrValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        itemList.add(new double[6][0]);
        createFile(context, filepath);
        setContentView(R.layout.activity_active_workout);
        // Set up the recycler view in the popup
        databaseViewModel = ((MyApp) getApplication()).getWorkoutItemViewModel();

        btnBack = findViewById(R.id.btnBack);
        btnFinish = findViewById(R.id.btnFinish);
        connectionText = findViewById(R.id.connectionText);
        connectionText.setText("Connection Status: Not Connected");
        workout = (Workout) getIntent().getSerializableExtra("Workout");

        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new ActiveWorkoutAdapter(this, getSupportFragmentManager());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String pojo = workout.getPojoWorkoutItems();
        List<Integer> ints = convertStringToList(pojo);
        List<WorkoutItem> items = databaseViewModel.getAllWorkoutItems().getValue();

        corrValues = new ArrayList<>();

        for(Integer i : ints){
            WorkoutItem data = items.get(i - 1);
            workoutItems.add(data);
            mAdapter.mSelectedWorkoutItemList.add(data);
        }
        if(workoutItems.size() > 0){
            currentItem = 0;
            currentCnt = 0;
            currentPace = workoutItems.get(currentItem).getPace();
        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Add Finish stuff
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
                    //updateDataCounter();
                    if (status == ConnectIQ.IQMessageStatus.SUCCESS) {
                        if(!receiving){
                            itemList.add(new double[6][0]);
                        }
                        receiving = true;

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
                            }
                            else{
                                onAllDataReceived();
                            }
                        } else{
                            onAllDataReceived();
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

    public void onAllDataReceived() {
        currentCnt++;
        if(currentCnt >= workoutItems.get(currentItem).getCount()){
            currentItem++;
            if(currentItem < workoutItems.size()){
                //There are more workout items to go through
                currentPace = workoutItems.get(currentItem).getPace();
                currentCnt = 0;
            }
            else{
                //There are no more workout items
                //Put some stuff here if you want something special to happen
            }
        }
        currentPackage++; //next index for list
        receiving = false;
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
            if(currentPackage >= 0){
                if (arrayList.size() >= 5) {
                    itemList.get(currentPackage)[0] = concatenateArrays(itemList.get(currentPackage)[0], convertToDoubleArray(arrayList.get(0)));
                    itemList.get(currentPackage)[1] = concatenateArrays(itemList.get(currentPackage)[1], convertToDoubleArray(arrayList.get(1)));
                    itemList.get(currentPackage)[2] = concatenateArrays(itemList.get(currentPackage)[2], convertToDoubleArray(arrayList.get(2)));
                    itemList.get(currentPackage)[3] = concatenateArrays(itemList.get(currentPackage)[3], convertToDoubleArray(arrayList.get(3)));
                    itemList.get(currentPackage)[4] = concatenateArrays(itemList.get(currentPackage)[4], convertToDoubleArray(arrayList.get(4)));
                }

                // Add the sixth element (Beat) with varying length
                if (arrayList.size() >= 6) {
                    itemList.get(currentPackage)[5] = concatenateArrays(itemList.get(currentPackage)[5], convertToDoubleArray(arrayList.get(5)));
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

    public static List<Integer> convertStringToList(String input) {
        List<Integer> resultList = new ArrayList<>();

        // Split the string by spaces
        String[] numberStrings = input.split("\\s+");

        // Convert each substring to an integer and add to the list
        for (String numStr : numberStrings) {
            try {
                int number = Integer.parseInt(numStr);
                resultList.add(number);
            } catch (NumberFormatException e) {
                // Handle the case where a substring is not a valid integer
                System.err.println("Invalid number format: " + numStr);
            }
        }

        return resultList;
    }

}