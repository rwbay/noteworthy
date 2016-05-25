package com.ittcapstone.model.sqlite;

import com.ittcapstone.Configurations;

import java.util.ArrayList;

/**
 * This is the initial data for the database.
 * The SQL Queries are written for SQLite.
 *
 * @author <a href="http://rwbay.com/">Nicholas Jensen</a> on 5/2/16.
 * @version 1.0
 */
public class SQLiteInitialData {

    // the current database version
    private static double databaseVersion = 0.1;

    // array for the initial data
    private static ArrayList<String> initialDataQuery = new ArrayList<>();

    /**
     * The SQL to create and populate the database.
     *
     * @return The SQL query returned as a string
     */
    public static ArrayList<String> getInitialDataQuery() {

        initialDataQuery.add("CREATE TABLE main.NoteEntries" +
                " (" +
                " id INTEGER PRIMARY KEY," +
                " Title TEXT," +
                " Text TEXT," +
                " TmStamp TEXT" +
                ");");

        long nowTIme = System.currentTimeMillis();
        initialDataQuery.add("INSERT INTO main.NoteEntries" +
                " (Title,Text,TmStamp)" +
                " VALUES" +
                " (" +
                " ' My first note '" +
                " , ' Welcome to " + Configurations.getApplicationName() + ". Please, enjoy our application. '" +
                " , " + nowTIme +
                ")");


        return initialDataQuery;

    }

}
