package com.example.android.voltageapp;

import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
//    //BLE Connection
//    public ArrayList<BluetoothDevice> mLeDevices;
//    private BluetoothAdapter mBluetoothAdapter;
//    public BluetoothGatt mBluetoothGatt;
//    private BluetoothGattService service;
//    private BluetoothGattCharacteristic characteristic;
//    private BluetoothGattDescriptor descriptor;
//    public byte[] bytes;
//    //Control Variable
//    public boolean mScanning;
//
//    public String mMessage="No Data";
//    public BluetoothDevice myDevice;
//
//    private static final int REQUEST_ENABLE_BT = 1;
//    // Stops scanning after 10 seconds
//    private Handler mHandler;
//    private static final long SCAN_PERIOD = 10000;
//
//    //UI parameters needed
//    public Switch connect;
//    public Button show;
//    String display_message;
//    public ProgressBar loading_Indicator;
//
//    //LillyPAD parameters
//    public static final String MAC_ADDRESS = "F8:76:6C:D1:B2:1C";
//    private static final UUID UUID_SERVICE = UUID.fromString("0000fe84-0000-1000-8000-00805f9b34fb");
//    private static final UUID UUID_CHARACTERISTIC_READ = UUID.fromString("2d30c082-f39f-4ce6-923f-3484ea480596");
//    public static final UUID UUID_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
//    private static final String CHARACTERISTIC_STRING = "2d30c082-f39f-4ce6-923f-3484ea480596";


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        com.example.android.voltageapp.ConexionSQLiteHelper conn = new com.example.android.voltageapp.ConexionSQLiteHelper(this,
                "heartRate",null,1);
        SQLiteDatabase heartRate = conn.getWritableDatabase();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

//        //BLE Connection
//        mHandler = new Handler();
//        mLeDevices = new ArrayList<BluetoothDevice>();
//        myDevice = null;
//        //Call respective buttons and text views
//        connect = (Switch) findViewById(R.id.swConnect);
//        show = (Button) findViewById(R.id.show);
//        loading_Indicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
//
//        //Check if BLE is supported in the device
//        if (isBleSupported(this)){
//            //Get the Bluetooth service into the Bluetooth Manager
//            final BluetoothManager bluetoothManager =
//                    (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
//            mBluetoothAdapter = bluetoothManager.getAdapter();
//            //Check if the bluetooth adapter has been initialized and bluetooth is enabled
//            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()){
//                Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
//            }else{
//                Toast.makeText(this, "Bluetooth is currently enabled, please connect...", Toast.LENGTH_SHORT).show();
//            }
//        }else{
//            Toast.makeText(this, "BLE is not supported on this device, closing app...", Toast.LENGTH_SHORT).show();
//            closeApp();
//        }
    }

