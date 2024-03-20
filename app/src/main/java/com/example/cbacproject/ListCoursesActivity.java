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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListCoursesActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_courses);
        call("https://ergast.com/api/f1/2024/results.json");
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
        Log.d("listdecourse", toDisplay);
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

        List<Course> courses = new ArrayList<>();

        JSONObject root = new JSONObject(toDisplay);
        JSONObject suite = root.getJSONObject("MRData");
        JSONObject raceTable = suite.getJSONObject("RaceTable");
        JSONArray race = root.getJSONArray("Races");
        for(int i=0; i<race.length();i++) {
            JSONObject Jname = race.getJSONObject(i);
            season = Jname.getString("season");
            round = Jname.getString("round");
            name = Jname.getString("raceName");
            JSONObject loc = Jname.getJSONObject("Location");
            locality = loc.getString("locality");
            country = loc.getString("country");
            JSONArray Results = Jname.getJSONArray("Results");
            JSONObject firsts = Results.getJSONObject(0);

            JSONObject driver = firsts.getJSONObject("Driver");
            first = driver.getString("givenName") + driver.getString("familyName");

            JSONObject construct = firsts.getJSONObject("Constructor");
            constructorFirst = construct.getString("name");

            JSONObject lap = firsts.getJSONObject("Time");
            firstTime = lap.getString("time");

            JSONObject seconds = Results.getJSONObject(1);

            driver = firsts.getJSONObject("Driver");
            second = driver.getString("givenName") + driver.getString("familyName");

            construct = firsts.getJSONObject("Constructor");
            constructorSecond = construct.getString("name");

            lap = firsts.getJSONObject("Time");

            secondTime = lap.getString("time");
            JSONObject thirds = Results.getJSONObject(2);

            driver = firsts.getJSONObject("Driver");
            third = driver.getString("givenName") + driver.getString("familyName");

            construct = firsts.getJSONObject("Constructor");
            constructorThird = construct.getString("name");

            lap = firsts.getJSONObject("Time");
            thirdTime = lap.getString("time");

            Course course = new Course(name, season, round, locality, country, first, firstTime, constructorFirst, second, secondTime, constructorSecond, third, thirdTime, constructorThird);
            courses.add(course);
        }
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