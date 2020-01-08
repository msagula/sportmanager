/*An activity that displays the match results of Manager's teams, which week of the
* season it is, and a button that advances the matchday to the next week
* At the end of the season, it promotes and relegates team to different leagues and
* display a message if any of Manager's team has been promoted or relegated*/
package com.example.sportmanager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;

public class MatchdayActivity extends AppCompatActivity {
    TextView tv, tv2;
    Button button;
    DBHelper db;
    League selectedLeague;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_matchday);

        db = new DBHelper(this);

        //if allLeagues arrayList is empty, populate it with data retrieved from SQLite database
        if(League.getAllLeagues() == null) League.setAllLeagues(db.getLeaguesDB());

        //Retrieves teams' IDs from database and populates the ArrayList of teams that the Manager owns
        ArrayList<Integer> managerTeamsID = db.getManagerTeamsID();
        ArrayList<Team> temp = new ArrayList<>();
            for (League l : League.getAllLeagues()) {
                for (Team t : l.getTeams()) {
                    if (managerTeamsID.contains(t.getTeamID())) temp.add(t);
                }
            }
        Manager.setManagerTeams(temp);

        Manager.setMoney(db.getManagerMoneyDB());

        //retrieves teams from database and assigns them to a league that they belong to
        for(int i = 0; i < League.getAllLeagues().size(); i++) {
            selectedLeague = League.getAllLeagues().get(i);
            selectedLeague.setTeams(db.getTeamsDB(selectedLeague.getLeagueID()));
        }

        Matchday.setWeek(db.getWeek());

        tv = findViewById(R.id.weekNumber);
        tv2 = findViewById(R.id.results);
        button = findViewById(R.id.nextWeekButton);

        //Displays the current week of the season
        tv.setText(Integer.toString(Matchday.getWeek()));

        //'Next Week' button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matchday.nextWeek();

                //Displays the current week of the season
                tv.setText(Integer.toString(Matchday.getWeek()));

                //If the season is ongoing, it gets Manager's team result for current match week
                if(Matchday.getWeek() < 10) { tv2.setText(Matchday.getResults(Matchday.getWeek())); }

                //End of the season
                else if (Matchday.getWeek() == 10) {
                    StringBuilder pMessage = new StringBuilder("Your teams that have been promoted: ");
                    StringBuilder rMessage = new StringBuilder("Your teams that have been relegated: ");

                    //Promotes top 3 teams and relegates bottom 3 teams, if there are leagues
                    //above and/or below given league
                    //It also builds a string with Manager's teams that have been promoted and/or relegated
                    for(League l : League.getAllLeagues()) {
                        if(l instanceof PandRLeague) {
                            int al = ((PandRLeague) l).getAboveLeague();
                            int bl = ((PandRLeague) l).getBelowLeague();
                            if(al != 0) { //if there is a league above 'l'
                                pMessage.append(checkManagerPromoted(l)); ////Adds names, to the message, of the Manager's teams that have been promoted
                                ((PandRLeague) l).promoteTeams();
                            }
                            if(bl != 0) { //if there is a league below 'l'
                                rMessage.append(checkManagerRelegated(l)); //Adds names, to the message, of the Manager's teams that have been relegated
                                ((PandRLeague) l).relegateTeams(); }
                        }
                    }

                    //Displays a dialog message informing user which of the teams that the Manager
                    //owns got promoted and/or relegated
                    AlertDialog.Builder mMessage  = new AlertDialog.Builder(MatchdayActivity.this);
                    mMessage.setMessage(pMessage + "\n\n" + rMessage);
                    mMessage.setTitle("End of a season");
                    mMessage.setPositiveButton("OK", null);
                    mMessage.setCancelable(true);
                    mMessage.create().show();


                    //resets match week to 0 (a new season)
                    Matchday.resetWeek();

                    //resets each team's points
                    for (League l : League.getAllLeagues()) {
                        for (Team t : l.getTeams()) {
                            t.resetPoints();
                        }
                    }
                }
            }
        });
    }

    //returns a StringBuilder with the names of Manager's teams that have been promoted
    private StringBuilder checkManagerPromoted(League league) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (Team t : Manager.getManagerTeams()) {
                if (t.getTeamID() == league.getTeamTable().get(i).getTeamID()) {
                    sb.append(t.getTeamName());
                    sb.append(", ");
                }
            }
        }
        return sb;
    }

    //returns a StringBuilder with the names of Manager's teams that have been relegated
    private StringBuilder checkManagerRelegated(League league) {
        StringBuilder sb = new StringBuilder();
        int tSize = league.getTeamTable().size();

        for (int i = tSize - 3; i < tSize; i++) {
            for (Team t : Manager.getManagerTeams()) {
                if (t.getTeamID() == league.getTeamTable().get(i).getTeamID()) {
                    sb.append(t.getTeamName());
                    sb.append(", ");
                }
            }
        }
        return sb;
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Updates Manager's money in the database
        db.updateMoney(Manager.getMoney());

        //Updates current week in the database
        db.updateWeek(Matchday.getWeek());

        //Updates each team's information in the database
        db.updateTeams(League.getAllLeagues());
    }
}
