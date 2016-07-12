package no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.temp;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Øyvind Grimnes, Visma Consulting AS on 12.07.2016.
 */
public class ServiceRequest {

    @JsonProperty("fnr")
    private String identifier;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
