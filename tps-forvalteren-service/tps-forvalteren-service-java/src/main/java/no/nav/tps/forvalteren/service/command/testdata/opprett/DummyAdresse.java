package no.nav.tps.forvalteren.service.command.testdata.opprett;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;

public final class DummyAdresse {

    private static final String GATEADRESSE = "SANNERGATA";
    private static final String HUSNR = "2";
    private static final String POSTNR = "0557";
    private static final String GATEKODE = "16188";
    private static final String KOMMUNENR = "0301";
    private static final Gateadresse ADRESSE;

    private DummyAdresse() {
    }

    static {
        ADRESSE =
                Gateadresse.builder()
                        .husnummer(HUSNR)
                        .gatekode(GATEKODE)
                        .adresse(GATEADRESSE)
                        .build();
        ADRESSE.setPostnr(POSTNR);
        ADRESSE.setKommunenr(KOMMUNENR);
    }

    public static Adresse create() {
        return ADRESSE;
    }
}
