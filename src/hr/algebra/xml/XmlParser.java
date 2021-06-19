package hr.algebra.xml;

import hr.algebra.model.Song;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XmlParser {

    public static Song loadSongXML(String songName) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Song.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (Song) unmarshaller.unmarshal(new File("songs/" + songName.toLowerCase().trim() + ".xml"));
    }
}
