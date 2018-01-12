package no.nav.tps.forvalteren.service.command.testdata.opprett.implementation;

import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;

@RunWith(MockitoJUnitRunner.class)
public class DefaultSetDummyAdresseOnPersonsTest {

    @InjectMocks
    private DefaultSetDummyAdresseOnPersons defaultSetDummyAdresseOnPersons;

    private List<Person> persons = Arrays.asList(aMalePerson().build());

    private static final LocalDateTime FLYTTEDATO = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
    private static final String GATEADRESSE = "SANNERGATA";
    private static final String HUSNR = "2";
    private static final String POSTNR = "0557";
    private static final String GATEKODE = "16188";
    private static final String KOMMUNENR = "0301";

    @Test
    public void checkAdresse() {
        defaultSetDummyAdresseOnPersons.execute(persons);
        Gateadresse adresse = (Gateadresse) persons.get(0).getBoadresse();
        assertThat(adresse.getHusnummer(), is(HUSNR));
        assertThat(adresse.getGatekode(), is(GATEKODE));
        assertThat(adresse.getKommunenr(), is(KOMMUNENR));
        assertThat(adresse.getAdresse(), is(GATEADRESSE));
        assertThat(adresse.getPostnr(), is(POSTNR));
        assertThat(adresse.getFlyttedato(), is(FLYTTEDATO));

    }
}