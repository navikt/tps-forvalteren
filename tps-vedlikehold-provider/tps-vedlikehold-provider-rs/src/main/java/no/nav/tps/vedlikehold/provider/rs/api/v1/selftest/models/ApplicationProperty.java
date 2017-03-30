package no.nav.tps.vedlikehold.provider.rs.api.v1.selftest.models;


public class ApplicationProperty {

    private String name;
    private String value;

    public ApplicationProperty(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}