package hr.algebra.audio;

public class InputNoteDetector {

    public static String detectNote(int frequency) {
        if (isBetween(frequency, 80, 84))
            return "E low";
        else if (isBetween(frequency, 108, 112))
            return "A";
        else if (isBetween(frequency, 145, 149))
            return "D";
        else if (isBetween(frequency, 194, 198))
            return "G";
        else if (isBetween(frequency, 245, 249))
            return "B";
        else if (isBetween(frequency, 327, 331))
            return "E high";
        else
            return "_";
    }

    private static boolean isBetween(int value, int lower, int higher) {
        return lower <= value && value <= higher;
    }

}
