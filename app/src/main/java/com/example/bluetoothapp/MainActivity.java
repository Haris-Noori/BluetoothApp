package com.example.bluetoothapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;

    TextView mStatusBlueTv;
    TextView mPairedTv;
    ImageView mBlueTv;
    Button mOnBtn, mOffBtn, mDiscoverBtn, mPairedBtn;

    BluetoothAdapter mBlueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStatusBlueTv = (TextView) findViewById(R.id.statusBluetoothTv);
        mPairedTv = (TextView) findViewById(R.id.pairedTv);
        mBlueTv = (ImageView) findViewById(R.id.bluetoothIv);
        mOnBtn = findViewById(R.id.onBtn);
        mOffBtn = findViewById(R.id.offBtn);
        mDiscoverBtn = findViewById(R.id.discoverableBtn);
        mPairedBtn = findViewById(R.id.pairedBtn);

        //adapter
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();

        // Check if bluetooth is available or not
        if(mBlueAdapter == null )
        {
            mStatusBlueTv.setText("Bluetooth is not available");
        }
        else
        {
            mStatusBlueTv.setText("Bluetooth is available");
        }

        // Set Image according to bluetooth status(on/off)
        if(mBlueAdapter.isEnabled())
        {
            mBlueTv.setImageResource(R.drawable.ic_bluetooth_on);
        }
        else
        {
            mBlueTv.setImageResource(R.drawable.ic_bluetooth_off);
        }

        // On Btn CLick
        mOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mBlueAdapter.isEnabled()){
                    showToast("Turning on Bluetooth...");
                    // Intent to On Bluetooth
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE_BT);
                }
                else{
                    showToast("Bluetooth is already on");
                }
            }
        });

        // Discover Bluetooth Btn
        mDiscoverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mBlueAdapter.isDiscovering()){
                    showToast("Making Your Device Discoverable");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent, REQUEST_DISCOVER_BT);
                }
            }
        });

        // Off Button Click
        mOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBlueAdapter.isEnabled()){
                    mBlueAdapter.disable();
                    showToast("Turning Bluetooth Off");
                    mBlueTv.setImageResource(R.drawable.ic_bluetooth_off);
                }
                else{
                    showToast("Bluetooth is already Off");
                }
            }
        });

        // Get Paired Devices Btn Click
        mPairedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBlueAdapter.isEnabled()){
                    mPairedTv.setText("Paired Devices");
                    Set<BluetoothDevice> devices = mBlueAdapter.getBondedDevices();
                    for (BluetoothDevice device: devices){
                        mPairedTv.append("\nDevice: " + device.getName() + "," + device);
                    }
                }
                else{
                    // Bluetooth is Off so can't get paired Devices
                    showToast("Turn on Bluetooth to get Paired Devices");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case REQUEST_ENABLE_BT:
            if (resultCode == RESULT_OK){
                // Bluetooth is On
                mBlueTv.setImageResource(R.drawable.ic_bluetooth_on);
                showToast("Bluetooth is on");
            }
            else{
                // User Denied to Turn Bluetooth On
                showToast("Couldn't on Bluetooth");
            }
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Toast Message Function
    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}