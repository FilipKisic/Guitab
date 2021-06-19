package hr.algebra.controllers;

import hr.algebra.audio.AudioInputProcessor;
import hr.algebra.audio.InputNoteDetector;
import hr.algebra.model.Song;
import hr.algebra.xml.XmlParser;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.shape.Ellipse;

import javax.xml.bind.JAXBException;

public class MainPageController {

    private static Song selectedSong;

    public Label lblResult;
    public Button btnStart;
    public Label lblNote;
    public Label lbSongTitle;
    public ToggleButton btnPlayPause;
    public ToggleButton btnSkipToStart;
    public ToggleButton btnSkipToEnd;
    public Label lbBandName;
    public Label lbSongName;
    public Label lbDuration;
    public TextArea taTab;
    public Ellipse lowE_1;


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
            selectedSong = XmlParser.loadSongXML(songName);
        } catch (JAXBException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not find song in library.", ButtonType.OK);
            alert.showAndWait();
        }
        if (selectedSong != null)
            fillWithInfo(selectedSong);
    }

    private void fillWithInfo(Song song) {
        lbSongTitle.setText(song.getName());
        lbBandName.setText(song.getArtist() + " - " + song.getAlbum());
        lbSongName.setText(song.getName());
        lbDuration.setText("00:00/" + song.getDuration());
    }

    public void btnPlayPausePressed(ActionEvent actionEvent) {

    }

    public void btnSkipToStartPressed(ActionEvent actionEvent) {

    }

    public void btnSkipToEndPressed(ActionEvent actionEvent) {

    }
}
