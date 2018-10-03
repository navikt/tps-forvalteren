package no.nav.tps.forvalteren.service.command.foedselsmelding;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.skd.RsTpsFoedselsmeldingRequest;
import no.nav.tps.forvalteren.service.command.testdata.opprett.EkstraherIdenterFraTestdataRequests;
import no.nav.tps.forvalteren.service.command.testdata.opprett.OpprettPersonerService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.PersonNameService;
import no.nav.tps.forvalteren.service.command.testdata.opprett.TestdataIdenterFetcher;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetRandomAdresseOnPersons;
import no.nav.tps.forvalteren.service.command.testdata.utils.HentIdenttypeFraIdentService;

@RunWith(MockitoJUnitRunner.class)
public class OpprettPersonMedEksisterendeForeldreServiceTest {

    private static final String IDENT_MOR = "12129012345";
    private static final String IDENT_FAR = "13118912345";

    @Mock
    private TestdataIdenterFetcher testdataIdenterFetcher;

    @Mock
    private EkstraherIdenterFraTestdataRequests ekstraherIdenterFraTestdataRequests;

    @Mock
    private OpprettPersonerService opprettPersonerService;

    @Mock
    private PersonNameService personNameService;

    @Mock
    private SetRandomAdresseOnPersons randomAdresseOnPerson;

    @Mock
    private HentIdenttypeFraIdentService hentIdenttypeFraIdentService;

    @InjectMocks
    private OpprettPersonMedEksisterendeForeldreService opprettPersonService;

    private RsTpsFoedselsmeldingRequest rsTpsFoedselsmeldingRequest;

    @Before
    public void setup() {
        when(opprettPersonerService.execute(anyCollection())).thenReturn(Arrays.asList(new Person()));
    }

    @Test
    public void genererPersonMedRelasjonOK() throws Exception {

        rsTpsFoedselsmeldingRequest = RsTpsFoedselsmeldingRequest.builder()
                .foedselsdato(LocalDateTime.now())
                .identMor(IDENT_MOR)
                .identFar(IDENT_FAR)
                .miljoe("u6")
                .build();

        Person result = opprettPersonService.execute(rsTpsFoedselsmeldingRequest);

        assertThat(result.getRelasjoner().get(0).getPerson().getIdent(), is(equalTo(IDENT_MOR)));
        assertThat(result.getRelasjoner().get(1).getPerson().getIdent(), is(equalTo(IDENT_FAR)));
    }
}