package com.example.cbacproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Map;

public class ParametreActivity extends AppCompatActivity {
    private static final String mypref = "mypref";
    private SharedPreferences sharePref;
    private int nbPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametre);
        init();

        Toolbar myToolbar = findViewById(R.id.mytoolbar);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("Para");
    }

    private void init(){
        Map<String, ?> listSave = Get();
        if(listSave.isEmpty()){
            TextView t;
            t = new TextView(getApplicationContext());
            t.setText("aucune donnée sauvegardé");
            t.setId(0);
            generate(t, findViewById(R.id.SaveLayout));
        }else {
            String[] s = listSave.keySet().toArray(new String[0]);
            s = tri(s);
            this.nbPref = 0;
            for (Object i : s) {
                generateListSave((String) i, listSave.get(i).toString(), this.nbPref, findViewById(R.id.SaveLayout));
                this.nbPref++;
            }
        }
    }

    public static String[] tri(String[] x) {
        for(int j =0; j<=x.length-1; j++) {
            for(int i =0; i<x.length-1; i++) {
                if(x[i].compareTo(x[i + 1]) > 0) {
                    String c = x[i];
                    x[i]=x[i+1];
                    x[i+1]=c;
                }
            }
        }
        return x;
    }

    private void generate(TextView t, LinearLayout layout){
        layout.addView(t);
    }


    private void generateListSave(String Label, String content, int index, LinearLayout layout){
        TextView t;
        t = new TextView(getApplicationContext());
        t.setText(Label+": "+  content);
        t.setId(index);
        t.setTextSize(20);
        CheckBox cb = new CheckBox(getApplicationContext());
        cb.setId(index+100);
        layout.addView(t);
        layout.addView(cb);

    }

    public Map<String, ?> Get(){
        sharePref=getSharedPreferences(mypref, Context.MODE_PRIVATE);
        return sharePref.getAll();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TextView txt;
        if (item.getItemId() == R.id.home) {
            Intent intent = new Intent(ParametreActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.cat){
            Intent intent = new Intent(ParametreActivity.this, DailyCatFact.class);
            startActivity(intent);
            finish();
            return true;
        }else if (item.getItemId() == R.id.map) {
            Intent intent = new Intent(ParametreActivity.this, RedirectMapActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.car){
            Intent intent = new Intent(ParametreActivity.this, ListCoursesActivity.class);
            startActivity(intent);
            finish();
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

    public void DeleteAll(View view){
        sharePref.edit().clear().apply();
        Intent intent = new Intent(ParametreActivity.this, ParametreActivity.class);
        startActivity(intent);
        finish();
    }
    public void DeleteSelect(View view){
        LinearLayout layoutQuiz = findViewById(R.id.SaveLayout);
        for(int i =0; i<nbPref;i++){
            TextView textView = layoutQuiz.findViewById(i);
            CheckBox checkBox = layoutQuiz.findViewById(i +100);
            String lab = textView.getText().toString();
            String Label= lab.split(":")[0];

            if(checkBox.isChecked()){
                sharePref.edit().remove(Label).apply();

            }
        }
        Intent intent = new Intent(ParametreActivity.this, ParametreActivity.class);
        startActivity(intent);
        finish();
    }

    public void addPref(View view){
        EditText context = findViewById(R.id.addContext);
        sharePref = getSharedPreferences(mypref,
                Context.MODE_PRIVATE);
        Save("Pref"+sharePref.getAll().size(), context.getText().toString());
        Intent intent = new Intent(ParametreActivity.this, ParametreActivity.class);
        startActivity(intent);
        finish();
    }
    public void Save(String Name, String name){
        sharePref = getSharedPreferences(mypref,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharePref.edit();
        editor.putString(Name, name);
        editor.apply();
    }

}