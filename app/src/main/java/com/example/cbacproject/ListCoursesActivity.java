package com.example.cbacproject;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListCoursesActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_courses);
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

    private void display(String toDisplay) throws JSONException {

        String name;
        String season;
        String round;
        String locality;
        String country;
        String first;
        String firstTime;
        String constructorFirst;
        String second;
        String secondTime;
        String constructorSecond;
        String third;
        String thirdTime;
        String constructorThird;


        JSONObject root = new JSONObject(toDisplay);
        JSONObject suite = root.getJSONObject("MRData");
        JSONObject raceTable = suite.getJSONObject("RaceTable");
        season = raceTable.getString("season");
        JSONArray race = root.getJSONArray("Races");
        JSONObject Jname = race.getJSONObject(0);
        name = Jname.getString("raceName");
        JSONObject loc = Jname.getJSONObject("Location");
        locality = loc.getString("locality");
        country = loc.getString("country");
        JSONArray Results = Jname.getJSONArray("Results");
        JSONObject firsts = Results.getJSONObject(0);

        JSONObject driver = firsts.getJSONObject("Driver");
        first = driver.getString("givenName") + driver.getString("familyName");

        JSONObject construct =firsts.getJSONObject("Constructor");
        constructorFirst = construct.getString("name");

        JSONObject lap = firsts.getJSONObject("Time");
        firstTime = lap.getString("time");


    }

    public int searchSeason(View view) {
        EditText et = findViewById(R.id.year);
        TextView tw = findViewById(R.id.wrongSearch);
        String search = et.getText().toString();
        if (search.matches("-?\\d+")) {
            if (parseInt(search) > 2024 || parseInt(search) < 1950) {
                tw.setVisibility(View.VISIBLE);
            } else {
                return parseInt(search);
            }
        } else {
            tw.setVisibility(View.VISIBLE);
        }
        return 2024;


    }
}