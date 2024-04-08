package com.example.cbacproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class raceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);
        Toolbar myToolbar = findViewById(R.id.mytoolbar);
        TextView textView = findViewById(R.id.seasonrace);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle(textView.getText().toString());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        /**
         * initialisation Toolbar
         */
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TextView txt;
        if (item.getItemId() == R.id.home) {
            Intent intent = new Intent(raceActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.cat){
            Intent intent = new Intent(raceActivity.this, DailyCatFact.class);
            startActivity(intent);
            finish();
            return true;
        }else if (item.getItemId() == R.id.map) {
            Intent intent = new Intent(raceActivity.this, RedirectMapActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.car){
            Intent intent = new Intent(raceActivity.this, ListCoursesActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return false;
    }
}