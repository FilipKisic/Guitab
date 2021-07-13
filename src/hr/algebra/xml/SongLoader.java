package hr.algebra.xml;

import hr.algebra.model.Fragment;
import hr.algebra.model.GuitarString;
import hr.algebra.model.Song;
import hr.algebra.utils.Stopwatch;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class SongLoader {

    public static Song loadedSong;
    private static final StringBuilder stringBuilderLowE = new StringBuilder();
    private static final StringBuilder stringBuilderA = new StringBuilder();
    private static final StringBuilder stringBuilderD = new StringBuilder();
    private static final StringBuilder stringBuilderG = new StringBuilder();
    private static final StringBuilder stringBuilderB = new StringBuilder();
    private static final StringBuilder stringBuilderHighE = new StringBuilder();
    public static Map<Integer, Integer> timeFrequencyMap;
    public static Map<Integer, String> timeEllipseMap;

    public static void loadSongXML(String songName) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Song.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        loadedSong = (Song) unmarshaller.unmarshal(new File("songs/" + songName.toLowerCase().trim() + ".xml"));
    }

    public static String parseSongToTab() {
        StringBuilder stringBuilder = new StringBuilder();
        timeFrequencyMap = new HashMap<>();
        timeEllipseMap = new HashMap<>();
        for (Fragment fragment : loadedSong.getFragments()) {
            GuitarString gs = GuitarString.valueOf(fragment.getString().toUpperCase());
            createTabColumn(gs, fragment.getFret());
            timeFrequencyMap.put(Stopwatch.calculateTotalSeconds(fragment.getTime()), fragment.getFrequency());
            timeEllipseMap.put(Stopwatch.calculateTotalSeconds(fragment.getTime()), fragment.getString() + "_" + fragment.getFret());
        }
        stringBuilder.append(stringBuilderHighE).append("\n")
                .append(stringBuilderB).append("\n")
                .append(stringBuilderG).append("\n")
                .append(stringBuilderD).append("\n")
                .append(stringBuilderA).append("\n")
                .append(stringBuilderLowE);
        return stringBuilder.toString();
    }

    private static void createTabColumn(GuitarString guitarString, Integer fret) {
        switch (guitarString) {
            case LOW_E:
                checkForFirstFret(stringBuilderLowE, fret);
                stringBuilderA.append("-");
                stringBuilderD.append("-");
                stringBuilderG.append("-");
                stringBuilderB.append("-");
                stringBuilderHighE.append("-");
                addSpaceForEverySecond();
                break;
            case A:
                stringBuilderLowE.append("-");
                checkForFirstFret(stringBuilderA, fret);
                stringBuilderD.append("-");
                stringBuilderG.append("-");
                stringBuilderB.append("-");
                stringBuilderHighE.append("-");
                addSpaceForEverySecond();
                break;
            case D:
                stringBuilderLowE.append("-");
                stringBuilderA.append("-");
                checkForFirstFret(stringBuilderD, fret);
                stringBuilderG.append("-");
                stringBuilderB.append("-");
                stringBuilderHighE.append("-");
                addSpaceForEverySecond();
                break;
            case G:
                stringBuilderLowE.append("-");
                stringBuilderA.append("-");
                stringBuilderD.append("-");
                checkForFirstFret(stringBuilderG, fret);
                stringBuilderB.append("-");
                stringBuilderHighE.append("-");
                addSpaceForEverySecond();
                break;
            case B:
                stringBuilderLowE.append("-");
                stringBuilderA.append("-");
                stringBuilderD.append("-");
                stringBuilderG.append("-");
                checkForFirstFret(stringBuilderB, fret);
                stringBuilderHighE.append("-");
                addSpaceForEverySecond();
                break;
            case HIGH_E:
                stringBuilderLowE.append("-");
                stringBuilderA.append("-");
                stringBuilderD.append("-");
                stringBuilderG.append("-");
                stringBuilderB.append("-");
                checkForFirstFret(stringBuilderHighE, fret);
                addSpaceForEverySecond();
                break;
            default:
                stringBuilderLowE.append("-");
                stringBuilderA.append("-");
                stringBuilderD.append("-");
                stringBuilderG.append("-");
                stringBuilderB.append("-");
                stringBuilderHighE.append("-");
                addSpaceForEverySecond();
                break;
        }
    }

    private static void addSpaceForEverySecond() {
        stringBuilderLowE.append("--");
        stringBuilderA.append("--");
        stringBuilderD.append("--");
        stringBuilderG.append("--");
        stringBuilderB.append("--");
        stringBuilderHighE.append("--");
    }

    private static void checkForFirstFret(StringBuilder stringBuilder, int fret) {
        if (fret == 1)
            stringBuilder.append(" ").append(fret);
        else
            stringBuilder.append(fret);
    }
}
