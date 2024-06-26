package com.example.cbacproject;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class ListCoursesActivity extends AppCompatActivity {
    private EditText erreurAPI;
    private List<EditText> editTexts = new ArrayList<EditText>();

    private List<Course> courses;
    public static final String MY_PREFS_FILENAME = "mypref";
    private boolean demander = false;
    private String request;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_courses);

        erreurAPI = new EditText(getApplicationContext());
        erreurAPI.setText("API Innaccessible, encore...\nVeuillez réessayer dans quelques jours....");
        erreurAPI.setVisibility(View.INVISIBLE);
        LinearLayout layout = findViewById(R.id.layoutListeCourseIn);
        layout.addView(erreurAPI);

        Toolbar myToolbar = findViewById(R.id.mytoolbar);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("Course");
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
        if (item.getItemId() == R.id.home) {
            Intent intent = new Intent(ListCoursesActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.cat){
            Intent intent = new Intent(ListCoursesActivity.this, DailyCatFact.class);
            startActivity(intent);
            finish();
            return true;
        }else if (item.getItemId() == R.id.map) {
            Intent intent = new Intent(ListCoursesActivity.this, RedirectMapActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.car){
            Intent intent = new Intent(ListCoursesActivity.this, ListCoursesActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return false;
    }

    public void call(String param){
        if(this.demander){
            LinearLayout lay = findViewById(R.id.layoutListeCourseIn);
            lay.removeAllViews();
            /*for (int i = 0; i<editTexts.size(); i++) {
                editTexts.get(i).setVisibility(View.INVISIBLE);

            }*/
            courses.removeAll(courses);
            editTexts.removeAll(editTexts);
        }


        this.demander = true;

        ExecutorService ex= Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        ex.execute(() -> {
            String data = getDataFromHTTP(param);
            handler.post(() -> {
                try {

                    display(data);
                } catch (JSONException e) {
                    erreurAPI.setVisibility(View.VISIBLE);
                }
            });
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
        JSONObject circuit;

        LinearLayout layout = findViewById(R.id.layoutListeCourseIn);

        courses = new ArrayList<>();

        JSONObject root = new JSONObject(toDisplay);
        JSONObject suite = root.getJSONObject("MRData");
        JSONObject raceTable = suite.getJSONObject("RaceTable");
        JSONArray race = raceTable.getJSONArray("Races");
        for(int i=0; i<race.length();i++) {

            JSONObject Jname = race.getJSONObject(i);
            season = Jname.getString("season");
            round = Jname.getString("round");
            name = Jname.getString("raceName");

            circuit = Jname.getJSONObject("Circuit");
            JSONObject loc = circuit.getJSONObject("Location");
            locality = loc.getString("locality");
            country = loc.getString("country");
            JSONArray Results = Jname.getJSONArray("Results");
            JSONObject firsts = Results.getJSONObject(0);

            JSONObject driver = firsts.getJSONObject("Driver");
            first = driver.getString("givenName") + " " + driver.getString("familyName");

            JSONObject construct = firsts.getJSONObject("Constructor");
            constructorFirst = construct.getString("name");

            JSONObject lap = firsts.getJSONObject("Time");
            firstTime = lap.getString("time");

            JSONObject seconds = Results.getJSONObject(1);

            driver = seconds.getJSONObject("Driver");
            second = driver.getString("givenName") + driver.getString("familyName");

            construct = seconds.getJSONObject("Constructor");
            constructorSecond = construct.getString("name");

            lap = seconds.getJSONObject("Time");

            secondTime = lap.getString("time");
            JSONObject thirds = Results.getJSONObject(2);

            driver = thirds.getJSONObject("Driver");
            third = driver.getString("givenName") + driver.getString("familyName");

            construct = thirds.getJSONObject("Constructor");
            constructorThird = construct.getString("name");

            lap = thirds.getJSONObject("Time");
            thirdTime = lap.getString("time");

            Course course = new Course(name, season, round, locality, country, first, firstTime, constructorFirst, second, secondTime, constructorSecond, third, thirdTime, constructorThird);
            courses.add(course);
            generateTextView(i, layout, course.affichageSimple());

        }
    }

    public void searchSeason(View view) {
        Handler handler = new Handler(Looper.getMainLooper());
        int annee = 2024;
        EditText et = findViewById(R.id.year);
        TextView tw = findViewById(R.id.wrongSearch);
        String search = et.getText().toString();
        if (search.matches("-?\\d+")) {
            if (parseInt(search) > 2024 || parseInt(search) < 1950) {
                tw.setVisibility(View.VISIBLE);
            } else {
                tw.setVisibility(View.INVISIBLE);
                annee = parseInt(search);
                request = "https://ergast.com/api/f1/" + annee + "/results.json?limit=999";
                call(request);
            }
        } else {
            request = "https://ergast.com/api/f1/" + annee + "/results.json?limit=999";
            call(request);
            tw.setVisibility(View.INVISIBLE);
            if(! search.equals(""))
                tw.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void generateTextView(int index, LinearLayout layout, String text){
        EditText editText = new EditText(getApplicationContext());
        final boolean[] affSimple = {true};
        editText.setOnTouchListener(new OnSwipeTouchListener(ListCoursesActivity.this) {
            @Override public void onSwipeLeft() {
            String s = "";
                if(affSimple[0]){
                    s = courses.get(index).affichageSimple();
                }
                else{
                    s = courses.get(index).toString();
                }

            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_FILENAME, MODE_PRIVATE).edit();
            editor.putString("courseFav", courses.get(index).affichageSimple());

            editor.commit();
            Toast toast = Toast.makeText(getApplicationContext(), "Mis en course favorite", Toast.LENGTH_SHORT );
            toast.show();

            }

            @Override
            public void onSwipeRight() {
                if(affSimple[0]){
                    editText.setText(courses.get(index).toString());
                    affSimple[0] = false;
                }
                else{
                    editText.setText(courses.get(index).affichageSimple());
                    affSimple[0] = true;
                }

            }
        });

        editText.setText(text);
        editText.setId(index);
        layout.addView(editText);
        editTexts.add(editText);
    }

    public List<Course> getCourses() {
        return courses;
    }

}