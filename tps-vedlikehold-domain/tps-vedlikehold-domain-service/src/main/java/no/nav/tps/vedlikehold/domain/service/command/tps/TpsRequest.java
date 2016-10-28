package no.nav.tps.vedlikehold.domain.service.command.tps;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by f148888 on 29.09.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TpsRequest {
    private String environment;

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

}
