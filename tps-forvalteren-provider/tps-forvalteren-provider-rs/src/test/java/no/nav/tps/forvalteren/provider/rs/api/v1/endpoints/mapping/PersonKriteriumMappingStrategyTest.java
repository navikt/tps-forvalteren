package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.mapping;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import ma.glasnost.orika.MapperFacade;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.dolly.RsPersonBestillingKriteriumRequest;
import no.nav.tps.forvalteren.provider.rs.util.MapperTestUtils;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentDatoFraIdentService;

@RunWith(SpringJUnit4ClassRunner.class)
public class PersonKriteriumMappingStrategyTest {

    private static final LocalDateTime TIMENOW = LocalDateTime.now();
    private static final String SIKKERHETSTILTAK = "tull";
    private static final String TYPESIKKERHET = "tøys";
    private static final String SPRAK = "EN";
    private static final String STATSBORGERSKAP = "SWE";
    private static final String IDENTTYPE = "FNR";
    private static final String SPESREG = "KODE6";

    @Mock
    private HentDatoFraIdentService hentDatoFraIdentService;

    private PersonKriteriumMappingStrategy personKriteriumMappingStrategy;

    private MapperFacade mapper;

    @Before
    public void setup() {
        personKriteriumMappingStrategy = new PersonKriteriumMappingStrategy();
        mapper = MapperTestUtils.createMapperFacadeForMappingStrategy(personKriteriumMappingStrategy);
        ReflectionTestUtils.setField(personKriteriumMappingStrategy, "hentDatoFraIdentService", hentDatoFraIdentService);
    }

    @Test
    public void matchVerificationOk() {
        Person person = mapper.map(RsPersonBestillingKriteriumRequest.builder()

                        .typeSikkerhetsTiltak(TYPESIKKERHET)
                        .beskrSikkerhetsTiltak(SIKKERHETSTILTAK)
                        .datoSprak(TIMENOW)
                        .sprakKode(SPRAK)
                        .statsborgerskap(STATSBORGERSKAP)
                        .statsborgerskapRegdato(TIMENOW)
                        .identtype(IDENTTYPE)
                        .spesreg(SPESREG)
                        .spesregDato(TIMENOW)
                        .egenAnsattDatoFom(TIMENOW)
                        .egenAnsattDatoTom(TIMENOW)
                        .build(),
                Person.class);

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