package com.ittcapstone;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The starting point of the program.
 *
 * For the program to compile and run,
 * the jars in the 'libraries' folder must be added to the build.
 *
 * @author <a href="http://rwbay.com/">Nicholas Jensen</a> on 4/17/16.
 */
public class Main extends Application {

    /**
     * This is the controller for the whole program
     */
    private MainController mainController;

    /**
     * @param primaryStage This is the stage passed in by the Application
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        //System.out.println("* Application Launch called");

        /**
         * Starts the main application and GUI
         */
        mainController = new MainController();
        mainController.startApp(primaryStage, mainController);

    }

    public static void main(String[] args) {

        /**
         * Runs the application (GUI section)
         */
        //System.out.println("- - - starting application - - - ");
        launch(args);
        //System.out.println("- - - bye - - -");

    }

}
