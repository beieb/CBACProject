package com.example.cbacproject;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static java.lang.Double.parseDouble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

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
import com.google.android.gms.location.LocationCallback;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.cbacproject.databinding.ActivityMapsNoLocBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class MapsActivityNoLoc extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap gMap;
    FrameLayout map;
    private double lat;
    private double lon;
    private LocationCallback locationCallback;
    private String tag = "PermissionApplication";

    private int LOCATION_PERMISSION_CODE = 1;
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
        /**
         * mise en place des réponse en cas de click sur les boutons de la toolbar
         */
        TextView txt;
        if (item.getItemId() == R.id.home) {
            Log.d("CBAC", "home yes");
            Intent intent = new Intent(MapsActivityNoLoc.this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.cat) {
            Log.d("CBAC", "user yes");
            Intent intent = new Intent(MapsActivityNoLoc.this, DailyCatFact.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.map) {
            Log.d("CBAC", "map yes");
            Intent intent = new Intent(MapsActivityNoLoc.this, RedirectMapActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.car) {
            Log.d("CBAC", "mountaineer yes");
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
        Log.d(tag, lat + " " + lon);

        List list = new ArrayList<String[]>();
        list.add(new String[]{"Albert Park Grand Prix Circuit", "-37.8497", "144.968"});
        list.add(new String[]{"Bahrain International Circuit", "26.0325", "50.5106"});

        definePoint(list);

    }

    private void definePoint(List list) {
        for (int j = 0; j < list.size(); j++) {
            String[] s = (String[]) list.get(j);
            LatLng map = new LatLng(parseDouble(s[1]), parseDouble(s[2]));
            this.gMap.addMarker(new MarkerOptions().position(map).title(s[0]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

        }
    }
}

