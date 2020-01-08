/*Contains League's id, name, type, team list, and team table*/
package com.example.sportmanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class League {
    private static ArrayList<League> allLeagues;

    private ArrayList<Team> teamList = new ArrayList<>();

    protected int id;
    protected String name;
    String type;

    public League(){}
    public League(int id){
        this.id = id;
    }

    //Sorts teams based on points in descending order
    //Returns an ArrayList with sorted objects
    ArrayList<Team> getTeamTable(){
        ArrayList<Team> teamTable = new ArrayList<>(teamList);

        Collections.sort(teamTable, new Comparator<Team>() {
            @Override
            public int compare(Team o1, Team o2) {
                return o2.getPoints() - o1.getPoints();
            }
        });
        return teamTable;
    }

    static ArrayList<League> getAllLeagues() {
        return allLeagues;
    }

    static void setAllLeagues(ArrayList<League> leagues) {
        allLeagues = leagues;
    }

    ArrayList<Team> getTeams() {
        return teamList;
    }

    void setTeams(ArrayList<Team> teams) {
        teamList = teams;
    }

    int getLeagueID() {
        return id;
    }

    String getLeagueName() {
        return name;
    }

    String getLeagueType() {
        return type;
    }
}
