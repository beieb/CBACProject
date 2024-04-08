package com.example.cbacproject;

import static com.google.android.gms.tasks.Tasks.call;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DailyCatFact extends AppCompatActivity {
    private TextView et;
    private static String mypref = "mypref";
    private SharedPreferences sharePref;
    private static String DATE = "DateDailyCatFact";
    private static String DCF = "DailyCatFact";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_cat_fact);
        Toolbar myToolbar = findViewById(R.id.mytoolbar);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("DCF");

        Date now = new Date();
        DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);
        String dateFormat = dateFormatter.format(now);

        et = findViewById(R.id.Dailycatfact);

        if(dateFormat.equals(Get(DATE))){
            display(Get(DCF));
        } else {
            Save(DATE, dateFormat);
            call("https://catfact.ninja/fact");
        }
    }

    public void Save(String Name, String name){
        SharedPreferences sharedPreferences = getSharedPreferences(mypref,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Name, name);
        editor.apply();
    }

    public String Get(String Name){
        sharePref=getSharedPreferences(mypref, Context.MODE_PRIVATE);
        return sharePref.getString(Name,"Innexistant");
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
        Save(DCF, result.toString());
        return result.toString();
    }

    private void display(String toDisplay) {
        try {
            String end = "Erreur";
            TextView tw = findViewById(R.id.Dailycatfact);
            if (toDisplay.equals("Erreur ")) {
                tw.setText("Impossible d'obtenir un fait sur les chats pour le moment");
            } else {
                JSONObject root = new JSONObject(toDisplay);
                end = root.getString("fact");
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
            Intent intent = new Intent(DailyCatFact.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.cat){
            Intent intent = new Intent(DailyCatFact.this, DailyCatFact.class);
            startActivity(intent);
            finish();
            return true;
        }else if (item.getItemId() == R.id.map) {
            Intent intent = new Intent(DailyCatFact.this, RedirectMapActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.car){
            Intent intent = new Intent(DailyCatFact.this, ListCoursesActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return false;
    }
}