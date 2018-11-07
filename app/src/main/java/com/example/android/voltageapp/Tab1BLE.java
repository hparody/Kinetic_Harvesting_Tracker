package com.example.android.voltageapp;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.voltageapp.entidades.Utilidades.Utilidades;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.bluetooth.BluetoothAdapter.STATE_CONNECTED;
import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.pm.PackageManager.FEATURE_BLUETOOTH_LE;
import static android.support.v4.content.ContextCompat.getSystemService;
import static android.widget.Toast.makeText;
import static java.lang.Float.*;

public class Tab1BLE extends Fragment{
    ConexionSQLiteHelper conn;
    public ArrayList<BluetoothDevice> mLeDevices;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager bluetoothManager;
    public BluetoothGatt mBluetoothGatt;
    private BluetoothGattService service;
    private BluetoothGattCharacteristic characteristic;
    private BluetoothGattDescriptor descriptor;
    public byte[] bytes;

    // Control variables
    public boolean mScanning;
    public volatile boolean mConnected;
    public Toast connectingToast;
    public Toast deviceNotFoundToast;
    public Toast serviceNotFoundToast;
    public Toast connectionSuccessfulToast;
    public Toast deviceLostToast;

    public String mMessage;
    public Float mPower;
    public Float mVoltage;
    public BluetoothDevice myDevice;
    public SaveDatabase saveData;

    private static final int REQUEST_ENABLE_BT = 1;
    public static final String NO_DATA_MESSAGE = "No Data";
    public String TAG = "BATTERY_MONITOR";
        private static final int PERMISSION_REQUEST_COARSE_LOCATION = 456;
    public boolean LOCATION_PERMISSION;

    // Stops scanning after 5 seconds or when the device is found.
    private Handler mHandler;
    private static final long SCAN_PERIOD = 5000;

    // Handler to create new thread and show message
    public Handler showMessageHandler = new Handler();
    // Handler to execute delay at the beginning of the scan
    public final Handler delayHandler = new Handler();

    // UI parameters needed
    public Switch connect;
    public TextView display_message;
    public ProgressBar loading_Indicator;

    // Notification
    private static final int Notification_ID = 1;
    private static final int CONNECT_PENDING_INTENT_ID = 0;
    private static final int PENDING_INTENT_START_MAIN_ACTIVITY = 1;
    private static final int PENDING_INTENT_CLOSE_APP = 2;
    private static final String Notification_Channel_ID = "1";

    private static final String CLOSE_APP_ACTION = "CLOSE_APP_BROADCAST";
    private static final String OPEN_APP_ACTION = "OPEN_APP_BROADCAST";
    public String currentHearthRateLabel = "Current Voltage: ";
    public String currentHearthRate = "";
    public NotificationCompat.Builder mBuilder;
    public NotificationManager notificationManager;
    public Notification notification;

    // LilyPAD parameters
    public static final String MAC_ADDRESS_A = "F8:76:6C:D1:B2:1C"; // Accelerometer
    public static final String MAC_ADDRESS_H = "E8:3D:1C:A4:7C:A2"; // Heart Rate
    private static final UUID UUID_SERVICE = UUID.fromString("0000fe84-0000-1000-8000-00805f9b34fb");
    private static final UUID UUID_CHARACTERISTIC_READ = UUID.fromString("2d30c082-f39f-4ce6-923f-3484ea480596");
    public static final UUID UUID_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private static final String CHARACTERISTIC_STRING = "2d30c082-f39f-4ce6-923f-3484ea480596";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LOCATION_PERMISSION = false;
        mMessage = NO_DATA_MESSAGE;
        mPower = valueOf(0);
        mHandler = new Handler();
        mLeDevices = new ArrayList<BluetoothDevice>();
        myDevice = null;
        connectingToast = Toast.makeText(getActivity(), "Connecting", Toast.LENGTH_SHORT);
        deviceNotFoundToast = Toast.makeText(getActivity(), "Device not found", Toast.LENGTH_SHORT);
        serviceNotFoundToast = Toast.makeText(getActivity(), "Service not found on the remote " +
                "device, can not connect", Toast.LENGTH_SHORT);
        connectionSuccessfulToast =  makeText(getActivity(), "Successfully Connected", Toast.LENGTH_SHORT);
        deviceLostToast = makeText(getActivity(), "Device Lost. Connect again",Toast.LENGTH_SHORT);
        // Notification
        initHearthRateNotification(Tab1BLE.this.getActivity());

