package com.example.cbacproject;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class RedirectMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int LOCATION_PERMISSION_CODE =1;
    private LocationRequest locationRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirect_map);

        chooseActivity();


    }
    private void sActivity(Intent intent){
        startActivity(intent);
        finish();
    }

    private void chooseActivity(){
        Intent intent = null;

        if (ActivityCompat.checkSelfPermission(RedirectMapActivity.this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //verifie que l'utilisateur a donnée la permission pour la localisation
            if (isGPSEnable()) {
                //verifie que l'utilisateur a le gps d'activé
                intent = new Intent(RedirectMapActivity.this, MapsActivity.class);
                sActivity(intent);

            } else {
                locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                turnOnGPS();
            }
        } else {
            requestLocPermission();
        }

    }
    private void requestLocPermission() {
        /**
         * demande la permission d'utilisation du gps
         */
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)){
            new AlertDialog.Builder(this)
                    .setTitle("permission needed")
                    .setMessage("please let us use permission")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(RedirectMapActivity.this,new String[]{ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(RedirectMapActivity.this, MapsActivityNoLoc.class);
                            sActivity(intent);
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
                if(isGPSEnable()){
                    Intent intent = new Intent(RedirectMapActivity.this, MapsActivity.class);
                    sActivity(intent);
                }else {
                    turnOnGPS();
                }
            } else {
                Intent intent = new Intent(RedirectMapActivity.this, MapsActivityNoLoc.class);
                sActivity(intent);
                //requestLocPermission();
            }
        }
    }

    private void turnOnGPS() {

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
                    Toast.makeText(RedirectMapActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RedirectMapActivity.this, MapsActivity.class);
                    sActivity(intent);
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
        boolean isEnable;
        lM = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        isEnable = lM.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnable;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                // Le GPS a été activé avec succès
                Toast.makeText(RedirectMapActivity.this, "GPS is turned on", Toast.LENGTH_SHORT).show();
                // Démarrer votre activité souhaitée ici
                Intent intent = new Intent(RedirectMapActivity.this, MapsActivity.class);
                sActivity(intent);
            } else {
                // L'utilisateur a annulé l'activation du GPS
                Toast.makeText(RedirectMapActivity.this, "User declined to turn on GPS", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RedirectMapActivity.this, MapsActivityNoLoc.class);
                sActivity(intent);
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }





}