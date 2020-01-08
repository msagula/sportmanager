/*Calculates result/winner for a given match
* Comes up with which team is playing against which for a given week
* Return and sets week number*/
package com.example.sportmanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Matchday {
    private static int weekNumber;

    public Matchday() {}

    static void resetWeek() {weekNumber = 0;}

    static int getWeek() {return weekNumber;}

    static void setWeek(int dbWeek) {weekNumber = dbWeek;}

    static void nextWeek() {weekNumber += 1;}

    static String getResults(int week) {
        StringBuilder result = new StringBuilder();

        ArrayList<Team> matchdayTeams;

        for(League l : League.getAllLeagues()) {

            matchdayTeams = getMatchdayTeams(l.getTeams(), week);

            int gamesNumber = matchdayTeams.size()/2;
            for(int i = 0; i < gamesNumber; i++) {
                Team t1 = matchdayTeams.get(i);
                Team t2 = matchdayTeams.get(i+gamesNumber);

                //Returns either 1 if the t1 wins or 2 if t2 wins
                int winner = calculateWinner(t1, t2);

                //Appends Manager's teams match results to a StringBuilder
                for(Team t : Manager.getManagerTeams()) {
                    if (t.getTeamID() == t1.getTeamID() || t.getTeamID() == t2.getTeamID()) {
                        if (winner == 1) {
                            result.append(t1.getTeamName());
                            result.append(" beat ");
                            result.append(t2.getTeamName());
                            result.append("\n");
                        } else {
                            result.append(t2.getTeamName());
                            result.append(" beat ");
                            result.append(t1.getTeamName());
                            result.append("\n");
                        }
                    }
                }
            }
        }
        return result.toString();
    }

    //For each week, teams in the ArrayList are moved to different position so that
    //each team throughout the season will play against every team, and only once.
    private static ArrayList<Team> getMatchdayTeams(ArrayList<Team> teamList, int week) {
        ArrayList<Team> finalList = new ArrayList<>();
        ArrayList<Team> tempList = new ArrayList<>(teamList);

        //Team in the 0th position is added to new Array. It will always stay in the 0th position
        Team first = tempList.get(0);
        finalList.add(first);
        tempList.remove(0);

        //Remaining teams of the old ArrayList get rotated to next position by 'week' amount of times
        Collections.rotate(tempList, week);

        //The first half of the old ArrayList gets added to a List
        //Second half gets added to another list
        List<Team> firstHalf = tempList.subList(0, tempList.size()/2);
        List<Team> secondHalf = tempList.subList(tempList.size()/2, tempList.size());

        //Reflects second list
        Collections.reverse(secondHalf);

        //First and seconds list are added to the new ArrayList
        finalList.addAll(firstHalf);
        finalList.addAll(secondHalf);

        return finalList;
    }

    //Each team's probability of winning is based on how many points and money they have
    private static int calculateWinner(Team t1, Team t2) {
        ArrayList<Integer> temp = new ArrayList<>();

        int money1 = t1.getMoney();
        if(money1 < 0) money1 = 0;
        int money2 = t2.getMoney();
        if(money2 < 0) money2 = 0;

        //Points and money/100 are added for both teams
        int i1 = t1.getPoints() + (money1/100);
        int i2 = t2.getPoints() + (money2/100);

        //Adds 1 to an ArrayList for each of first team's point and money/100 unit
        //Adds 2 to an ArrayList for each of second team's point and money/100 unit
        for(int i = 0; i < i1; i++) temp.add(1);
        for(int i = 0; i < i2; i++) temp.add(2);

        //If both teams have no points and 0/negative money, a 1 and a 2 is added to an ArrayList
        if(temp.size() == 0) {
            temp.add(1);
            temp.add(2);
        }

        //ArrayList is shuffled so that 1s and 2s are not grouped together
        Collections.shuffle(temp);

        //selects a random number from the ArrayList that contains 1s and 2s
        Random rand = new Random();
        int x = temp.get(rand.nextInt(temp.size()));

        //If the first team won, points and money are added to it
        //Money are subtracted from second team
        if(x == 1) {
            t1.increaseWorth();
            t2.decreaseWorth();
            t1.addPoints();

            //If manager owns the first team, money are added to Manager
            //If manager owns the second team, money are subtracted from Manager
            for(Team t : Manager.getManagerTeams()) {
                if(t.getTeamID() == t1.getTeamID()) Manager.addMoney(100);
                if(t.getTeamID() == t2.getTeamID()) Manager.subtractMoney(100);
            }
        }
        //If the second team won, points and money are added to it
        //Money are subtracted from first team
        else if(x == 2) {
            t2.increaseWorth();
            t1.decreaseWorth();
            t2.addPoints();

            //If manager owns the first team, money are subtracted from Manager
            //If manager owns the second team, money are added to Manage
            for(Team t : Manager.getManagerTeams()) {
                if(t.getTeamID() == t2.getTeamID()) Manager.addMoney(100);  //final variable
                if(t.getTeamID() == t1.getTeamID()) Manager.subtractMoney(100);
            }
        }
        return x;
    }
}
