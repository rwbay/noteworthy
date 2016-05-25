package com.ittcapstone.control;

import com.ittcapstone.Configurations;
import com.ittcapstone.MainController;
import com.ittcapstone.model.data.Entry;
import com.ittcapstone.model.sqlite.SQLiteHelper;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;


/**
 * Controller for PrimaryScene.fxml
 *
 * @author <a href="http://rwbay.com/">Nicholas Jensen</a> on 5/24/16.
 * @version 1.0
 */
public class PrimarySceneController {

    // The programs's Main Controller
    private MainController mainController;

    // data object with the entries from the database
    ArrayList<Entry> entryList = new ArrayList<>();
    ArrayList<Integer> entryListIndex = new ArrayList<>();

    // the id of the current data entry; -1 if none
    private int currentNote = -1;

    // navigation mod Buttons EventHandlers
    EventHandler<MouseEvent> btnModCreateEventHandler;
    EventHandler<MouseEvent> btnModDeleteEventHandler;
    EventHandler<MouseEvent> btnModCancelEventHandler;
    EventHandler<MouseEvent> btnModSaveEventHandler;

    /**
     * This acts as the constructor
     */
    public void initProcess(MainController mainController) {

        System.out.println("* PrimarySceneController init process started");

        this.mainController = mainController;

        // set the logo
        setMainLogo();

        // set the title
        setTitleTexts();

        // load the data from the database
        entryList = mainController.getSqLiteHelper().queryEntries();


    }

    public void postShowStageInitProcess() {

        System.out.println("* PrimarySceneController post show stage init process started");

        // hide the flash box
        setInitialFlashBox();

        // set the initial modification buttons' states
        setInitialButtonMods();

        // hide the 'New Note' pane
        setInitialPaneNote();

        // populate the categories combo box
        setInitialPaneAllNotes();

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
    Text logoText1, logoText2;

    @FXML
    HBox flashBox;

    @FXML
    StackPane flashBoxContent;

    private void setTitleTexts() {

        String[] titles = Configurations.getApplicationName().split(" ");

        if (titles.length > 1) {
            logoText1.setText(titles[0]);
            logoText2.setText(titles[1]);
        } else {
            logoText1.setText(Configurations.getApplicationName());
            logoText2.setText("");
        }


    }

    @FXML
    VBox paneNote;

    @FXML
    ListView paneAllNotes;

    @FXML
    TextField noteTitle;

    @FXML
    TextArea noteTextarea;

    public void btnMod2CancelHandle() {
        hidePaneNote();
    }

    public void btnMod2CreateHandle() {

        SQLiteHelper sqLiteHelper = mainController.getSqLiteHelper();
        Boolean success = sqLiteHelper.queryInsert(noteTitle.getText(), noteTextarea.getText());
        if (success) {
            loadAndShowEntryList();
            for (int i = 0; i < entryList.size(); i++) {
                Entry entry = entryList.get(i);
                if (noteTitle.getText().equals(entry.getTitle()) && noteTextarea.getText().equals(entry.getText())) {

                    setCurrentNote(entry.getId());
                    updateNavigationBottom();
                    updatePaneNoteInputs();

                }
            }
        }
    }

    public void btnMod2DeleteHandle() {

        SQLiteHelper sqLiteHelper = mainController.getSqLiteHelper();
        Boolean success = sqLiteHelper.queryDelete(getCurrentNote());

        if (success) {
            setCurrentNote(-1);
            loadAndShowEntryList();
            hidePaneNote();
        }

    }

    public void btnMod2SaveHandle() {

        SQLiteHelper sqLiteHelper = mainController.getSqLiteHelper();
        Boolean success = sqLiteHelper.queryUpdate(getCurrentNote(), noteTitle.getText(), noteTextarea.getText());

        loadAndShowEntryList();

        hidePaneNote();

    }

    public void paneAllNotesHandle() {

        if (paneAllNotes.getSelectionModel().getSelectedItem() != null && paneAllNotes.getSelectionModel().getSelectedIndex() > -1) {

            setCurrentNote(entryListIndex.get(paneAllNotes.getSelectionModel().getSelectedIndex()));
            showPaneNote();
            updateNavigationBottom();
            updatePaneNoteInputs();

        }

    }

    public void btnFlashBoxCloseHandle() {
        hideFlashBox();
    }

    public void btnAboutHandle() {
        showFlashBox();
    }

    // this is the button for the new note
    public void navTopBtn2NewHandle() {
        noteTitle.setText("");
        noteTextarea.setText("");
        setCurrentNote(-1);
        showPaneNote();
    }

    private void showPaneNote() {

        btnMod1.setVisible(true);
        btnMod2.setVisible(true);

        updateNavigationBottom();

        paneNote.setOpacity(0.0);
        paneNote.setVisible(true);
        navigationBottom.setVisible(true);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(150), paneNote);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();

    }

