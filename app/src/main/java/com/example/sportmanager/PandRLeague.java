/*Extends League class
* It is created if the type of the league is PandR
* It allows teams to be moved to leagues specified in aboveLeague and belowLeague variables*/
package com.example.sportmanager;

import java.util.ArrayList;

public class PandRLeague extends League {

    private final String TYPE = "PandR";

    private int aboveLeague;
    private int belowLeague;

    PandRLeague(int id, String name, int aboveLeague, int belowLeague) {
        type = TYPE;

        this.id = id;
        this.name = name;
        this.aboveLeague = aboveLeague;
        this.belowLeague = belowLeague;
    }

    //Adds the first 3 teams in the ArrayList returned by getTeamTable to the League's teamList
    //specified by the aboveLeague variable
    //Changes the League ID of first 3 teams in an ArrayList that was returned by getTeamTable()
    void promoteTeams() {
        ArrayList<Team> aboveLeagueTeams = League.getAllLeagues().get(aboveLeague-1).getTeams();

        for (int i = 0; i < 3; i++) {
            if(i == 0) getTeamTable().get(i).increaseWorthLW(); //adds 300 to a team that won the league
            aboveLeagueTeams.add(getTeamTable().get(i));
            getTeamTable().get(i).setLeagueID(aboveLeague);
            getTeams().remove(getTeamTable().get(i));
        }
    }

    //Adds the last 3 teams in the ArrayList returned by getTeamTable to the League's teamList
    //specified by the belowLeague variable
    //Changes the League ID of last 3 teams in an ArrayList that was returned by getTeamTable()
    void relegateTeams() {
        int leagueSize = getTeams().size();
        ArrayList<Team> belowLeagueTeams = League.getAllLeagues().get(belowLeague-1).getTeams();

        for (int i = leagueSize -1 ; i > leagueSize - 4; i--) {
            belowLeagueTeams.add(getTeamTable().get(i));
            getTeamTable().get(i).setLeagueID(belowLeague);
            getTeams().remove(getTeamTable().get(i));
        }
    }

    int getAboveLeague() {
        return aboveLeague;
    }

    int getBelowLeague() {
        return belowLeague;
    }
}