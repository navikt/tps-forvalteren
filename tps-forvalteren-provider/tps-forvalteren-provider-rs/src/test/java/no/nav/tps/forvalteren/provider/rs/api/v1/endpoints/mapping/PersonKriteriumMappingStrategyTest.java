package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Gateadresse;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.provider.rs.util.MapperTestUtils;
import no.nav.tps.forvalteren.service.command.testdata.opprett.DummyAdresseService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.DummyLanguageService;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;

@RunWith(MockitoJUnitRunner.class)
public class PersonKriteriumMappingStrategyTest {

    private static final LocalDateTime TIMENOW = LocalDateTime.now();
    private static final String SIKKERHETSTILTAK = "tull";
    private static final String TYPESIKKERHET = "tøys";
    private static final String SPRAK = "EN";
    private static final String STATSBORGERSKAP = "SWE";
    private static final String IDENTTYPE = "FNR";
    private static final String SPESREG = "KODE6";

    private MapperFacade mapper;

    @Mock
    private DummyAdresseService dummyAdresseService;

    @Mock
    private HentDatoFraIdentService hentDatoFraIdentService;

    @Mock
    private DummyLanguageService dummyLanguageService;

    @InjectMocks
    private PersonKriteriumMappingStrategy personKriteriumMappingStrategy;

    @Before
    public void setup() {
        mapper = MapperTestUtils.createMapperFacadeForMappingStrategy(personKriteriumMappingStrategy);
        Gateadresse gateadresse = new Gateadresse();
        gateadresse.setFlyttedato(LocalDateTime.now());
        when(dummyAdresseService.createDummyBoAdresse(any(Person.class))).thenReturn(gateadresse);
    }

    @Test
    public void matchVerificationOk() {

        RsPersonBestillingKriteriumRequest bestilling = new RsPersonBestillingKriteriumRequest();
        bestilling.setTypeSikkerhetsTiltak(TYPESIKKERHET);
        bestilling.setBeskrSikkerhetsTiltak(SIKKERHETSTILTAK);
        bestilling.setDatoSprak(TIMENOW);
        bestilling.setSprakKode(SPRAK);
        bestilling.setStatsborgerskap(STATSBORGERSKAP);
        bestilling.setStatsborgerskapRegdato(TIMENOW);
        bestilling.setIdenttype(IDENTTYPE);
        bestilling.setSpesreg(SPESREG);
        bestilling.setSpesregDato(TIMENOW);
        bestilling.setEgenAnsattDatoFom(TIMENOW);
        bestilling.setEgenAnsattDatoTom(TIMENOW);

        Person person = mapper.map(bestilling, Person.class);

        assertThat(person.getIdenttype(), is(equalTo(IDENTTYPE)));
        assertThat(person.getStatsborgerskap(), is(equalTo(STATSBORGERSKAP)));
        assertThat(person.getStatsborgerskapRegdato(), is(equalTo(TIMENOW)));
        assertThat(person.getSprakKode(), is(equalTo(SPRAK)));
        assertThat(person.getDatoSprak(), is(equalTo(TIMENOW)));
        assertThat(person.getBeskrSikkerhetsTiltak(), is(equalTo(SIKKERHETSTILTAK)));
        assertThat(person.getTypeSikkerhetsTiltak(), is(equalTo(TYPESIKKERHET)));
        assertThat(person.getSpesreg(), is(equalTo(SPESREG)));
        assertThat(person.getSpesregDato(), is(equalTo(TIMENOW)));
        assertThat(person.getEgenAnsattDatoFom(), is(equalTo(TIMENOW)));
        assertThat(person.getEgenAnsattDatoTom(), is(equalTo(TIMENOW)));
    }
}