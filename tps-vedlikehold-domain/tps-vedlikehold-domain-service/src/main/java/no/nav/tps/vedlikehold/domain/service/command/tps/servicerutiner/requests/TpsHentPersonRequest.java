package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests;

import com.fasterxml.jackson.xml.annotate.JacksonXmlRootElement;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@JacksonXmlRootElement(localName = "tpsServiceRutine")
public class TpsHentPersonRequest extends TpsRequest {

    private String aksjonsDato;
    private String fnr;

    public String getAksjonsDato() {
        return aksjonsDato;
    }

    public void setAksjonsDato(String aksjonsDato) {
        this.aksjonsDato = aksjonsDato;
    }

    public String getFnr() {
        return fnr;
    }

    public void setFnr(String fnr) {
        this.fnr = fnr;
    }

}
