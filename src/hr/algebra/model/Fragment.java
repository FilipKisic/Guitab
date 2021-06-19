package hr.algebra.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Fragment {
    @XmlElement(name = "string")
    private String string;

    @XmlElement(name = "fret")
    private Integer fret;

    @XmlElement(name = "frequency")
    private Integer frequency;

    @XmlElement(name = "time")
    private String time;
}
