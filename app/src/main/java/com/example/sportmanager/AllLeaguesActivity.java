/*An activity that contains a list of all leagues
* When clicked on an individual league, a new
* activity will open that contains that league's team table and information*/
package com.example.sportmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

public class AllLeaguesActivity extends AppCompatActivity {
    DBHelper db;
    protected static final String EXTRA_RES_ID = "pos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_layout);

        db = new DBHelper(this);

        //if allLeagues arrayList is empty, populate it with data retrieved from SQLite database
        if(League.getAllLeagues() == null) League.setAllLeagues(db.getLeaguesDB());

        GridView gridview = findViewById(R.id.gridView);
        gridview.setAdapter(new AllLeaguesAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(AllLeaguesActivity.this,
                        LeagueActivity.class);

                //An intent extra containing which grid cell was clicked
                //Used for deciding which league to display
                intent.putExtra(EXTRA_RES_ID, position);
                startActivity(intent);
            }
        });
    }
}
