/*Welcome screen that contains 3 buttons:
* 1) Starting MatchdayActivity
* 2) Starting ManagerActivity
* 3) Starting AllLeaguesActivity
* It also creates and populates a SQLite database*/
package com.example.sportmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button matchdayButton = findViewById(R.id.matchdayBttn);
        Button managerButton = findViewById(R.id.managerBttn);
        Button allLeaguesButton = findViewById(R.id.leagueMapBttn);

        matchdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MatchdayActivity.class);
                startActivity(intent);
            }
        });

        managerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ManagerActivity.class);
                startActivity(intent);
            }
        });

        allLeaguesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AllLeaguesActivity.class);
                startActivity(intent);
            }
        });

        File database=getApplicationContext().getDatabasePath(DBHelper.DATABASE_NAME);
        DBHelper db = new DBHelper(this);

        if (!database.exists()) {
            BufferedReader reader;

            //get each league's information from csv file to SQLite database
            try {
                reader = new BufferedReader(new InputStreamReader(getAssets().open("leagues.csv")));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] temp = line.split(",");
                    db.insertLeagueDB(Integer.valueOf(temp[0]), String.valueOf(temp[1]), String.valueOf(temp[2]), Integer.valueOf(temp[3]), Integer.valueOf(temp[4]));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            //get each team's information from csv file to SQLite database
            try {
                reader = new BufferedReader(new InputStreamReader(getAssets().open("teams.csv")));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] temp = line.split(",");
                    db.insertTeamDB(Integer.valueOf(temp[0]), String.valueOf(temp[1]), Integer.valueOf(temp[2]), Integer.valueOf(temp[3]));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            //get Manager's information from csv file to SQLite database
            db.insertManager();
        }
    }
}
