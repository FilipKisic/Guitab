package hr.algebra.utils;

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

    public Stopwatch(String duration, Consumer<Integer> updateUIConsumer) {
        String[] split = duration.split(":");
        int minutes = Integer.parseInt(split[0]);
        int seconds = Integer.parseInt(split[1]);
        this.totalSeconds = (minutes * 60) + seconds;
        this.updateUIConsumer = updateUIConsumer;
    }

    @Override
    public void run() {
        if (doReset) {
            reset();
        }
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
        currentSeconds++;
        updateUIConsumer.accept(currentSeconds);
    }

    public synchronized void stopTicking() {
        System.out.println("STOPPED");
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
}
