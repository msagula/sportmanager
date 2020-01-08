/*Contains id, money, and teams that the Manager owns*/
package com.example.sportmanager;

import java.util.ArrayList;

public class Manager {
    private static int id;
    private static int money;
    private static ArrayList<Team> teamsOwned = new ArrayList<>();

    public Manager(){ }

    static void setID(int managerID) {id = managerID;}

    static int getID() {return id;}

    static void addMoney(int amnt){
        money += amnt;
    }

    static void subtractMoney(int amnt) {
        money -= amnt;
    }

    static void setMoney(int amount) {money = amount;}

    static int getMoney() {return money;}

    static void addTeam(Team t) {
        teamsOwned.add(t);
    }

    static void removeTeam(Team t) {
        teamsOwned.remove(t);
    }

    static ArrayList<Team> getManagerTeams() {
        return teamsOwned;
    }

    static void setManagerTeams(ArrayList<Team> teams) {
        teamsOwned.clear();
        teamsOwned.addAll(teams);
    }

    static void clearManagerTeams() {
        teamsOwned.clear();
    }
}
