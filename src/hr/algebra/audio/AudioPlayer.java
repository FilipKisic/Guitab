package hr.algebra.audio;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {

    private static File selectedMp3File;
    private static Clip clip;

    public static void loadBackingTrack() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedMp3File = jfc.getSelectedFile();
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(selectedMp3File);
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
                e.printStackTrace();
            }
        }

    }

    public static void play() {
        if (selectedMp3File != null && clip != null)
            clip.start();
    }

    public static void pause() {
        if (selectedMp3File != null && clip != null)
            clip.stop();
    }

    public static void skipToStart() {
    }

    public static void skipToEnd() {
    }
}
