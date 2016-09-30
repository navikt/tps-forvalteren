package no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.requests;

import no.nav.tps.vedlikehold.domain.service.command.tps.TpsRequest;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by f148888 on 29.09.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TpsRequestEndringsmelding extends TpsRequest {
    private String xmlEndringsMeldingTag;
    private String brukerID;
    private String kilde;

    public String getBrukerID() {
        return brukerID;
    }

    public void setBrukerID(String brukerID) {
        this.brukerID = brukerID;
    }

    public String getKilde() {
        return kilde;
    }

    public void setKilde(String kilde) {
        this.kilde = kilde;
    }

    public String getXmlEndringsMeldingTag() {
        return xmlEndringsMeldingTag;
    }

    public void setXmlEndringsMeldingTag(String xmlEndringsMeldingTag) {
        this.xmlEndringsMeldingTag = xmlEndringsMeldingTag;
    }
}
