package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TpsRequest {
    private String environment;
    private String serviceRutinenavn;

    private String aksjonsKode;
    private String aksjonsKode2;

    public void setAksjonsKode(String aksjonsKode) {
        this.aksjonsKode = aksjonsKode.substring(0,1);
        this.aksjonsKode2 = aksjonsKode.substring(1);
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getAksjonsKode() {
        return aksjonsKode;
    }

    public String getAksjonsKode2() {
        return aksjonsKode2;
    }

    public void setServiceRutinenavn(String serviceRutinenavn) {
        this.serviceRutinenavn = serviceRutinenavn;
    }

    public String getServiceRutinenavn() {
        return serviceRutinenavn;
    }
}
