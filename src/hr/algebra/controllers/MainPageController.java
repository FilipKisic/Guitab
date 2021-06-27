package hr.algebra.controllers;

import hr.algebra.audio.AudioInputProcessor;
import hr.algebra.audio.InputNoteDetector;
import hr.algebra.model.Song;
import hr.algebra.utils.Stopwatch;
import hr.algebra.xml.SongLoader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Ellipse;

import javax.xml.bind.JAXBException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    private static Stopwatch ticker;
    private static boolean isPlaying = false;

    public Label lblResult;
    public Button btnStart;
    public Label lblNote;
    public Label lbSongTitle;
    public Button btnPlayPause;
    public Button btnSkipToStart;
    public Button btnSkipToEnd;
    public Label lbBandName;
    public Label lbSongName;
    public Label lbDuration;
    public TextArea taTab;
    public Ellipse lowE_1;
    private Thread tickingThread;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupButtonImage("/hr/algebra/res/images/play-b.png", btnPlayPause, 40);
        setupButtonImage("/hr/algebra/res/images/start.png", btnSkipToStart, 25);
        setupButtonImage("/hr/algebra/res/images/end.png", btnSkipToEnd, 25);
    }

    private void setupButtonImage(String imageUrl, Button btnRef, double imageSize) {
        Image imgPlay = new Image(imageUrl);
        ImageView imgView = new ImageView(imgPlay);
        imgView.setFitHeight(imageSize);
        imgView.setPreserveRatio(true);
        btnRef.setGraphic(imgView);
    }

    public void btnStartPressed() {
        AudioInputProcessor aip = new AudioInputProcessor(this::updateUI);
        Thread inputThread = new Thread(aip::processInput);
        inputThread.setDaemon(true);
        inputThread.start();
    }

    private void updateUI() {
        int frequency = AudioInputProcessor.currentFrequency;
        lblResult.setText(frequency + "Hz");
        lblNote.setText(InputNoteDetector.detectNote(frequency));
    }

    public void loadSong(String songName) {
        try {
            SongLoader.loadSongXML(songName);
        } catch (JAXBException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not find song in library.", ButtonType.OK);
            alert.showAndWait();
        }
        fillWithInfo(SongLoader.loadedSong);
        initTicker();
        taTab.setText(SongLoader.parseSongToTab());
    }

    private void initTicker() {
        ticker = new Stopwatch(SongLoader.loadedSong.getDuration());
        tickingThread = new Thread(ticker);
        tickingThread.start();
        ticker.stopTicking();
    }

    private void fillWithInfo(Song song) {
        lbSongTitle.setText(song.getName());
        lbBandName.setText(song.getArtist() + " - " + song.getAlbum());
        lbSongName.setText(song.getName());
        lbDuration.setText("00:00/" + song.getDuration());
    }

    public void btnPlayPausePressed() {
        Platform.runLater(this::btnPlayPauseProcess);
    }

    private void btnPlayPauseProcess() {
        System.out.println("Button play-pause pressed!");
        isPlaying = !isPlaying;
        if (isPlaying) {
            ticker.resume();
            setupButtonImage("/hr/algebra/res/images/pause.png", btnPlayPause, 40);
        }
        else{
            ticker.stopTicking();
            setupButtonImage("/hr/algebra/res/images/play-b.png", btnPlayPause, 40);
        }
    }

    public void btnSkipToStartPressed() {
        System.out.println("Skip to Start pressed!");
    }

    public void btnSkipToEndPressed() {
        System.out.println("Skip to End pressed!");
    }

    public void stopApplication() {
        Platform.exit();
    }
}
