package no.nav.tps.forvalteren.domain.service;

public enum RelasjonType {

    MOR("MOR"),
    FAR("FAR"),
    BARN("BARN"),
    FOEDSEL("FOEDSEL"),
    EKTEFELLE("EKTEFELLE"),
    PARTNER("PARTNER");

    private final String navn;

    RelasjonType(final String relasjonNavn){
        navn = relasjonNavn;
    }

    public String getName() {
        return navn;
    }
}
