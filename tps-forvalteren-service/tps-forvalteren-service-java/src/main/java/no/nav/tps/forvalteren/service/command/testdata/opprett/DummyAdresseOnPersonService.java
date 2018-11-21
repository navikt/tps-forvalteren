package no.nav.tps.forvalteren.service.command.testdata.opprett;

import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;

@Service
public class DummyAdresseOnPersonService {

    private static final String GATEADRESSE = "SANNERGATA";
    private static final String HUSNR = "2";
    private static final String POSTNR = "0557";
    private static final String GATEKODE = "16188";
    private static final String KOMMUNENR = "0301";

    public void execute(Person person) {
        Gateadresse gateadresse =
                Gateadresse.builder()
                        .husnummer(HUSNR)
                        .gatekode(GATEKODE)
                        .adresse(GATEADRESSE)
                        .build();
        gateadresse.setPostnr(POSTNR);
        gateadresse.setKommunenr(KOMMUNENR);
        person.setBoadresse(gateadresse);
    }
}
