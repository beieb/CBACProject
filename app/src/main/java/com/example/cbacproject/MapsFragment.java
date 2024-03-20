package com.example.cbacproject;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.PendingIntent.getActivity;

import static java.lang.Double.parseDouble;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
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
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.List;
import java.util.ArrayList;

public class MapsFragment extends AppCompatActivity  implements OnMapReadyCallback {

    GoogleMap gMap;
    FrameLayout map;
    private double lat;
    private double lon;
    private LocationCallback locationCallback;
    private String tag = "PermissionApplication";

    private int LOCATION_PERMISSION_CODE =1;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * initialisation de la page utilisant le layout activity_map
         * contenant la toolbar utilisant le modele menu/toolbar
         * Setup de la map ainsi que de l'optention de la localisation de l'utilisateur
         * ainsi que la gestion d'erreure si l'utilisateur ne donne pas la permission de localisation
         * ou n'active pas le gps
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar myToolbar = (Toolbar) this.findViewById(R.id.mytoolbar2);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("Map");

        locationRequest=LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(MapsFragment.this, ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                //verifie que l'utilisateur a donnée la permission pour la localisation
                Log.d(tag, "you have already this permission");
                if (isGPSEnable()){
                    //verifie que l'utilisateur a le gps d'activé
                    Log.d(tag, "GPS is enable");
                    LocationServices.getFusedLocationProviderClient(MapsFragment.this)
                                    .requestLocationUpdates(locationRequest, new LocationCallback() {
                                        //fait une request pour obtenir la localisation
                                        @Override
                                        public void onLocationResult(@NonNull LocationResult locationResult) {
                                            super.onLocationResult(locationResult);

                                            LocationServices.getFusedLocationProviderClient(MapsFragment.this)
                                                    .removeLocationUpdates(this);
                                            if (locationResult != null && locationResult.getLocations().size()>0){
                                                int index = locationResult.getLocations().size()-1;
                                                lat= locationResult.getLocations().get(index).getLatitude();
                                                lon= locationResult.getLocations().get(index).getLongitude();
                                                Log.d(tag, lat+" "+ lon);
                                                map = findViewById(R.id.map);

                                                SupportMapFragment mapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                                                mapFragment.getMapAsync(MapsFragment.this);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * mise en place des réponse en cas de click sur les boutons de la toolbar
         */
        TextView txt;
        if (item.getItemId() == R.id.home) {
            Log.d("CBAC", "home yes");
            Intent intent = new Intent(MapsFragment.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.cat){
            Log.d("CBAC", "user yes");
            Intent intent = new Intent(MapsFragment.this, DailyCatFact.class);
            startActivity(intent);
            finish();
            return true;
        }else if (item.getItemId() == R.id.map) {
            Log.d("CBAC", "map yes");
            Intent intent = new Intent(MapsFragment.this, RedirectMapActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.car){
            Log.d("CBAC", "mountaineer yes");
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        /**
         * set up de la toolbar utilisant menu/toolbar
         */
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
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

                            ActivityCompat.requestPermissions(MapsFragment.this,new String[]{ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
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
                    Toast.makeText(MapsFragment.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(MapsFragment.this, 2);
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
        this.gMap.addMarker(new MarkerOptions().position(mapFr).title("ME").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        this.gMap.moveCamera(CameraUpdateFactory.newLatLng(mapFr));
        List list=new ArrayList<String[]>();
        list.add(new String[]{"Albert Park Grand Prix Circuit", "-37.8497", "144.968"});
        list.add(new String[]{"Bahrain International Circuit", "26.0325", "50.5106"});

        definePoint(list);

    }

    private void definePoint(List list){
        for(int j =0; j<list.size(); j++){
            String[] s = (String[]) list.get(j);
            LatLng map = new LatLng(parseDouble(s[1]), parseDouble(s[2]));
            this.gMap.addMarker(new MarkerOptions().position(map).title(s[0]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

        }
    }

}