        // Check if BLE is supported in the device
        if (isBleSupported(Tab1BLE.this.getActivity())){
            // Get the bluetooth service into the Bluetooth Manager
            Log.w(TAG, "BLE Supported");
            getContext();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }else{
                LOCATION_PERMISSION = true;
                Log.w(TAG, "Location Permissions granted");
            }
            this.bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();
            // Check if the bluetooth adapter has been initialized and bluetooth is enabled
            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT); // startActivityForResult(activity to start, request code identifier);
            } else {
                Toast.makeText(getActivity(), "Bluetooth is currently enabled, please connect...", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "BLE is not supported on this device, closing app...", Toast.LENGTH_SHORT).show();
            closeApp();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1ble, container, false);
        // Call respective buttons and text views
        connect =  (Switch) rootView.findViewById(R.id.sw_Btn_Connect);
        display_message = (TextView) rootView.findViewById(R.id.tv_received_data);
        loading_Indicator = (ProgressBar) rootView.findViewById(R.id.pb_loading_indicator);
        Button fake_data = (Button) rootView.findViewById(R.id.btn_fake_data);
        fake_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarTramaFalsa();
            }
        });
        hideLoadingIndicator();
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectOnClick();
            }
        });
        return rootView;
    }

    private void ConnectOnClick() {
        // Check if bluetooth is enable in the phone
        if(mBluetoothAdapter.isEnabled()) {
            // Start after one second to avoid abrupt connections/disconnections -- ON PROCESS YET
            if(connect.isChecked()) {
                deviceNotFoundToast.cancel();serviceNotFoundToast.cancel();
                connectionSuccessfulToast.cancel(); deviceLostToast.cancel();
                connectingToast.show();
                showLoadingIndicator();
                myDevice = null;
                scanLeDevice(true);
            }else {
                if(mConnected){
                    mBluetoothGatt.disconnect();
                }
                mConnected = false;
                notificationManager.cancelAll();
                connectingToast.cancel();
                scanLeDevice(false);
                display_message.setText("Current Voltage");
            }
        }else{
            Toast.makeText(getActivity(), "Please, enable bluetooth on the device", Toast.LENGTH_SHORT).show();
            connect.setChecked(false);
        }
    }

    private boolean isBleSupported (Context context){
        return context.getPackageManager().hasSystemFeature(FEATURE_BLUETOOTH_LE);
    }

    private void closeApp() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        Tab1BLE.this.getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to just UI topics
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                // If Bluetooth is enabled
                Toast.makeText(getActivity(), "Bluetooth Enabled, please connect...", Toast.LENGTH_SHORT).show();
            }
            else {
                // If Bluetooth wasn't enabled
                Toast.makeText(getActivity(), "Bluetooth not enabled, closing app...", Toast.LENGTH_SHORT).show();
                closeApp();
            }
        }else{
            Toast.makeText(getActivity(), "Bluetooth is OFF, please turn ON", Toast.LENGTH_SHORT).show();
        }
    }

    public void showLoadingIndicator(){
        loading_Indicator.setVisibility(View.VISIBLE);
    }

    public void hideLoadingIndicator(){
        loading_Indicator.setVisibility(View.GONE);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(mScanning && !mConnected) {
                        if (myDevice == null) {
                            connectingToast.cancel(); connectionSuccessfulToast.cancel();
                            deviceNotFoundToast.show(); deviceLostToast.cancel();
                            connect.setChecked(false);
                            hideLoadingIndicator();
                            mConnected = false;
                            mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        }
                    }else {
                        if(myDevice != null) {
                            mBluetoothAdapter.stopLeScan(mLeScanCallback);
                            mScanning = false;
                            mConnected = true;
                        }
                    }
                }
            }, SCAN_PERIOD);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = true;
                    mConnected = false;
                    mBluetoothAdapter.startLeScan(mLeScanCallback);
                }
            }, 1500);

        }else{
            hideLoadingIndicator();
            mScanning = false;
            if(mConnected){
                mBluetoothGatt.disconnect();
            }
            mConnected = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            myDevice = null;
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            Tab1BLE.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!mLeDevices.contains(device)) {
                        mLeDevices.add(device);
                    }
                    if ((device.getAddress().equals(MAC_ADDRESS_A)) || (device.getAddress().equals(MAC_ADDRESS_H))){
                        myDevice = device;
                        mBluetoothGatt = myDevice.connectGatt(Tab1BLE.this.getActivity(), false, mGattCallback);
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        hideLoadingIndicator();
                        connectingToast.cancel();connectionSuccessfulToast.show();
                    }else{
                        myDevice = null;
                    }
                }
            });
        }
    };



    public void initHearthRateNotification(Context context){
        Intent intentStartMainActivity = new Intent();
        intentStartMainActivity.setAction(OPEN_APP_ACTION);
        PendingIntent pendingIntentStartMainActivity = PendingIntent.getBroadcast(Tab1BLE.this.getActivity(),PENDING_INTENT_START_MAIN_ACTIVITY,
                intentStartMainActivity,FLAG_UPDATE_CURRENT);

        notification = new Notification();
        notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification_Channel";
            NotificationChannel mChannel = new NotificationChannel(Notification_Channel_ID, name, NotificationManager.IMPORTANCE_HIGH);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(mChannel);
        }

        mBuilder = new NotificationCompat.Builder(Tab1BLE.this.getActivity(),Notification_Channel_ID)
                .setColor(ContextCompat.getColor(context,R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_ble)
                .setContentTitle("Kinetic Harvesting Tracker")
                .setContentText(currentHearthRateLabel)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
    }

    public BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == STATE_CONNECTED) {
                gatt.discoverServices();
                mConnected = true;
            }
            if (status == BluetoothGatt.GATT_FAILURE) {
                gatt.disconnect();
                gatt.close();
                gatt = null;
                mConnected = false;
            }
            if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                assert gatt != null;
                gatt.disconnect();
                gatt.close();
                mConnected = false;
            }else if (status != BluetoothGatt.GATT_SUCCESS) {
                assert gatt != null;
                gatt.disconnect();
                gatt.close();
                mConnected = false;
            }
            if(status == BluetoothGatt.GATT_SUCCESS){

            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            service = gatt.getService(UUID_SERVICE);
            Log.w(TAG, String.valueOf(service));
            if (service != null) {
                characteristic = service.getCharacteristic(UUID_CHARACTERISTIC_READ);
                Log.w(TAG, String.valueOf(characteristic));
                if (characteristic != null) {
                    mConnected = true;
                    gatt.setCharacteristicNotification(characteristic, true);
                    descriptor = characteristic.getDescriptor(UUID_DESCRIPTOR);
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    gatt.writeDescriptor(descriptor);
                    gatt.readCharacteristic(characteristic);
                    bytes = characteristic.getValue();
                    try {
                        Log.w(TAG, new String(bytes,"UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }else{
                    mConnected = false;
                }
            }else{
                mConnected = false;
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            bytes = characteristic.getValue();
            try {
                String result = new String(bytes, "UTF-8");
                Log.w(TAG, result);
                mMessage = result;
                saveData = new SaveDatabase();
                saveData.execute(mMessage);
                mConnected = true;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            gatt.readCharacteristic(characteristic);
        }
    };

    private class SaveDatabase extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... data) {
            String Message = data[0];
            currentHearthRate = currentHearthRateLabel + Message + " V";
            String messageToShow = Message + " V";
            showMessage(messageToShow);
            mVoltage = Float.parseFloat(Message);
            Double power =  Math.pow(Double.parseDouble(Message), 2)/0.474;
            mPower = Float.parseFloat(String.valueOf(power));
            registrarTramaBLE(mPower,mVoltage);
            triggerNotification(currentHearthRate);
            return null;
        }

        @Override
        protected void onPostExecute(String Message){
        }
    }

    public void showMessage(final String messageToShow) {
        showMessageHandler.post(new Runnable() {
            public void run(){
                display_message.setText(messageToShow);
            }
        });
    }

    public void triggerNotification(String hearthRate){
        mBuilder.setContentText(hearthRate);
        notification = mBuilder.build();
        notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
        notificationManager.notify(Notification_ID, notification);
    }

    private void registrarTramaBLE(Float power, Float voltage) {
        SQLiteDatabase db = new ConexionSQLiteHelper(getActivity(), Utilidades.TABLA_HEARTRATE, null, 1).getWritableDatabase();
        DateFormat df = new DateFormat();
        String C = (String) DateFormat.format("yyyy-MM-dd kk:mm:ss", new java.util.Date());
        String[] parts = C.split(" ");
        String fecha = parts[0];
        String hora = parts[1];
        String potencia = String.valueOf(power);
        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_TRAMA, voltage);
        values.put(Utilidades.CAMPO_POTENCIA, potencia);
        values.put(Utilidades.CAMPO_FECHA, fecha);
        values.put(Utilidades.CAMPO_HORA, hora);
        values.put(Utilidades.CAMPO_DATE_TIME, C);
        db.insert(Utilidades.TABLA_HEARTRATE, null, values);
        db.close();
    }

    private void registrarTramaFalsa() {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getContext(), Utilidades.TABLA_HEARTRATE, null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        DateFormat df = new DateFormat();
        String C = (String) DateFormat.format("yyyy-MM-dd kk:mm:ss", new java.util.Date());
        String[] parts = C.split(" ");
        String fecha = parts[0];
        String hora = parts[1];
        String horasinseparacion = hora.replace(":", "");
        String[] horadividida = hora.split(":");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(horadividida[0]);
        stringBuilder.append(horadividida[1]);
        String horasinsegundos = stringBuilder.toString();
        StringBuilder horadecimal = new StringBuilder();
        horadecimal.append("0.");
        horadecimal.append(horasinsegundos);
        String horadecimalnew = horadecimal.toString();
        String horamilli = String.valueOf(System.currentTimeMillis());
        Double Result = (Math.random()*((5.0)+1));
        Double potencia_d =  Math.pow(Result, 2)/0.474;
        String fakepotencia = String.valueOf(potencia_d);
        String Tramanew = String.valueOf(Result);
        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_TRAMA, Tramanew);
        values.put(Utilidades.CAMPO_POTENCIA, fakepotencia);
        values.put(Utilidades.CAMPO_FECHA, fecha);
        values.put(Utilidades.CAMPO_HORA, hora);
        values.put(Utilidades.CAMPO_DATE_TIME, C);
        db.insert(Utilidades.TABLA_HEARTRATE, null, values);
        db.close();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                Log.w(TAG, "Permissions: " + String.valueOf(grantResults[0]));
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.w(TAG, "Permissions Granted");
                    // Permission granted, yay! Start the Bluetooth device scan.
                } else {
                    Log.w(TAG, "Permissions Denied");
                    Toast.makeText(getActivity(), "Location Permissions required for the app. Closing app.", Toast.LENGTH_SHORT).show();
                    closeApp();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();scanLeDevice(false);
        mLeDevices.clear();
        notificationManager.cancelAll();
        if(mConnected) {
            mBluetoothGatt.disconnect();
        }
        saveData.cancel(true);
        mBluetoothAdapter.disable();
    }
}
