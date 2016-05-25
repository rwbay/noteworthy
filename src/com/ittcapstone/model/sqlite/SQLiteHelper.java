package com.ittcapstone.model.sqlite;

import com.ittcapstone.Configurations;
import com.ittcapstone.model.data.Entry;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

/**
 * Database tools for working with SQLite
 *
 * @author <a href="http://rwbay.com/">Nicholas Jensen</a> on 4/28/16.
 * @version 1.0
 */
public class SQLiteHelper {

    /**
     * Sets up the database. Loads all the initial data.
     */
    public void setup() {

        System.out.println("* database setup");

        // check configurations file
        if (Configurations.getUseSQLiteDatabase()) {

            // look for database
            if (Files.notExists(Paths.get(Configurations.getSQLiteDatabase()))) {
                System.out.println("- database does not exist");
                querySQLiteInitialData();
            } else {
                System.out.println("- database already exists");
                System.out.println("- - " + Configurations.getSQLiteDatabase());
            }

        }

    }

    /**
     * Use a Try With Resources to auto-close the connection.
     *
     * @return Connection object
     */
    private Connection connect() {

        // SQLite connection string
        String url = "jdbc:sqlite:" + Configurations.getSQLiteDatabase();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;

    }

    private void querySQLiteInitialData() {

        System.out.println("* creating new database");

        try (Connection connection = connect()) {

            if (connection != null) {

                DatabaseMetaData meta = connection.getMetaData();
                System.out.println("- new database has been created");
                System.out.println("- - " + Configurations.getSQLiteDatabase());
                System.out.println("- - The driver name is " + meta.getDriverName());

                ArrayList<String> initialDataQuery = SQLiteInitialData.getInitialDataQuery();

                for (int i = 0; i < initialDataQuery.size(); i++) {

                    // submit the query for database setup
                    query(initialDataQuery.get(i));

                }

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Submits sql query without modifying or checking.
     * @param sql sql string to submit to the database.
     */
    public void query(String sql) {

        System.out.println(" - " + sql);

        try (Connection connection = connect();
             Statement stmt = connection.createStatement()) {

            stmt.execute(sql);

        } catch (SQLException e) {
            System.out.println("* SQL query NOT successfully submitted");
            System.out.println(e.getMessage());
        }

    }

    /**
     * @return List of Entries from the database
     */
    public ArrayList<Entry> queryEntries() {

        ArrayList<Entry> entries = new ArrayList<>();
        String sql = "SELECT * FROM NoteEntries ORDER BY id ASC";

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                entries.add(new Entry(resultSet.getInt("id"), resultSet.getString("Title"), resultSet.getString("Text"), resultSet.getString("TmStamp")));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return entries;

    }

    /**
     * @param title Title of note
     * @param text  Text of note
     * @return True if successful; False if unsuccessful.
     */
    public boolean queryInsert(String title, String text) {

        //System.out.println("* queryInsert");

        String theDate = "" + System.currentTimeMillis();

        String sql = "INSERT INTO main.NoteEntries (Title,Text,TmStamp) VALUES (?,?,?)";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // set the corresponding param
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, text);
            preparedStatement.setString(3, theDate);

            // execute the delete statement
            int result = preparedStatement.executeUpdate();

            //System.out.println("- result: "+result);

            if (result > 0) return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;

    }

    /**
     * @param id    The ID of the entry in the database
     * @param title Title of entry
     * @param text  Text of entry
     * @return True if successful; False if unsuccessful.
     */
    public boolean queryUpdate(int id, String title, String text) {

        //System.out.println("* queryUpdate");

        String theDate = "" + System.currentTimeMillis();

        String sql = "UPDATE main.NoteEntries SET Title = ?1, Text = ?2, TmStamp = ?3 WHERE id = ?4";
        //String sql = "UPDATE main.NoteEntries SET Title = ? WHERE id = ?";
        //System.out.println("- "+sql);

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // set the corresponding param
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, text);
            preparedStatement.setString(3, theDate);
            preparedStatement.setInt(4, id);

            // execute the delete statement
            int result = preparedStatement.executeUpdate();

            //System.out.println("- result: "+result);

            if (result > 0) return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;

    }

    /**
     * @param id The ID of the entry in the database
     * @return True if successful; False if unsuccessful.
     */
    public boolean queryDelete(int id) {

        String sql = "DELETE FROM main.NoteEntries WHERE id = ?";

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // set the corresponding param
            preparedStatement.setInt(1, id);

            // execute the delete statement
            int result = preparedStatement.executeUpdate();

            if (result > 0) return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;

    }

}
