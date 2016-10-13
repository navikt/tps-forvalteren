package no.nav.tps.vedlikehold.domain.service.command.tps;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by f148888 on 29.09.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TpsRequest {
    private String environment;

    private String buffNr;

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

    public String getBuffNr() {
        return buffNr;
    }

    public void setBuffNr(String buffNr) {
        this.buffNr = buffNr;
    }
}
