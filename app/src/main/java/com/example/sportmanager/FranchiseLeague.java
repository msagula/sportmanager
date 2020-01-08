/*Extends League class
* It is created if the type id Franchise
* Teams in this type of league cannot be moved to another league */
package com.example.sportmanager;

public class FranchiseLeague extends League {

    private final String TYPE = "Franchise";

    FranchiseLeague(int id, String name) {
        type = TYPE;
        this.id = id;
        this.name = name;
    }
}
