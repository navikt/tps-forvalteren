package no.nav.tps.forvalteren.service.command.testdata.opprett;

import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Adresse;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;

@Service
public class DummyAdresseService {

    private static final String GATEADRESSE = "SANNERGATA";
    private static final String HUSNR = "2";
    private static final String POSTNR = "0557";
    private static final String GATEKODE = "16188";
    private static final String KOMMUNENR = "0301";

    public Adresse create() {
        Gateadresse gateadresse =
                Gateadresse.builder()
                        .husnummer(HUSNR)
                        .gatekode(GATEKODE)
                        .adresse(GATEADRESSE)
                        .build();
        gateadresse.setPostnr(POSTNR);
        gateadresse.setKommunenr(KOMMUNENR);

        return gateadresse;
    }
}
