package hr.algebra.utils;

import hr.algebra.xml.SongLoader;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.function.Consumer;


public class Stopwatch implements Runnable {

    private Timeline timeline;
    private int currentSeconds = 0;
    private final int totalSeconds;
    private boolean doStop = false;
    private boolean doReset = true;
    private final Consumer<Integer> updateUIConsumer;
    private final Runnable methodReference;

    public Stopwatch(String duration, Consumer<Integer> updateUIConsumer, Runnable methodReference) {
        String[] split = duration.split(":");
        int minutes = Integer.parseInt(split[0]);
        int seconds = Integer.parseInt(split[1]);
        this.totalSeconds = (minutes * 60) + seconds;
        this.updateUIConsumer = updateUIConsumer;
        this.methodReference = methodReference;
    }

    @Override
    public void run() {
        if (doReset)
            reset();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (keepTicking() && currentSeconds < totalSeconds) {
                tick();
            } else if (currentSeconds == totalSeconds) {
                stopTicking();
                updateUIConsumer.accept(totalSeconds);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private synchronized boolean keepTicking() {
        return !doStop;
    }

    public synchronized void tick() {
        GridView.cleanFingers();
        currentSeconds++;
        updateUIConsumer.accept(currentSeconds);
        GridView.showFingerOnFret(SongLoader.timeEllipseMap.get(currentSeconds));
        methodReference.run();
    }

    public synchronized void stopTicking() {
        doStop = true;
        if (timeline != null)
            timeline.pause();
    }

    public synchronized void resume() {
        doStop = false;
        timeline.play();
    }

    public void reset() {
        doReset = false;
        currentSeconds = 0;
        if (timeline != null)
            timeline.playFromStart();
    }

    public void skipToEnd() {
        currentSeconds = totalSeconds;
    }

    public static Integer calculateTotalSeconds(String duration) {
        String[] split = duration.split(":");
        int minutes = Integer.parseInt(split[0]);
        int seconds = Integer.parseInt(split[1]);
        return (minutes * 60) + seconds;
    }
}
