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

        @SuppressLint("MissingInflatedId") Toolbar myToolbar = findViewById(R.id.mytoolbar);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("HOME");

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

}