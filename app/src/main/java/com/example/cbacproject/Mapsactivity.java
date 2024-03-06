package com.example.cbacproject;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class Mapsactivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap gMap;
    FrameLayout map;
    private double lat;
    private double lon;
    private LocationCallback locationCallback;
    private String tag = "PermissionApplication";

    private int LOCATION_PERMISSION_CODE =1;
    private static final String permission = ACCESS_FINE_LOCATION;
    private LocationRequest locationRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapsactivity);

        locationRequest=LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(Mapsactivity.this, ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                Log.d(tag, "you have already this permission");
                if (isGPSEnable()){
                    Log.d(tag, "GPS is enable");
                    LocationServices.getFusedLocationProviderClient(Mapsactivity.this)
                                    .requestLocationUpdates(locationRequest, new LocationCallback() {
                                        @Override
                                        public void onLocationResult(@NonNull LocationResult locationResult) {
                                            super.onLocationResult(locationResult);

                                            LocationServices.getFusedLocationProviderClient(Mapsactivity.this)
                                                    .removeLocationUpdates(this);
                                            if (locationResult != null && locationResult.getLocations().size()>0){
                                                int index = locationResult.getLocations().size()-1;
                                                lat= locationResult.getLocations().get(index).getLatitude();
                                                lon= locationResult.getLocations().get(index).getLongitude();
                                                Log.d(tag, lat+" "+ lon);
                                                map = findViewById(R.id.map);

                                                SupportMapFragment mapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                                                mapFragment.getMapAsync(Mapsactivity.this);
                                            }
                                        }
                                    }, Looper.getMainLooper());
                    Log.d(tag, "fin if");


                }else {
                    Log.d(tag, "GPS is not enable");

                    turnOnGPS();
                }

            }else{
                Log.d(tag, "you don't have already this permission");
                requestLocPermission();
            }
        }


    }

    private void requestLocPermission() {
        Log.d(tag, String.valueOf(ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)));
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)){
            Log.d(tag, "if request");

            new AlertDialog.Builder(this)
                    .setTitle("permission needed")
                    .setMessage("yes")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(tag, "requestPermissionButtonOk");

                            ActivityCompat.requestPermissions(Mapsactivity.this,new String[]{ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
                            Log.d(tag, "requestPermissionButtonOkclick");

                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();


        } else{
            Log.d(tag, "else");

            ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(tag, "Permission GRANTED");
            } else {
                Log.d(tag, "Permission not GRANTED");
            }
        }
    }

    private void turnOnGPS() {

        Log.d(tag, "turnOnGPS");


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(Mapsactivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(Mapsactivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });
    }

    private boolean isGPSEnable(){
        LocationManager lM = null;
        boolean isEnable =false;
        if (lM == null){
            lM = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        }
        isEnable = lM.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnable;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;
        Log.d(tag, lat+" "+ lon);

        LatLng mapFr = new LatLng(lat, lon);
        this.gMap.addMarker(new MarkerOptions().position(mapFr).title("Marker in france"));
        this.gMap.moveCamera(CameraUpdateFactory.newLatLng(mapFr));
    }
}