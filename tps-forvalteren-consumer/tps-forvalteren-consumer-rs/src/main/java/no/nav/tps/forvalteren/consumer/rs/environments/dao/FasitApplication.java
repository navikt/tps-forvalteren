package no.nav.tps.forvalteren.consumer.rs.environments.dao;

import java.util.List;

import lombok.Data;

@Data
public class FasitApplication {

    private String id;
    private String name;
    private String application;
    private String environment;
    private String version;
    private String selftest;
    private List<FasitUsedResources> usedresources;

}