/*An activity that contains a gridview that is used to display
* Manager's teams and their information*/
package com.example.sportmanager;

import android.os.Bundle;
import android.widget.GridView;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;

public class ManagerActivity extends AppCompatActivity {
    DBHelper db;
    League selectedLeague;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_layout);

        db = new DBHelper(this);

        //Retrieves teams from database for each league
        for(int i = 0; i < League.getAllLeagues().size(); i++) {
            selectedLeague = League.getAllLeagues().get(i);
            selectedLeague.setTeams(db.getTeamsDB(selectedLeague.getLeagueID()));
        }

        //Retrieves manager's money from database
        Manager.setMoney(db.getManagerMoneyDB());

        //Retrieves manager's teams from database
        ArrayList<Integer> managerTeamsID = db.getManagerTeamsID();
        ArrayList<Team> temp = new ArrayList<>();
        for(League l : League.getAllLeagues()) {
            for(Team t : l.getTeams()) {
                if(managerTeamsID.contains(t.getTeamID())) temp.add(t);
            }
        }
        Manager.setManagerTeams(temp);

        GridView gridview = findViewById(R.id.gridView);
        gridview.setAdapter(new ManagerAdapter(this));
    }

    @Override
    protected void onStop() {
        super.onStop();

        db.updateMoney(Manager.getMoney()); //Updates manager's money in the database
        db.updateManagerTeamID(Manager.getManagerTeams()); //Updates manager's teams in the database
        db.updateTeams(League.getAllLeagues()); //Updates each team's information in the database
    }
}
