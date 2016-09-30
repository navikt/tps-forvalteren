package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@JacksonXmlRootElement(localName = "tpsServiceRutine")
public class TpsHentPersonRequestServiceRoutine extends TpsRequestServiceRoutine {

    private String fnr;

    public String getFnr() {
        return fnr;
    }

    public void setFnr(String fnr) {
        this.fnr = fnr;
    }

}
