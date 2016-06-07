package com.ittcapstone;

import com.ittcapstone.control.PrimarySceneController;
import com.ittcapstone.control.SplashSceneController;
import com.ittcapstone.model.applicationfolder.ApplicationFolder;
import com.ittcapstone.model.sqlite.SQLiteHelper;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * The main controller.
 * This controlls the stage, scene, and SQLite objects.
 *
 * @author <a href="http://rwbay.com/">Nicholas Jensen</a> on 4/17/16.
 */
public class MainController {

    // SQLite Object
    private SQLiteHelper sqLiteHelper = new SQLiteHelper();

    // Application Folder Object
    private ApplicationFolder applicationFolder;

    // Its own class object
    private MainController mainController;

    // Primary Stage / Window
    private Stage primaryStage;

    // Splash Screen - Scene
    private Scene splashScene;

    // Splash Screen - Scene: Controller
    private SplashSceneController splashSceneController;

    // Primary Screen - Scene
    private Scene primaryScene;

    // Primary Screen - Scene: Controller
    private PrimarySceneController primarySceneController;

    /**
     * This starts the program and shows the GUI window.
     */
    public void startApp(Stage primaryStage, MainController mainController) {

        // set the parameters
        this.primaryStage = primaryStage;
        this.mainController = mainController;

        Task task1 = new Task() {
            @Override
            protected Object call() throws Exception {

                // set up the application folders
                applicationFolder = new ApplicationFolder();
                applicationFolder.setup();

                // set up the database
                sqLiteHelper = new SQLiteHelper();
                sqLiteHelper.setup();

                return null;
            }
        };
        Thread thread1 = new Thread(task1);
        thread1.start();

        Task task2 = new Task() {
            @Override
            protected Object call() throws Exception {

                // load the FXML, setup the scene
                loadPrimaryScene();

                return null;
            }
        };
        Thread thread2 = new Thread(task2);
        thread2.start();

        // load the FXML, setup the scene
        loadSplashScreen();

        // set the variables for the primary stage
        setUpStage();

        // load the stage
        showStage();

    }

    public boolean isStageShowing() {
        return primaryStage.isShowing();
    }

    private void loadSplashScreen() {

        //System.out.println("* Splash Scene");

        // set the scene
        FXMLLoader fxmlLoader = null;
        String fxmlFileLocation = "view/fxml/SplashScene.fxml";
        try {

            // load the FXML
            //System.out.println(" - loading the fxml: " + fxmlFileLocation);
            fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFileLocation));
            Parent root = (Parent) fxmlLoader.load();

            // pass parameters to new controller
            //System.out.println(" - running init process on the controller");
            splashSceneController = fxmlLoader.<SplashSceneController>getController();
            splashSceneController.initProcess(mainController);

            // setting the root pane and scene
            splashScene = new Scene(root, getSceneWidth(), getSceneHeight());

        } catch (IOException e) {
            //System.out.println(" - unable to find: " + fxmlFileLocation);
            e.printStackTrace();
        }

    }

    private void loadPrimaryScene() {

        //System.out.println("* Primary Scene");

        // set the scene
        FXMLLoader fxmlLoader = null;
        String fxmlFileLocation = "view/fxml/PrimaryScene.fxml";
        try {

            // load the FXML
            //System.out.println(" - loading the fxml: " + fxmlFileLocation);
            fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFileLocation));
            Parent root = (Parent) fxmlLoader.load();

            // pass parameters to new controller
            //System.out.println(" - running init process on the controller");
            primarySceneController = fxmlLoader.<PrimarySceneController>getController();
            primarySceneController.initProcess(mainController);

            // setting the root pane and scene
            primaryScene = new Scene(root, getSceneWidth(), getSceneHeight());

        } catch (IOException e) {
            //System.out.println(" - unable to find: " + fxmlFileLocation);
            e.printStackTrace();
        }

    }

    private void setUpStage() {

        // set the title
        primaryStage.setTitle(Configurations.getApplicationStageTitle());

        // set the stage1 favicon
        setStageFavicon(primaryStage);

        // setting the stage position
        //System.out.println(" - resizing stage");
        //System.out.println(" - - display width/height: " + getVisualBoundsWidth() + ", " + getVisualBoundsHeight());
        primaryStage.setX(getStagePositionX());
        primaryStage.setY(getStagePositionY());

        // resizable stage1?
        primaryStage.setResizable(true);

    }

    private void showStage() {

        if (primaryStage.isShowing() == false) {

            primaryStage.setScene(splashScene);

            // set the stage
            //System.out.println(" - showing the stage");
            primaryStage.show();
            splashSceneController.startLoadingAnimation();

            //primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            //    @Override
            //    public void handle(WindowEvent t) {
            //        Platform.exit();
            //        System.exit(0);
            //    }
            //});

        }

    }

    public void showSplashScene() {

        if (primaryStage.isShowing() == true) {

            primaryStage.setScene(splashScene);

        }

    }

    public void showPrimaryScene() {

        if (primaryStage.isShowing() == true) {

            primaryStage.setScene(primaryScene);
            primarySceneController.postShowStageInitProcess();

        }

    }

    private void setStageFavicon(Stage stage) {

        //System.out.println(" - setting the favicon");
        //InputStream iconStream = getClass().getResourceAsStream(Configurations.getApplicationFavicon());
        URL iconStream = getClass().getResource(Configurations.getApplicationFavicon());
        if (iconStream != null) {
            //System.out.println(" - - " + iconStream.toString());
            //System.out.println(" - - found image");
            Image icon = new Image(iconStream.toString());
            stage.getIcons().add(icon);
        } else {
            //System.out.println(" - - DID NOT find image");
        }

    }

    private double getVisualBoundsWidth() {
        double stageVisualBoundsWidth = Screen.getPrimary().getVisualBounds().getWidth();
        return stageVisualBoundsWidth;
    }

    private double getVisualBoundsHeight() {
        double stageVisualBoundsHeight = Screen.getPrimary().getVisualBounds().getHeight();
        return stageVisualBoundsHeight;
    }

    private double getSceneWidth() {

        double widthNew = Configurations.getApplicationWidthPreferred();
        if (widthNew > getVisualBoundsWidth()) {
            widthNew = getVisualBoundsWidth();
        }
        return widthNew;

    }

    private double getSceneHeight() {

        double heightNew = Configurations.getApplicationHeightPreferred();
        if (heightNew > getVisualBoundsHeight()) {
            heightNew = getVisualBoundsHeight();
        }
        return heightNew;

    }

    private double getStagePositionX() {

        double posX = (getVisualBoundsWidth() / 2) - (getSceneWidth() / 2);

        //System.out.println(" - - scene width: " + getSceneWidth());
        //System.out.println(" - - scene X position: " + posX);

        return posX;

    }

    private double getStagePositionY() {

        double posY = (getVisualBoundsHeight() / 2) - (getSceneHeight() / 2);

        //System.out.println(" - - scene height: " + getSceneHeight());
        //System.out.println(" - - scene Y position: " + posY);

        return posY;

    }

    public SQLiteHelper getSqLiteHelper() {
        return sqLiteHelper;
    }

}
