
package no.nav.tps.forvalteren.service.command.tps.servicerutiner.response.S051;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for SRnavn.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SRnavn">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="FS03-ADRSNAVN-ADRSDATA-O"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SRnavn", namespace = "http://www.rtv.no/NamespaceTPS")
@XmlEnum
public enum SRnavn {

    @XmlEnumValue("FS03-ADRSNAVN-ADRSDATA-O")
    FS_03_ADRSNAVN_ADRSDATA_O("FS03-ADRSNAVN-ADRSDATA-O");
    private final String value;

    SRnavn(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SRnavn fromValue(String v) {
        for (SRnavn c: SRnavn.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
