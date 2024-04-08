package com.example.cbacproject;

import static java.lang.Double.parseDouble;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapsActivityNoLoc extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap gMap;
    FrameLayout map;
    private double lat;
    private double lon;
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

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        map = findViewById(R.id.map);

        SupportMapFragment mapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivityNoLoc.this);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TextView txt;
        if (item.getItemId() == R.id.home) {
            Intent intent = new Intent(MapsActivityNoLoc.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.cat){
            Intent intent = new Intent(MapsActivityNoLoc.this, DailyCatFact.class);
            startActivity(intent);
            finish();
            return true;
        }else if (item.getItemId() == R.id.map) {
            Intent intent = new Intent(MapsActivityNoLoc.this, RedirectMapActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.car){
            Intent intent = new Intent(MapsActivityNoLoc.this, ListCoursesActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /**
         * set up de la toolbar utilisant menu/toolbar
         */
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;
        go(this.getCurrentFocus());
    }

    private void definePoint(List<circuit> list){
        Get();
        if(lat!=0.0 & lon!=0.0){
            LatLng latLng = new LatLng(lat, lon);
            this.gMap.addMarker(new MarkerOptions().position(latLng).title("Me").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        }

        for(int j =0; j<list.size(); j++){
            String nom = list.get(j).getNom();
            String lat = list.get(j).getLat();
            String lon = list.get(j).getLon();
            LatLng latLng = new LatLng(parseDouble(lat), parseDouble(lon));

            this.gMap.addMarker(new MarkerOptions().position(latLng).title(nom).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

        }
    }
    public void Get(){
        sharePref=getSharedPreferences(mypref, Context.MODE_PRIVATE);
        if(sharePref.contains("Lat") & sharePref.contains("Lon")){
            lat = Double.parseDouble(sharePref.getString("Lat","-100000"));
            lon = Double.parseDouble(sharePref.getString("Lon", "-100000"));
        }
    }

    public void go(View view) {

        call("https://ergast.com/api/f1/circuits.json?limit=80");
    }
    public void call(String param){
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

