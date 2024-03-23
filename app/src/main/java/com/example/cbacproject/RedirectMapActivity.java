package com.example.cbacproject;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class RedirectMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String tag = "PermissionApplication";

    private int LOCATION_PERMISSION_CODE =1;
    private LocationRequest locationRequest;
    private List<circuit> list= new ArrayList<circuit>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirect_map);

        chooseActivity();


    }
    private void chooseActivity(){
        Log.d("createCircuit", String.valueOf(list.size()));
        Intent intent = null;

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(RedirectMapActivity.this, ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                //verifie que l'utilisateur a donnée la permission pour la localisation
                Log.d(tag, "you have already this permission");
                if (isGPSEnable()){
                    //verifie que l'utilisateur a le gps d'activé
                    Log.d(tag, "GPS is enable");
                    intent = new Intent(RedirectMapActivity.this, MapsActivity.class);


                }else {
                    Log.d(tag, "GPS is not enable");
                    turnOnGPS();
                    if (isGPSEnable()) {
                        //verifie que l'utilisateur a le gps d'activé
                        Log.d(tag, "GPS is enable");
                        intent = new Intent(RedirectMapActivity.this, MapsActivity.class);
                        

                    } else {
                        Log.d(tag, "GPS is not enable");
                        intent = new Intent(RedirectMapActivity.this, MapsActivityNoLoc.class);
                        
                    }
                }

            }else{
                Log.d(tag, "you don't have already this permission");
                requestLocPermission();
                if(ActivityCompat.checkSelfPermission(RedirectMapActivity.this, ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                    //verifie que l'utilisateur a donnée la permission pour la localisation
                    Log.d(tag, "you have already this permission");
                    if (isGPSEnable()) {
                        //verifie que l'utilisateur a le gps d'activé
                        Log.d(tag, "GPS is enable");
                        intent = new Intent(RedirectMapActivity.this, MapsActivity.class);
                        

                    } else {
                        Log.d(tag, "GPS is not enable");
                        turnOnGPS();
                        if (isGPSEnable()) {
                            //verifie que l'utilisateur a le gps d'activé
                            Log.d(tag, "GPS is enable");
                            intent = new Intent(RedirectMapActivity.this, MapsActivity.class);
                            

                        } else {
                            Log.d(tag, "GPS is not enable");
                            intent = new Intent(RedirectMapActivity.this, MapsActivityNoLoc.class);
                            
                        }
                    }
                }else{
                    intent = new Intent(RedirectMapActivity.this, MapsActivityNoLoc.class);
                    
                }
                

            }
        }
        startActivity(intent);
        finish();


    }
    private void requestLocPermission() {
        /**
         * demande la permission d'utilisation du gps
         */
        Log.d(tag, String.valueOf(ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)));
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)){
            Log.d(tag, "if request");

            new AlertDialog.Builder(this)
                    .setTitle("permission needed")
                    .setMessage("please let us use permission")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(tag, "requestPermissionButtonOk");

                            ActivityCompat.requestPermissions(RedirectMapActivity.this,new String[]{ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
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
                    Toast.makeText(RedirectMapActivity.this, "GPS is not tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(RedirectMapActivity.this, 2);
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

    }





}