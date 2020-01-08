/*Contains team's id, name, league that the team belongs to,
* worth. and points*/
package com.example.sportmanager;

public class Team {

    private int id;
    private String name;
    private int leagueID;
    private int money;
    private int points;

    public Team(int id, String name, int leagueID, int money, int points){
        this.id = id;
        this.name = name;
        this.leagueID = leagueID;
        this.money = money;
        this.points = points;
    }

    void increaseWorth(){
        money += 100;
    }

    void decreaseWorth(){
        money -= 100;
    }

    void increaseWorthLW() { money += 300; }

    void addPoints() {
        points+= 3;
    }

    void resetPoints() {points = 0;}

    int getTeamID() {return id;}

    String getTeamName() {return name;}

    int getLeagueID() {return leagueID;}

    void setLeagueID(int lID) {leagueID = lID;}

    int getMoney() {return money;}

    int getPoints() {return points;}

}
