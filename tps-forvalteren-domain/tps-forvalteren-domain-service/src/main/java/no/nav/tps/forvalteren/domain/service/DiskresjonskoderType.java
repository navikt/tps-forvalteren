package no.nav.tps.forvalteren.domain.service;

public enum DiskresjonskoderType {

    VABO("0", "Vanlig bosatt"),
    URIK("1", "I utenrikstjeneste"),
    MILI("2", "Militær"),
    SVAL("3", "Svalbard"),
    KLIE("4", "Klientadresse"),
    UFB("5", "Uten fast bobel"),
    SPSF("6", "Sperret adresse, strengt fortrolig"),
    SFU("6", "Streng fortrolig utland"),
    SPFO("7", "Sperret adresse, fortrolig"),
    PEND("8", "Pendler");

    private String navn;
    private String kodeverdi;

    DiskresjonskoderType(final String kode, final String diskresjonskodeNavn) {
        kodeverdi = kode;
        navn = diskresjonskodeNavn;
    }

    public String getName() {
        return navn;
    }

    public String getKodeverdi() {
        return kodeverdi;
    }
}