package no.nav.tps.forvalteren.domain.service.tps.skdmelding.enums;

public enum Aarsakskode {

    VIGSEL("11"),
    INNGAAELSE_PARTNERSKAP("61");

    private final String kode;

    Aarsakskode(final String kode){
        this.kode = kode;
    }

    @Override
    public String toString(){
        return kode;
    }
}
