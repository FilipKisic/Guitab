package hr.algebra.audio;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.filters.HighPass;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.io.jvm.AudioPlayer;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import javafx.application.Platform;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;

public class AudioInputProcessor {
    private AudioDispatcher audioDispatcher;
    public static int currentFrequency;
    private final Runnable frequencySetterMethodReference;

    public AudioInputProcessor(Runnable frequencySetterMethodReference) {
        this.frequencySetterMethodReference = frequencySetterMethodReference;
    }

    public void processInput() {
        configAudioDispatcher();
        PitchDetectionHandler pitchDetectionHandler = this::handlePitch;
        PitchProcessor.PitchEstimationAlgorithm algorithm = PitchProcessor.PitchEstimationAlgorithm.YIN;
        AudioProcessor pitchEstimator = new PitchProcessor(algorithm, 16000, 1024, pitchDetectionHandler);
        audioDispatcher.addAudioProcessor(pitchEstimator);
        playbackAudioInput();
        audioDispatcher.run();
    }

    private void playbackAudioInput() {
        try {
            audioDispatcher.addAudioProcessor(new AudioPlayer(new AudioFormat(16000, 16, 1, true, true)));
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void configAudioDispatcher() {
        try {
            audioDispatcher = AudioDispatcherFactory.fromDefaultMicrophone(16000, 1024, 0);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        AudioProcessor highPass = new HighPass(70, 16000);
        audioDispatcher.addAudioProcessor(highPass);
    }

    private void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
        if (pitchDetectionResult.getPitch() != -1.0) {
            currentFrequency = Math.round(pitchDetectionResult.getPitch());
            setCurrentFrequency();
        }
    }

    private void setCurrentFrequency() {
        try {
            Thread.sleep(5); //provide enough time to set variables
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(frequencySetterMethodReference);
    }
}
