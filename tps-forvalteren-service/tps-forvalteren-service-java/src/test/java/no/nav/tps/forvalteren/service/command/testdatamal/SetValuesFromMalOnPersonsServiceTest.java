package no.nav.tps.forvalteren.service.command.testdatamal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.jpa.Personmal;
import no.nav.tps.forvalteren.domain.test.provider.PersonmalProvider;

@RunWith(SpringJUnit4ClassRunner.class)
public class SetValuesFromMalOnPersonsServiceTest {

    private SetValuesFromMalOnPersonsService setValuesFromMalOnPersonsService;
    private List<Person> personer;
    private List<Personmal> personmalListe;

    @Before
    public void setup() {
        setValuesFromMalOnPersonsService = new SetValuesFromMalOnPersonsService();

        personer = Arrays.asList(Person.builder().etternavn("Hansen").fornavn("Hans").ident("11111111111").build());
        personmalListe = Arrays.asList(PersonmalProvider.personmalA().antallIdenter(1).build());
    }

    @Test
    public void executeOk() {

        setValuesFromMalOnPersonsService.execute(personer, personmalListe);
        assertThat(personer.get(0).getSivilstand(), is(equalTo(personmalListe.get(0).getSivilstand())));
        assertThat(toLocalDate(personer.get(0).getDoedsdato()), is(equalTo(personmalListe.get(0).getDoedsdato())));
        assertThat(personer.get(0).getStatsborgerskap().get(0).getStatsborgerskap(), is(equalTo(personmalListe.get(0).getStatsborgerskap())));
        assertThat(toLocalDate(personer.get(0).getStatsborgerskap().get(0).getStatsborgerskapRegdato()), is(equalTo(personmalListe.get(0).getStatsborgerskapRegdato())));
        assertThat(personer.get(0).getSpesreg(), is(equalTo(personmalListe.get(0).getSpesreg())));
        assertThat(toLocalDate(personer.get(0).getSpesregDato()), is(equalTo(personmalListe.get(0).getSpesregDato())));
        assertThat(toLocalDate(personer.get(0).getEgenAnsattDatoFom()), is(equalTo(personmalListe.get(0).getEgenAnsattDatoFom())));
        assertThat(toLocalDate(personer.get(0).getEgenAnsattDatoTom()), is(equalTo(personmalListe.get(0).getEgenAnsattDatoTom())));
        assertThat(personer.get(0).getInnvandretFraLand(), is(equalTo(personmalListe.get(0).getInnvandretFraLand())));
        assertThat(toLocalDate(personer.get(0).getInnvandretFraLandFlyttedato()), is(equalTo(personmalListe.get(0).getInnvandretFraLandFlyttedato())));
        assertThat(toLocalDate(personer.get(0).getInnvandretFraLandRegdato()), is(equalTo(personmalListe.get(0).getInnvandretFraLandRegdato())));
        assertThat(toLocalDate(personer.get(0).getSikkerhetsTiltakDatoFom()), is(equalTo(personmalListe.get(0).getSikkerhetsTiltakDatoFom())));
        assertThat(toLocalDate(personer.get(0).getSikkerhetsTiltakDatoTom()), is(equalTo(personmalListe.get(0).getSikkerhetsTiltakDatoTom())));
        assertThat(personer.get(0).getBeskrSikkerhetsTiltak(), is(equalTo(personmalListe.get(0).getBeskrSikkerhetsTiltak())));
    }

    private LocalDate toLocalDate(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.toLocalDate() : null;
    }
}