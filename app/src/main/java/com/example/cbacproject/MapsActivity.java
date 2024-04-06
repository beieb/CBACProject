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
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapsActivity extends AppCompatActivity  implements OnMapReadyCallback {

    GoogleMap gMap;
    FrameLayout map;
    private double lat;
    private double lon;
    private LocationCallback locationCallback;
    private String tag = "PermissionApplication";

    private int LOCATION_PERMISSION_CODE =1;
    private LocationRequest locationRequest;
    private List<circuit> listCircuit;
    private SharedPreferences sharePref;
    public static final String mypref = "mypref";



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

        listCircuit = new ArrayList<circuit>();

        locationRequest=LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(MapsActivity.this, ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                //verifie que l'utilisateur a donnée la permission pour la localisation
                Log.d(tag, "you have already this permission");
                if (isGPSEnable()){
                    //verifie que l'utilisateur a le gps d'activé
                    Log.d(tag, "GPS is enable");
                    LocationServices.getFusedLocationProviderClient(MapsActivity.this)
                                    .requestLocationUpdates(locationRequest, new LocationCallback() {
                                        //fait une request pour obtenir la localisation
                                        @Override
                                        public void onLocationResult(@NonNull LocationResult locationResult) {
                                            super.onLocationResult(locationResult);

                                            LocationServices.getFusedLocationProviderClient(MapsActivity.this)
                                                    .removeLocationUpdates(this);
                                            if (locationResult != null && locationResult.getLocations().size()>0){
                                                int index = locationResult.getLocations().size()-1;
                                                lat= locationResult.getLocations().get(index).getLatitude();
                                                lon= locationResult.getLocations().get(index).getLongitude();
                                                map = findViewById(R.id.map);

                                                SupportMapFragment mapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                                                mapFragment.getMapAsync(MapsActivity.this);
                                            }
                                        }
                                    }, Looper.getMainLooper());
                    Log.d(tag, "fin if");


                }else {
                    Log.d(tag, "GPS is not enable");
                    Intent intent = new Intent(MapsActivity.this, RedirectMapActivity.class);
                    startActivity(intent);
                    finish();
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
            Intent intent = new Intent(MapsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.cat){
            Log.d("CBAC", "user yes");
            Intent intent = new Intent(MapsActivity.this, DailyCatFact.class);
            startActivity(intent);
            finish();
            return true;
        }else if (item.getItemId() == R.id.map) {
            Log.d("CBAC", "map yes");
            Intent intent = new Intent(MapsActivity.this, RedirectMapActivity.class);
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

                            ActivityCompat.requestPermissions(MapsActivity.this,new String[]{ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
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
        Save();
        LatLng mapFr = new LatLng(lat, lon);
        this.gMap.addMarker(new MarkerOptions().position(mapFr).title("ME").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        this.gMap.moveCamera(CameraUpdateFactory.newLatLng(mapFr));

        go(this.getCurrentFocus());



    }
    public void Save(){
        SharedPreferences sharedPreferences = getSharedPreferences(mypref,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.d("MapsLocGet", String.valueOf(lon));
        editor.putString("Lat", String.valueOf(lat));
        editor.putString("Lon", String.valueOf(lon));
        editor.apply();
    }
    private void definePoint(List<circuit> list){
        for(int j =0; j<list.size(); j++){
            String nom = list.get(j).getNom();
            String lat = list.get(j).getLat();
            String lon = list.get(j).getLon();
            LatLng latLng = new LatLng(parseDouble(lat), parseDouble(lon));

            this.gMap.addMarker(new MarkerOptions().position(latLng).title(nom).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

        }
    }

    public void go(View view) {

        call("https://ergast.com/api/f1/circuits.json?limit=80");
    }
    public void call(String param){
        Log.d("apisMap", "-------------");

        ExecutorService ex= Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        ex.execute(new Runnable() {
            @Override
            public void run() {
                String data = getDataFromHTTP(param);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            display(data);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
    }

    private void display(String toDisplay) throws JSONException {
        if(toDisplay.equals("Erreur ")){
            Toast.makeText(this, "Impossible de trouver ou sont les circuits", Toast.LENGTH_LONG).show();
        }else {

            Log.d("createCircuit", toDisplay);
            JSONObject root = new JSONObject(toDisplay);

            JSONObject MRdata = root.getJSONObject("MRData");
            JSONObject CircuitTable = MRdata.getJSONObject("CircuitTable");
            JSONArray circuits = CircuitTable.getJSONArray("Circuits");

            String nom;
            String lat;
            String lon;

            for (int i = 0; i < circuits.length(); i++) {

                JSONObject circuit = circuits.getJSONObject(i);
                nom = circuit.getString("circuitName");
                JSONObject location = circuit.getJSONObject("Location");

                lat = location.getString("lat");
                lon = location.getString("long");
                circuit c = new circuit(nom, lat, lon);
                listCircuit.add(c);
            }
            definePoint(listCircuit);
        }


    }
    public String getDataFromHTTP(String param){
        StringBuilder result = new StringBuilder();
        HttpURLConnection connexion = null;
        try {
            URL url = new URL(param);
            connexion = (HttpURLConnection) url.openConnection();
            connexion.setRequestMethod("GET");
            InputStream inputStream = connexion.getInputStream();
            InputStreamReader inputStreamReader = new
                    InputStreamReader(inputStream);
            BufferedReader bf = new BufferedReader(inputStreamReader);
            String ligne = "";
            while ((ligne = bf.readLine()) != null) {
                result.append(ligne);
            }
            inputStream.close();
            bf.close();
            connexion.disconnect();
        } catch (Exception e) {
            result = new StringBuilder("Erreur ");
        }
        return result.toString();
    }

}