package hr.algebra.model;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@XmlRootElement(name = "song")
@XmlAccessorType(XmlAccessType.FIELD)
public class Song {

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "artist")
    private String artist;

    @XmlElement(name = "album")
    private String album;

    @XmlElement(name = "duration")
    private String duration;

    @XmlElementWrapper(name = "tab")
    @XmlElement(name = "fragment")
    private List<Fragment> fragments;
}
