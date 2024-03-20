package com.example.cbacproject;

import static com.google.android.gms.tasks.Tasks.call;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

public class DailyCatFact extends AppCompatActivity {
    private TextView et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_cat_fact);
        Toolbar myToolbar = findViewById(R.id.mytoolbar);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("DCF");

        et = findViewById(R.id.Dailycatfact);
        call("https://catfact.ninja/fact");
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
                        display(data);
                    }
                });
            }
        });
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

    private void display(String toDisplay) {
        try {
            String end = "Erreur";
            TextView tw = findViewById(R.id.Dailycatfact);
            if (toDisplay.equals("Erreur ")) {
                tw.setText(end);
            } else {
                JSONObject root = new JSONObject(toDisplay);
                Log.d("DailyCatFact", end);
                end = root.getString("fact");
                Log.d("DailyCatFact", end);
                tw.setText(end);
            }

        } catch(JSONException e){
            throw new RuntimeException(e);
        }

    }




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
            Log.d("CBAC", "home yes");
            Intent intent = new Intent(DailyCatFact.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.cat){
            Log.d("CBAC", "user yes");
            Intent intent = new Intent(DailyCatFact.this, DailyCatFact.class);
            startActivity(intent);
            finish();
            return true;
        }else if (item.getItemId() == R.id.map) {
            Log.d("CBAC", "map yes");
            Intent intent = new Intent(DailyCatFact.this, MapsFragment.class);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.car){
            Log.d("CBAC", "mountaineer yes");
            return true;
        }
        return false;
    }
}