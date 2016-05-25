package com.ittcapstone;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Configure the app with these variables
 * <p>
 * For the program to compile and run,
 * the jars in the 'libraries' folder must be added to the build.
 *
 * @author <a href="http://rwbay.com/">Nicholas Jensen</a> on 4/29/16.
 */
public class Configurations {

    // JavaFX variables
    private static String applicationName = "Note Worthy";
    private static String applicationURL = "http://play.rwbay.com/capstone-itt-technical-institute/";
    private static String applicationStageTitle = applicationName + ", for your worthy notes";
    private static double applicationWidthPreferred = 800.0;
    private static double applicationHeightPreferred = 500.0;
    private static String applicationCompanyLogo = "view/images/logo.png";
    private static String applicationFavicon = "view/images/favicon.png";

    // Application Folder
    private static String applicationFolder = "ITT-Capstone-Noteworthy/note-worthy";

    // folder for temporary files
    private static String tempFilesFolder = "temporary-files";

    // Derby JavaDB
    private static boolean useSQLiteDatabase = true;
    private static String SQLiteDatabaseFolderName = "SQLite";
    private static String SQLiteDatabaseFileName = "note-worthy.db";


    // - - - below are the getters - - -

    /**
     * @return Name of the application
     */
    public static String getApplicationName() {
        return applicationName;
    }

    /**
     * @return URL for the application
     */
    public static String getApplicationURL() {
        return applicationURL;
    }

    /**
     * @return The preferred width of the application
     */
    public static double getApplicationWidthPreferred() {
        return applicationWidthPreferred;
    }

    /**
     * @return The preferred height of the application
     */
    public static double getApplicationHeightPreferred() {
        return applicationHeightPreferred;
    }

    /**
     * @return The location of the company logo (relative to the Main Controller)
     */
    public static String getApplicationCompanyLogo() {
        return applicationCompanyLogo;
    }

    /**
     * @return The location of the company favicon (relative to the Main Controller)
     */
    public static String getApplicationFavicon() {
        return applicationFavicon;
    }

    /**
     * @return The title that will show on the top program bar
     */
    public static String getApplicationStageTitle() {
        return applicationStageTitle;
    }

    /**
     * @return Full path to the application folder on the hard drive
     */
    public static String getApplicationFolder() {
        String applicationFolderNew = System.getProperty("user.home", ".") + "/" + applicationFolder;
        return applicationFolderNew;
    }

    /**
     * @return True to use SQLite; False to not use SQLite
     */
    public static boolean getUseSQLiteDatabase() {
        return useSQLiteDatabase;
    }

    /**
     * @return Path of the SQLite database
     */
    public static String getSQLiteDatabaseFolderName() {

        String derbyFolderNew = getApplicationFolder() + "/" + SQLiteDatabaseFolderName;
        return derbyFolderNew;
    }

    /**
     * @return File name of the SQLite database
     */
    public static String getSQLiteDatabaseFileName() {

        if (!SQLiteDatabaseFileName.substring(SQLiteDatabaseFileName.length() - 3).equals(".db")) {
            SQLiteDatabaseFileName += ".db";
        }
        return SQLiteDatabaseFileName;
    }

    /**
     * @return Path and file name of the SQLite database
     */
    public static String getSQLiteDatabase() {

        String derbyDataBaseNameFolderNew = getSQLiteDatabaseFolderName() + "/" + getSQLiteDatabaseFileName();
        return derbyDataBaseNameFolderNew;
    }

    /**
     * @return Path of the temporary folder
     */
    public static String getTempFilesFolder() {

        String tempFilesFolderNew = getApplicationFolder() + "/" + tempFilesFolder + "/";
        return tempFilesFolderNew;
    }

    /**
     * @return Unique username for the user+computer
     */
    public static String getUserName() {

        String user = null;
        try {
            user = System.getProperty("user.name") + "@" + System.getProperty("os.name") + ":" + InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            user = System.getProperty("user.name") + "@" + System.getProperty("os.name");
            e.printStackTrace();
        }

        return user;

    }

}
