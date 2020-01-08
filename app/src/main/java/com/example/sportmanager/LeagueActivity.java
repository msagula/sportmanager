/*An activity that contains a gridview that is used to display selected
* league's team table and their information*/
package com.example.sportmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;

public class LeagueActivity extends AppCompatActivity {
    int pos;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_layout);

        Intent intent = getIntent();
        //A position of a grid cell that was clicked in the AllLeaguesActivity
        pos = intent.getIntExtra(AllLeaguesActivity.EXTRA_RES_ID, 999);

        db = new DBHelper(this);

        //Populates an ArrayList with teams that belong to that league
        //Teams are retrieved from the database
        for (League l : League.getAllLeagues()) {
            l.setTeams(db.getTeamsDB(l.getLeagueID()));
        }

        //Retrieves, from the database, and sets the amount of Manager's money
        Manager.setMoney(db.getManagerMoneyDB());

        //Retrieves teams' IDs from database and populates the ArrayList of teams that the Manager owns
        ArrayList<Integer> managerTeamsID = db.getManagerTeamsID();
        ArrayList<Team> temp = new ArrayList<>();
        for(League l : League.getAllLeagues()) {
            for(Team t : l.getTeams()) {
                if(managerTeamsID.contains(t.getTeamID())) temp.add(t);
            }
        }
        Manager.clearManagerTeams();
        Manager.setManagerTeams(temp);

        GridView gridview = findViewById(R.id.gridView);
        gridview.setAdapter(new LeagueAdapter(this, pos));
    }
    @Override
    protected void onStop() {
        super.onStop();

        db.updateMoney(Manager.getMoney()); //Updates Manager's money in the database
        db.updateManagerTeamID(Manager.getManagerTeams()); //Update's Manager's team IDs in the database
    }
}
