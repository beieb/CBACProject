package com.example.cbacproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView circuit;
    private TextView winner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_page);
        Toolbar myToolbar = findViewById(R.id.mytoolbar);
        circuit = findViewById(R.id.circuit);
        winner = findViewById(R.id.winner);
        String[] s = new String[]{"hello", "world"};
        initmlatestRace(s);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("HOME");

    }

    private void initmlatestRace(String[] s){
        this.circuit.setText(s[0]);
        this.winner.setText(s[1]);

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
}