package hr.algebra.controllers;

import hr.algebra.audio.AudioInputProcessor;
import hr.algebra.audio.AudioPlayer;
import hr.algebra.audio.InputNoteDetector;
import hr.algebra.model.Song;
import hr.algebra.utils.GridView;
import hr.algebra.utils.Stopwatch;
import hr.algebra.xml.SongLoader;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Ellipse;

import javax.xml.bind.JAXBException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    private static Stopwatch ticker;
    private static boolean isPlaying = false;

    public AnchorPane mainContainer;
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
    public Ellipse low_e_1;
    public Button btnChoose;

    private int currentUserFrequency;
    private Integer currentMinutes = 0;
    private Integer currentSeconds = 0;


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
        currentUserFrequency = AudioInputProcessor.currentFrequency;
        lblResult.setText(currentUserFrequency + "Hz");
        lblNote.setText(InputNoteDetector.detectNote(currentUserFrequency));
    }

    public void updateTimeUI(Integer tickingSeconds) {
        currentSeconds = tickingSeconds;
        if (currentSeconds % 60 == 0)
            currentMinutes++;
        if (currentMinutes >= 10 && currentSeconds % 60 >= 10)
            lbDuration.setText(currentMinutes + ":" + currentSeconds % 60 + "/" + SongLoader.loadedSong.getDuration());
        else if (currentMinutes >= 10 && currentSeconds % 60 < 10)
            lbDuration.setText(currentMinutes + ":0" + currentSeconds % 60 + "/" + SongLoader.loadedSong.getDuration());
        else if (currentMinutes < 10 && currentSeconds % 60 >= 10)
            lbDuration.setText("0" + currentMinutes + ":" + currentSeconds % 60 + "/" + SongLoader.loadedSong.getDuration());
        else
            lbDuration.setText("0" + currentMinutes + ":0" + currentSeconds % 60 + "/" + SongLoader.loadedSong.getDuration());
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
        GridView.initGridView(mainContainer);
    }

    private void initTicker() {
        ticker = new Stopwatch(SongLoader.loadedSong.getDuration(), this::updateTimeUI, this::checkFrequencyMatching);
        Thread tickingThread = new Thread(ticker);
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
        btnPlayPauseProcess();
    }

    private void btnPlayPauseProcess() {
        isPlaying = !isPlaying;
        if (isPlaying) {
            ticker.resume();
            AudioPlayer.play();
            setupButtonImage("/hr/algebra/res/images/pause.png", btnPlayPause, 40);
        } else {
            ticker.stopTicking();
            AudioPlayer.pause();
            setupButtonImage("/hr/algebra/res/images/play-b.png", btnPlayPause, 40);
        }
        checkFrequencyMatching();
    }

    private void checkFrequencyMatching() {
        Integer frequency = SongLoader.timeFrequencyMap.get(--currentSeconds);
        if (frequency != null && currentUserFrequency != 0) {
            if (isFrequencyInSafeRange(frequency)) {
                System.out.println("OKAY: " + frequency + "==" + currentUserFrequency);
            } else {
                System.out.println("BANANA: " + frequency + "!=" + currentUserFrequency);
            }
        }
    }

    private boolean isFrequencyInSafeRange(int frequency) {
        return frequency - 4 <= currentUserFrequency && currentUserFrequency <= frequency + 4;
    }

    public void btnSkipToStartPressed() {
        ticker.reset();
    }

    public void btnSkipToEndPressed() {
        ticker.skipToEnd();
    }

    public void stopApplication() {
        Platform.exit();
    }

    public void btnChoosePressed() {
        AudioPlayer.loadBackingTrack();
    }
}