//    private boolean isBleSupported (Context context){
//        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
//    }
//
//    private void closeApp(){
//        Intent startMain = new Intent(Intent.ACTION_MAIN);
//        startMain.addCategory(Intent.CATEGORY_HOME);
//        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(startMain);
//        finish();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
//        // Check which request we're responding to just UI topics
//        if (requestCode == REQUEST_ENABLE_BT) {
//            if (resultCode == RESULT_OK) {
//                // If Bluetooth is enabled
//                Toast.makeText(this, "Bluetooth Enabled, please connect...", Toast.LENGTH_SHORT).show();
//            }
//            else {
//                // If Bluetooth wasn't enabled
//                Toast.makeText(this, "Bluetooth not enabled, closing app...", Toast.LENGTH_SHORT).show();
//                closeApp();
//            }
//        }else{
//            Toast.makeText(this, "Bluetooth is OFF, please turn ON", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public void ShowOnClick (View view){
//        String display_message = mMessage;
//        Intent i=new Intent(this, Tab1BLE.class);
//        i.putExtra("mMessage", display_message );
//        startActivity(i);
//    }
//    public void ConnectOnClick(View view){
//        // Check if bluetooth is enable in the phone
//        if(mBluetoothAdapter.isEnabled()) {
//            if(connect.isChecked()) {
//                Toast.makeText(this, "Connecting", Toast.LENGTH_SHORT).show();
//                showLoadingIndicator();
//                mLeDevices.clear();
//                myDevice = null;
//                scanLeDevice(true);
//            }else{
//                scanLeDevice(false);
////                display_message.setText("DATA");
//                String display_message = "DATA";
//                Intent i=new Intent(this,Tab1BLE.class);
//                i.putExtra("mMessage", display_message);
//                startActivity(i);
//                mBluetoothGatt.disconnect();
//            }
//        }else{
//            Toast.makeText(this, "Please, enable bluetooth on the device", Toast.LENGTH_SHORT).show();
//            connect.setChecked(false);
//        }
//    }
//
//    private void scanLeDevice(final boolean enable) {
//        if (enable) {
//            // Stops scanning after a pre-defined scan period.
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if(mScanning) {
//                        if (myDevice == null) {
//                            Toast.makeText(MainActivity.this, "Device not found", Toast.LENGTH_SHORT).show();
//                            connect.setChecked(false);
//                            hideLoadingIndicator();
//                        }
//                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                        mScanning = false;
//                    }
//                }
//            }, SCAN_PERIOD);
//            mScanning = true;
//            mBluetoothAdapter.startLeScan(mLeScanCallback);
//        }else{
//            hideLoadingIndicator();
//            mScanning = false;
//            mBluetoothAdapter.stopLeScan(mLeScanCallback);
//            myDevice = null;
//        }
//    }
//
//    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
//        @Override
//        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if(!mLeDevices.contains(device)) {
//                        mLeDevices.add(device);
//                    }
//                    if (device.getAddress().equals(MAC_ADDRESS)){
//                        myDevice = device;
//                        mBluetoothGatt = myDevice.connectGatt(MainActivity.this, false, mGattCallback);
//                        Toast.makeText(MainActivity.this, "Successfully Connected", Toast.LENGTH_SHORT).show();
//                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                        hideLoadingIndicator();
//                    }else{
//                        myDevice = null;
//                    }
//                }
//            });
//        }
//    };
//
//    public BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
//        @Override
//        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//            super.onConnectionStateChange(gatt, status, newState);
//            if (newState == STATE_CONNECTED) {
//                gatt.discoverServices();
//            }
//            if (status == BluetoothGatt.GATT_FAILURE) {
//                gatt.disconnect();
//                gatt.close();
//            }
//            if (newState == BluetoothProfile.STATE_DISCONNECTED) {
//                gatt.disconnect();
//                gatt.close();
//            }else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
//                gatt.disconnect();
//                gatt.close();
//            }
//        }
//
//        @Override
//        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//            super.onServicesDiscovered(gatt, status);
//            service = gatt.getService(UUID_SERVICE);
//            if (service != null) {
//                characteristic = service.getCharacteristic(UUID_CHARACTERISTIC_READ);
//                if (characteristic != null) {
//                    gatt.setCharacteristicNotification(characteristic, true);
//                    descriptor = characteristic.getDescriptor(UUID_DESCRIPTOR);
//                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                    gatt.writeDescriptor(descriptor);
//                    gatt.readCharacteristic(characteristic);
//                    bytes = characteristic.getValue();
//                }
//            }
//        }
//
//        @Override
//        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
//            super.onCharacteristicChanged(gatt, characteristic);
//            bytes = characteristic.getValue();
//            try {
//                mMessage = new String(bytes, "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//            super.onCharacteristicRead(gatt, characteristic, status);
//            gatt.readCharacteristic(characteristic);
//        }
//    };
//
//    public void showLoadingIndicator(){
//        loading_Indicator.setVisibility(View.VISIBLE);
//    }
//
//    public void hideLoadingIndicator(){
//        loading_Indicator.setVisibility(View.GONE);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        scanLeDevice(false);
//        mLeDevices.clear();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        scanLeDevice(false);
//        mLeDevices.clear();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        scanLeDevice(false);
//        mLeDevices.clear();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    Tab1BLE tab1 = new Tab1BLE();
                    return tab1;
                case 1:
                    Tab2Historicos tab2 = new Tab2Historicos();
                    return tab2;
                case 2:
                    Tab3Battery tab3 = new Tab3Battery();
                    return tab3;
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
        @Override
        public CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return "BLE";
                case 1:
                    return "Históricos";
                case 2:
                    return "Battery";
            }
            return null;
        }
    }


}
