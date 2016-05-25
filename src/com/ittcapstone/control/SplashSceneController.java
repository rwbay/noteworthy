package com.ittcapstone.control;

import com.ittcapstone.Configurations;
import com.ittcapstone.MainController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;

/**
 * Controller for SplashScene.fxml
 *
 * @author <a href="http://rwbay.com/">Nicholas Jensen</a> on 4/29/16.
 */
public class SplashSceneController {

    // The programs's Main Controller
    private MainController mainController;

    /**
     * This acts as the constructor
     */
    public void initProcess(MainController mainController) {

        System.out.println("* SplashSceneController init process started");

        this.mainController = mainController;

        // set the logo
        setMainLogo();

        // set the title
        setTitleTexts();

    }

    @FXML
    ImageView logo;

    private void setMainLogo() {

        System.out.println("* Looking for logo at:");
        System.out.println("- " + Configurations.getApplicationCompanyLogo());
        URL iconStream = MainController.class.getResource(Configurations.getApplicationCompanyLogo());
        if (iconStream != null) {
            logo.setVisible(true);
            System.out.println(" - - found image");
            System.out.println(" - - " + iconStream.toString());
            Image imageLogo = new Image(iconStream.toString());
            logo.setImage(imageLogo);
        } else {
            System.out.println(" - - DID NOT find logo");
            logo.setVisible(false);
        }

    }

    @FXML
    Text titleText1, titleText2;

    private void setTitleTexts() {

        String[] titles = Configurations.getApplicationName().split(" ");

        if (titles.length > 1) {
            titleText1.setText(titles[0]);
            titleText2.setText(titles[1]);
        } else {
            titleText1.setText(Configurations.getApplicationName());
            titleText2.setText("");
        }


    }

    private void showPrimaryScene() {

        mainController.showPrimaryScene();

    }

    @FXML
    Text txtLoadingL, txtLoadingO, txtLoadingA, txtLoadingD, txtLoadingI, txtLoadingN, txtLoadingG;
    ArrayList<Text> textLoading = new ArrayList<>();

    @FXML
    ProgressBar progressBar;

    public void startLoadingAnimation() {

        System.out.println("* startLoadingAnimation");

        // list of all letters in the animation
        textLoading.add(txtLoadingL);
        textLoading.add(txtLoadingO);
        textLoading.add(txtLoadingA);
        textLoading.add(txtLoadingD);
        textLoading.add(txtLoadingI);
        textLoading.add(txtLoadingN);
        textLoading.add(txtLoadingG);

        // turn off all text elements
        for (int i = 0; i < textLoading.size(); i++) {
            dimLoadingAnimation(textLoading.get(i));
        }

        // start the progress bar
        progressBar.setProgress(0.0);
        startProgressBar(300);

        // start thread for to animate
        createLoadingAnimationTask(300);

    }

    private void startProgressBar(int timeToSleepInMillis) {

        Task task = new Task() {

            @Override
            protected Object call() throws Exception {

                if (mainController.isStageShowing()) {

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            progressBar.setProgress(progressBar.getProgress() + 0.10);

                        }
                    });

                    if (progressBar.getProgress() <= 1.0) {

                        Thread.sleep(timeToSleepInMillis);

                        // start thread for to animate
                        startProgressBar(timeToSleepInMillis);

                    } else {

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {

                                showPrimaryScene();

                            }
                        });

                    }

                }

                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();

    }

    private void dimLoadingAnimation(Text textToAnimate) {
        textToAnimate.setOpacity(0.0);
    }

    private void createLoadingAnimationTask(int timeToSleepInMillis) {

        Task task = new Task() {

            @Override
            protected Object call() throws Exception {

                Thread.sleep(timeToSleepInMillis);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {


                        // animate the text elements
                        int delayBetweenLetters = 200;
                        int delayCounter = 0;
                        for (int i = 0; i < textLoading.size(); i++) {
                            playLoadingAnimationTimeline(textLoading.get(i), delayCounter);
                            delayCounter += delayBetweenLetters;
                        }


                    }
                });
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();

    }


    private void playLoadingAnimationTimeline(Text textToAnimate, int delayInMillis) {

        Timeline timelineLoading = new Timeline();

        KeyValue keyValue1 = new KeyValue(textToAnimate.opacityProperty(), 0.0);
        KeyFrame keyFrame1 = new KeyFrame(Duration.millis(0), keyValue1);

        KeyValue keyValue2 = new KeyValue(textToAnimate.opacityProperty(), 0.5);
        KeyFrame keyFrame2 = new KeyFrame(Duration.millis(100), keyValue2);


        KeyValue keyValue3 = new KeyValue(textToAnimate.scaleXProperty(), 1.5);
        KeyFrame keyFrame3 = new KeyFrame(Duration.millis(200), keyValue3);

        KeyValue keyValue4 = new KeyValue(textToAnimate.scaleXProperty(), 1.0);
        KeyFrame keyFrame4 = new KeyFrame(Duration.millis(400), keyValue4);

        KeyValue keyValue5 = new KeyValue(textToAnimate.opacityProperty(), 1.0);
        KeyFrame keyFrame5 = new KeyFrame(Duration.millis(400), keyValue5);

        timelineLoading.getKeyFrames().add(keyFrame1);
        timelineLoading.getKeyFrames().add(keyFrame2);
        timelineLoading.getKeyFrames().add(keyFrame3);
        timelineLoading.getKeyFrames().add(keyFrame4);
        timelineLoading.getKeyFrames().add(keyFrame5);

        timelineLoading.setDelay(Duration.millis(delayInMillis));
        timelineLoading.play();

    }

}
