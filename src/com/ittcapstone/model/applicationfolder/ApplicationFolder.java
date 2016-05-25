package com.ittcapstone.model.applicationfolder;

import com.ittcapstone.Configurations;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This creates the application folder,
 * then the database folder,
 * then the temporary folder.
 * Created by Nicholas Jensen on 9/27/15.
 *
 * @author <a href="http://rwbay.com/">Nicholas Jensen</a>
 * @version 1.0
 */
public class ApplicationFolder {

    // pass / fail variables
    private boolean creationResult1 = false;
    private boolean creationResult2 = false;
    private boolean creationResult3 = false;

    /**
     * Sets up the folders for the application.
     *
     * @return boolean true = created application folders, false = could not create.
     */
    public boolean setup() {

        System.out.println("* Setting up application folder *");

        // Create application folder
        creationResult1 = folderCreate(Configurations.getApplicationFolder());

        // Create database folder
        creationResult2 = folderCreate(Configurations.getSQLiteDatabaseFolderName());

        // Create temporary files folder
        creationResult3 = folderCreate(Configurations.getTempFilesFolder());

        if (getCreationResult()) {
            return true;
        }
        return false;

    }

    /**
     * Checks to see if all the directories were created successfully.
     *
     * @return boolean true = created directories, false = could not create.
     */
    public boolean getCreationResult() {
        System.out.println("- Checking application folder creation result");
        if (creationResult1 && creationResult2 && creationResult3) {
            System.out.println("- - everything looks good");
            return true;
        } else {
            if (!creationResult1)
                System.out.println("- - " + Configurations.getApplicationFolder() + " NOT created successfully");
            if (!creationResult2)
                System.out.println("- - " + Configurations.getSQLiteDatabaseFolderName() + " NOT created successfully");
            if (!creationResult3)
                System.out.println("- - " + Configurations.getTempFilesFolder() + " NOT created successfully");
        }

        return false;
    }

    /**
     * This creates the specified folder
     *
     * @param directory This is the directory/folder to create
     * @return boolean true = created directory, false = could not create.
     */
    private boolean folderCreate(String directory) {

        System.out.println("- Checking for directory: " + directory);

        // Create application folder
        if (Files.notExists(Paths.get(directory))) {

            System.out.println("- - does not already exist");

            System.out.println("- - attempting to create");
            File ApplicationFolderCreate = new File(directory);
            ApplicationFolderCreate.mkdirs();

            if (Files.notExists(Paths.get(directory))) {
                System.out.println("- - - could not create directory");
                return false;
            } else {
                System.out.println("- - - directory created successfully");
                return true;
            }

        } else {
            System.out.println("- - directory already exists");
            return true;
        }

    }

}