    private void updatePaneNoteInputs() {

        for (int i = 0; i < entryList.size(); i++) {
            Entry entry = entryList.get(i);
            if (entry.getId() == getCurrentNote()) {

                noteTitle.setText(entry.getTitle());
                noteTextarea.setText(entry.getText());
                break;

            }
        }

    }

    private void updateNavigationBottom() {

        if (getCurrentNote() < 0) {
            txtMod.setText("new note");
            btnMod1.setOnMouseClicked(btnModCancelEventHandler);
            btnMod2.setOnMouseClicked(btnModCreateEventHandler);
            btnMod2.setText("create");
        } else {
            txtMod.setText("view note");
            btnMod1.setOnMouseClicked(btnModSaveEventHandler);
            btnMod2.setOnMouseClicked(btnModDeleteEventHandler);
            btnMod2.setText("delete");
        }

    }

    private void hidePaneNote() {

        btnMod2.setOnMouseClicked(null);

        txtMod.setText("view notes");
        btnMod1.setVisible(false);
        btnMod2.setVisible(false);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(150), paneNote);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.play();
        fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                paneNote.setVisible(false);
            }
        });

    }

    /**
     * @return The ID of the note in the database
     */
    public int getCurrentNote() {
        return currentNote;
    }

    /**
     * @return The ID of the note in the database
     */
    public void setCurrentNote(int currentNote) {
        this.currentNote = currentNote;
    }

    private void hideFlashBox() {

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), flashBoxContent);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        //fadeTransition.play();

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), flashBoxContent);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToY(0.01);

        ParallelTransition parallelTransition = new ParallelTransition(fadeTransition, scaleTransition);
        parallelTransition.play();
        parallelTransition.setOnFinished(event -> {
            flashBox.setVisible(false);
        });

    }

    private void showFlashBox() {

        flashBox.setVisible(true);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(150), flashBoxContent);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        //fadeTransition.play();

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), flashBoxContent);
        scaleTransition.setFromY(0.1);
        scaleTransition.setToY(1.0);

        ParallelTransition parallelTransition = new ParallelTransition(fadeTransition, scaleTransition);
        parallelTransition.play();

    }

    private void loadAndShowEntryList() {

        // load the data from the database
        entryList = mainController.getSqLiteHelper().queryEntries();

        String newItem = "";

        paneAllNotes.getItems().clear();

        entryListIndex.clear();

        for (int i = entryList.size() - 1; i > -1; i--) {

            //newItem = entryList.get(i).getTitle() + " | " + entryList.get(i).getDate();
            entryListIndex.add(entryList.get(i).getId());
            newItem = entryList.get(i).getTitle();
            paneAllNotes.getItems().add(newItem);

        }

        if (entryList.size() > 0) {
            paneAllNotes.setVisible(true);
        } else {
            paneAllNotes.setVisible(false);
        }


    }

    @FXML
    Button btnMod1, btnMod2;

    @FXML
    Text txtMod;

    @FXML
    HBox navigationBottom;

    private void setInitialFlashBox() {

        flashBoxContent.setOpacity(0.0);
        flashBox.setVisible(false);

    }

    private void setInitialPaneNote() {
        paneNote.setVisible(false);
    }

    private void setInitialPaneAllNotes() {

        loadAndShowEntryList();

        paneAllNotes.setOnMouseClicked(event -> paneAllNotesHandle());
    }

    private void setInitialButtonMods() {
        //navigationBottom.setVisible(false);
        txtMod.setText("view notes");
        btnMod1.setVisible(false);
        btnMod2.setVisible(false);

        // instantiate EventHandlers
        btnModCreateEventHandler = event -> btnMod2CreateHandle();
        btnModDeleteEventHandler = event -> btnMod2DeleteHandle();
        btnModCancelEventHandler = event -> btnMod2CancelHandle();
        btnModSaveEventHandler = event -> btnMod2SaveHandle();

    }

}
