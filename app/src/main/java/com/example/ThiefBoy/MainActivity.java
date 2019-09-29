package com.example.ThiefBoy;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ThiefBoy.EMail.Var;
import com.example.ThiefBoy.Notif.Noti_fication;
import com.example.ThiefBoy.Service.for_service;
import com.example.myapplication.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {

    int atm = 0, out = 0, in = 0;
    ;
    private Button lock, enable, dis, setbt;
    private TextView txv, txv1, ownmail;
    private EditText Ed;
    private DevicePolicyManager devicepolicymanager;
    private ComponentName componantname;
    static final int RESULT_ENABLE = 11;
    private ActivityManager activitymanger;
    protected Context context;

    protected LocationManager locationManager;
    protected Location location;

    private static final int MY_LOCATION_PERMISSION_CODE = 1000;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;


    //    double wayLatitude = 0.0;
//    double wayLongitude = 0.0;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        devicepolicymanager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        lock = (Button) findViewById(R.id.lock);
        enable = (Button) findViewById(R.id.En);
        dis = (Button) findViewById(R.id.DIS);
        txv = (TextView) findViewById(R.id.tv);
        txv1 = (TextView) findViewById(R.id.tv1);
        Ed = findViewById(R.id.Ed);
        ownmail = (TextView) findViewById(R.id.ownmail);
        setbt = (Button) findViewById(R.id.Setbt);
        componantname = new ComponentName(this, MyAdmin.class);
        activitymanger = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        setbt.setOnClickListener(this);
        lock.setOnClickListener(this);
        enable.setOnClickListener(this);
        dis.setOnClickListener(this);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

//      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
//      location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
//      System.out.println(location.getLatitude()+","+ location.getLongitude());
//
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        Var.lattitude = location.getLatitude();
                        Var.lontitude = location.getLongitude();

                        //setAddressData();

                        if (mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };
        getLocation();
        System.out.println(Var.lattitude);


        // notificationManager.notify(1, notification);


        Intent ser_intent = new Intent(this, for_service.class);
        startForegroundService(ser_intent);

        // txv1.setText("Latitude:" + Var.lattitude + "\nLongitude:" + Var.lontitude);

    }

    @Override
    protected void onStart() {
        super.onStart();
        txv.setText("Unlock Attempt " + atm + " times");
        ownmail.setText(Var.toEmail);
        if (Var.toEmail.contains("@")) {
            setbt.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onResume() {
        boolean isactive = devicepolicymanager.isAdminActive(componantname);
        dis.setVisibility(isactive ? View.VISIBLE : View.GONE);
        enable.setVisibility(isactive ? View.GONE : View.VISIBLE);

        super.onResume();
    }

    @Override
    protected void onStop() {

        super.onStop();
        MyAdmin.atm = 0;

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        atm = MyAdmin.atm;
        System.out.println("res " + atm);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.En:
                System.out.println("here");
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componantname);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "");
                startActivityForResult(intent, RESULT_ENABLE);
                break;

            case R.id.DIS:
                devicepolicymanager.removeActiveAdmin(componantname);
                dis.setVisibility(View.GONE);
                enable.setVisibility(View.VISIBLE);
                break;

            case R.id.lock:
                boolean active = devicepolicymanager.isAdminActive(componantname);
                if (active) {
                    devicepolicymanager.lockNow();

                } else {
                    Toast.makeText(this, "enable the activity device feature", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.Setbt:
                Var.toEmail = Ed.getText().toString();
                ownmail.setText(Var.toEmail);
                Ed.setText("");


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case RESULT_ENABLE:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(MainActivity.this, "Enabled", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(MainActivity.this, "problem", Toast.LENGTH_LONG).show();
                }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }


//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//
//        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
//           // Log.i("", "Dispath event power");
//            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//            sendBroadcast(closeDialog);
//            Toast.makeText(MainActivity.this,"Hoise",Toast.LENGTH_LONG).show();
//            return true;
//        }
//
//
//        return super.dispatchKeyEvent(event);
//    }

    //********PowerBUTTON OFF*****/////
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if(!hasFocus) {
//            // Close every kind of system dialog
//            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//            sendBroadcast(closeDialog);
//        }
//    }

    @Override
    public void onLocationChanged(Location location) {
        getLocation();
        System.out.println(Var.lattitude);
        Intent ser_intent = new Intent(this, for_service.class);
        startForegroundService(ser_intent);
        // txv1.setText("Latitude:" + location.getLatitude() + "\nLongitude:" + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == MY_LOCATION_PERMISSION_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                    if (location != null) {
                        Var.lattitude = location.getLatitude();
                        Var.lontitude = location.getLongitude();
                        //setAddressData();
                        txv1.setText("Latitude:" + Var.lattitude + "\nLongitude:" + Var.lontitude);
                    } else {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    }
                });
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_LONG).show();
            }
        }
//
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_LOCATION_PERMISSION_CODE);

        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, (Location location) -> {
                out++;
                if (location != null) {
                    in++;
                    Var.lattitude = location.getLatitude();
                    Var.lontitude = location.getLongitude();
                    txv1.setText("Latitude:" + Var.lattitude + "\nLongitude:" + Var.lontitude);
                    //txtAddress = getView().findViewById(R.id.txtAddress);
                    //setAddressData();
                } else {
                    mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

                }
            });

        }

    }
}

