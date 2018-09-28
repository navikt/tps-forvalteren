package no.nav.tps.forvalteren.domain.rs.skd;

public enum DoedsmeldingHandlingType {

    C("Send dødsmelding"),
    U("Endre dødsmelding"),
    D("Annuler");

    private String name;

    DoedsmeldingHandlingType(String name) {
        this.name = name;
    }
}