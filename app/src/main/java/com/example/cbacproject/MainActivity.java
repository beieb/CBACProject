package com.example.cbacproject;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button showmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_page_no_connect);
        Toolbar myToolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("HOME");

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TextView txt;
        if (item.getItemId() == R.id.home) {
            Log.d("CBAC", "home yes");
            return true;
        } else if (item.getItemId() == R.id.user){
            Log.d("CBAC", "user yes");
            return true;
        }else if (item.getItemId() == R.id.map) {
            Log.d("CBAC", "map yes");
            Intent intent = new Intent(MainActivity.this, Mapsactivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.mountaineer){
            Log.d("CBAC", "mountaineer yes");
            return true;
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TextView txt;
        if (item.getItemId() == R.id.home) {
            Log.d("CBAC", "home yes");
            return true;
        } else if (item.getItemId() == R.id.user){
            Log.d("CBAC", "user yes");
            return true;
        }else if (item.getItemId() == R.id.map) {
            Log.d("CBAC", "map yes");
            return true;
        } else if (item.getItemId() == R.id.mountaineer){
            Log.d("CBAC", "mountaineer yes");
            return true;
        }
        return false;
    }

}