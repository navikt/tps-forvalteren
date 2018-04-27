package no.nav.tps.forvalteren.consumer.rs.environments.dao;

import lombok.Data;

@Data
public class FasitUsedResources {

    private long id;
    private long revision;
    private long lastchange;
    private String alias;
    private String type;
    private String ref;
    private String lastupdatedby;
}
