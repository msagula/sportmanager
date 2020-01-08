/*Contains functions that save and retrieve information
* from SQLite database*/
package com.example.sportmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "MyDBName.db";

    DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table leagues " +
                "(id integer primary key, name text, type text, aboveLeague integer, belowLeague integer)");
        db.execSQL("create table teams " +
                "(id integer primary key, name text, leagueID integer, money integer, points integer default 0)");
        db.execSQL("create table manager " +
                "(id integer primary key autoincrement, money integer default 3000, week integer default 0)");
        db.execSQL("create table teamsowned " +
                "(id integer primary key autoincrement, teamID integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS leagues");
        db.execSQL("DROP TABLE IF EXISTS teams");
        db.execSQL("DROP TABLE IF EXISTS manager");
        onCreate(db);
    }

    //Retrieves League data from database and creates either PandR (Promotion or relegation) or
    //Franchise League. Returns an ArrayList with all those created Leagues
    ArrayList<League> getLeaguesDB() {
        ArrayList<League> allLeagues = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res =  db.rawQuery( "select * from leagues", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            res.getString(1);
            if(res.getString(2).equals("PandR"))
                allLeagues.add(new PandRLeague(res.getInt(0), res.getString(1),
                        res.getInt(3), res.getInt(4)));
            else if(res.getString(2).equals("Franchise"))
                allLeagues.add(new FranchiseLeague(res.getInt(0), res.getString(1)));
            res.moveToNext();
        }
        res.close();
        return allLeagues;
    }

    //Retrieves and creates Teams that belong to a specified League.
    //Return an ArrayList with those created Teams
    ArrayList<Team> getTeamsDB(int leagueid) {
        ArrayList<Team> teams = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res =  db.rawQuery( "select * from teams where leagueID = " + leagueid, null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            teams.add(new Team(res.getInt(0), res.getString(1), res.getInt(2), res.getInt(3), res.getInt(4)));
            res.moveToNext();
        }
        res.close();
        return teams;
    }

    //retrieves and returns Manager's money from database
    int getManagerMoneyDB() {
        int money = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res =  db.rawQuery( "select * from manager", null );
        res.moveToFirst();
        while(!res.isAfterLast()){
            money = res.getInt(1);
            res.moveToNext();
        }
        res.close();
        return money;
    }

    //retrieves and returns IDs of teams from database that the Manager owns
    ArrayList<Integer> getManagerTeamsID() {
        ArrayList<Integer> teams = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res =  db.rawQuery( "select * from teamsowned", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            teams.add(res.getInt(1));
            res.moveToNext();
        }
        res.close();
        return teams;
    }

    //updates Manager's money in database
    void updateMoney(int money) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("money", money);

        db.update("manager", contentValues, null, null);
    }

    //updates IDs of teams, in the database, that the Manager owns
    void updateManagerTeamID(ArrayList<Team> teams) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM teamsowned");

        for (Team t : teams) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("teamID", t.getTeamID());
            db.insert("teamsowned", null, contentValues);
        }
    }

    //updates each teams information in the database
    void updateTeams(ArrayList<League> allLeague) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM teams");

        for (League l : allLeague) {
            for (Team t : l.getTeams()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("id", t.getTeamID());
                contentValues.put("name", t.getTeamName());
                contentValues.put("leagueID", t.getLeagueID());
                contentValues.put("money", t.getMoney());
                contentValues.put("points", t.getPoints());

                db.insert("teams", null, contentValues);
            }
        }
    }

    //gets current week of the season from the database
    int getWeek() {
        int week = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res =  db.rawQuery( "select * from manager", null );
        res.moveToFirst();
        while(!res.isAfterLast()){
            week = res.getInt(2);
            res.moveToNext();
        }
        res.close();
        return week;
    }

    //updates current week of the season in the database
    void updateWeek(int week) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("week", week);

        db.update("manager", contentValues, null, null);
    }

    //inserts a new league's information to the database
    void insertLeagueDB(int id, String name, String type, int aboveLeague, int belowLeague) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("type", type);
        contentValues.put("aboveLeague", aboveLeague);
        contentValues.put("belowLeague", belowLeague);
        db.insert("leagues", null, contentValues);
    }

    //inserts a new team's information to the database
    void insertTeamDB(int id, String name, int leagueID, int money) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("leagueID", leagueID);
        contentValues.put("money", money);

        db.insert("teams", null, contentValues);
    }

    //insert's manager's information to the database
    void insertManager() {
        int money = 3000;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("money", money);

        db.insert("manager", null, contentValues);
    }
}
