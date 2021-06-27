package hr.algebra.utils;

public class Stopwatch implements Runnable {

    private int currentSeconds = 0;
    private int currentMinutes = 0;
    private final int totalSeconds;
    private boolean doStop = false;
    private boolean doReset = true;

    public Stopwatch(int maxMinutes, int maxSeconds) {
        this.totalSeconds = (maxMinutes * 60) + maxSeconds;
    }

    public Stopwatch(String duration) {
        String[] split = duration.split(":");
        int minutes = Integer.parseInt(split[0]);
        int seconds = Integer.parseInt(split[1]);
        this.totalSeconds = (minutes * 60) + seconds;
    }

    @Override
    public void run() {
        if (doReset) {
            reset();
        }
        while (keepTicking() && currentSeconds < totalSeconds) {
            tick();
            printTime();
        }
    }

    private void printTime() {
        System.out.println(currentMinutes + ":" + currentSeconds % 60);
    }

    private synchronized boolean keepTicking() {
        return !doStop;
    }

    public synchronized void tick() {
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        currentSeconds++;
        if (currentSeconds % 60 == 0) {
            currentMinutes++;
        }
    }

    public synchronized void stopTicking() {
        doStop = true;
    }

    public synchronized void resume() {
        doStop = false;
        run();
    }

    private void reset() {
        doReset = false;
        currentSeconds = 0;
        currentMinutes = 0;
    }
}
