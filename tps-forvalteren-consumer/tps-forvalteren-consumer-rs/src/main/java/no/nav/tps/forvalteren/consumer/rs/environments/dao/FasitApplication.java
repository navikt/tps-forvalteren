package no.nav.tps.forvalteren.consumer.rs.environments.dao;

import lombok.Data;

import java.util.List;

@Data
public class FasitApplication {

    private String id;
    private String application;
    private String environment;
    private String version;
    private String selftest;
    private List<FasitUsedResources> usedresources;

}