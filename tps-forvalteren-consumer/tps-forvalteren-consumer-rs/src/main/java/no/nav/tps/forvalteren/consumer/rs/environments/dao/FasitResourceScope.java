package no.nav.tps.forvalteren.consumer.rs.environments.dao;

import lombok.Data;

@Data
public class FasitResourceScope {

    private String environmentclass;
    private String zone;
    private String environment;
    private String application;
}
