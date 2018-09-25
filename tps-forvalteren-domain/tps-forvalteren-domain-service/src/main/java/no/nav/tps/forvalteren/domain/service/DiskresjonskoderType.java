package no.nav.tps.forvalteren.domain.service;

public enum DiskresjonskoderType {

    VABO("Vanlig bosatt"),
    URIK("I utenrikstjeneste"),
    MILI("Militær"),
    SVAL("Svalbard"),
    KLIE("Klientadresse"),
    UFB("Uten fast bobel"),
    SPSF("Sperret adresse, strengt fortrolig"),  //kode 6
    SPFO("Sperret adresse, fortrolig"),          //kode 7
    PEND("Pendler");

    private final String navn;

    DiskresjonskoderType(final String diskresjonskodeNavn){
        navn = diskresjonskodeNavn;
    }

    public String getName() {
        return navn;
    }
}