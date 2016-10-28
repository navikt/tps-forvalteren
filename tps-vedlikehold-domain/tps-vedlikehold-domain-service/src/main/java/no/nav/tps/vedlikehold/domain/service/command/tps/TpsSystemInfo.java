package no.nav.tps.vedlikehold.domain.service.command.tps;

import com.fasterxml.jackson.xml.annotate.JacksonXmlProperty;
import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;

/**
 * Created by F148888 on 28.10.2016.
 */

@JacksonXmlRootElement(localName = "systemInfo")
public class TpsSystemInfo {
    private String kilde;
    private String brukerID;

    public TpsSystemInfo(String kilde, String brukerID){
        this.kilde = kilde;
        this.brukerID = brukerID;
    }

    public String getKilde() {
        return kilde;
    }

    public void setKilde(String kilde) {
        this.kilde = kilde;
    }

    public String getBrukerID() {
        return brukerID;
    }

    public void setBrukerID(String brukerID) {
        this.brukerID = brukerID;
    }
}
