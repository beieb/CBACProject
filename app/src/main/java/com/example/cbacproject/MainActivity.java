package com.example.cbacproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * page home activity
 */

public class MainActivity extends AppCompatActivity {

    private TextView circuit;
    private TextView winner;

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
        /**
         * un tableau de string contenant le nom de la course et le vainqueure a modifier avec l'apis
         */
        String[] s = new String[]{"hello", "world"};
        initmlatestRace(s);

    }


    private void initmlatestRace(String[] s){
        /**
         * prend en parametre un tableau de string contenant le nom du circuit de la derniere course
         * et le vainqueure
         */
        this.circuit.setText(s[0]+"\n"+s[1]);
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
            Log.d("CBAC", "home yes");
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.cat){
            Log.d("CBAC", "user yes");
            Intent intent = new Intent(MainActivity.this, DailyCatFact.class);
            startActivity(intent);
            finish();
            return true;
        }else if (item.getItemId() == R.id.map) {
            Log.d("CBAC", "map yes");
            Intent intent = new Intent(MainActivity.this, RedirectMapActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.car){
            Log.d("CBAC", "mountaineer yes");
            return true;
        }
        return false;
    }

    public void goOnRace(View v){
        Intent intent = new Intent(MainActivity.this, raceActivity.class);
        startActivity(intent);
        finish();
    }
}