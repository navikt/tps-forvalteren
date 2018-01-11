package no.nav.tps.forvalteren.service.command.testdata.opprett.implementation;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetDummyAdresseOnPersons;

@Service
public class DefaultSetDummyAdresseOnPersons implements SetDummyAdresseOnPersons {

    private static final LocalDateTime NOW = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
    private static final String GATEADRESSE = "SANNERGATA";
    private static final String HUSNR = "2";
    private static final String POSTNR = "0557";
    private static final String GATEKODE = "16188";
    private static final String KOMMUNENR = "0301";

    @Override
    public void execute(List<Person> persons) {
        for (Person person : persons) {
            Gateadresse adresse = new Gateadresse();
            adresse.setHusnummer(HUSNR);
            adresse.setGatekode(GATEKODE);
            adresse.setKommunenr(KOMMUNENR);
            adresse.setAdresse(GATEADRESSE);
            adresse.setPostnr(POSTNR);
            adresse.setFlyttedato(NOW);
            adresse.setPerson(person);
            person.setBoadresse(adresse);
        }
    }

}
