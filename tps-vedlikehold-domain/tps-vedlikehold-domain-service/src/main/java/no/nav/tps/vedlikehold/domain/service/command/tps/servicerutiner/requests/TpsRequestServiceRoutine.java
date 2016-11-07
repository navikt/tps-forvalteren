package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests;

import no.nav.tps.vedlikehold.domain.service.command.tps.TpsRequest;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TpsRequestServiceRoutine extends TpsRequest {

    private String aksjonsDato;
    private String serviceRutinenavn;

    private String aksjonsKode;
    private String aksjonsKode2;

    public void setAksjonsKode(String aksjonsKode) {
        this.aksjonsKode = aksjonsKode.substring(0, 1);
        this.aksjonsKode2 = aksjonsKode.substring(1);
    }

    public void setServiceRutinenavn(String serviceRutinenavn) {
        this.serviceRutinenavn = serviceRutinenavn;
    }

    public String getServiceRutinenavn() {
        return serviceRutinenavn;
    }

    public String getAksjonsDato() {
        return aksjonsDato;
    }

    public void setAksjonsDato(String aksjonsDato) {
        this.aksjonsDato = aksjonsDato;
    }

    public String getAksjonsKode() {
        return aksjonsKode;
    }

    public String getAksjonsKode2() {
        return aksjonsKode2;
    }
}

