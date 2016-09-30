package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;

/**
 * Created by f148888 on 30.09.2016.
 */

// Er vel egentlig "tpsRequestServiceRoutine"
@JacksonXmlRootElement(localName = "tpsServiceRutine")
public class TpsSokPersonRequestServiceRoutine extends TpsRequestServiceRoutine {

    private String navn;
    private String etternavn;
    private String fornavn;

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public void setEtternavn(String etternavn) {
        this.etternavn = etternavn;
    }

    public String getFornavn() {
        return fornavn;
    }

    public void setFornavn(String fornavn) {
        this.fornavn = fornavn;
    }
}
