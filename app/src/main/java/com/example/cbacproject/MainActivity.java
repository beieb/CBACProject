package com.example.cbacproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * page home activity
 */

public class MainActivity extends AppCompatActivity {

    private TextView circuit;

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *     init de la page
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * setup de la page initialisation de la home_page qui sert de base
         * initialisation de la toolbar pour la page home utilisant le modele menu/toolbar
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        Toolbar myToolbar = findViewById(R.id.mytoolbar);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("HOME");

        circuit = findViewById(R.id.circuit);

        WebView wv = findViewById(R.id.webview);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("https://www.fia.com/calendar");
        WebViewClient wvc = new WebViewClient();
        wv.setWebViewClient(wvc);
        go(this.getCurrentFocus());

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
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.cat){
            Intent intent = new Intent(MainActivity.this, DailyCatFact.class);
            startActivity(intent);
            finish();
            return true;
        }else if (item.getItemId() == R.id.map) {
            Intent intent = new Intent(MainActivity.this, RedirectMapActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.car){

            Intent intent = new Intent(MainActivity.this, ListCoursesActivity.class);
            startActivity(intent);
            finish();

            return true;
        }
        return false;
    }

    public void go(View view) {
        call("https://ergast.com/api/f1/current/last/results.json");

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
        if(toDisplay.equals("Erreur ") ||toDisplay.equals("Erreur")){
            this.circuit.setText("Impossible d'obtenir cette information");
        }else {
            String result = null;

            JSONObject root = new JSONObject(toDisplay);
            JSONObject MRData = root.getJSONObject("MRData");
            JSONObject RaceTable = MRData.getJSONObject("RaceTable");
            JSONArray Races = RaceTable.getJSONArray("Races");
            JSONObject race = Races.getJSONObject(0);
            JSONArray Results = race.getJSONArray("Results");
            JSONObject Drivers = Results.getJSONObject(0);
            JSONObject Driver = Drivers.getJSONObject("Driver");
            String givenName = Driver.getString("givenName");
            String familyName = Driver.getString("familyName");

            result = givenName + "\n" + familyName;

            this.circuit.setText(result);
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
    public void param(View view){
        Intent intent = new Intent(MainActivity.this, ParametreActivity.class);
        startActivity(intent);
    }
}