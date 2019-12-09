package no.nav.tps.forvalteren.service.command.foedselsmelding;

import static com.google.common.collect.Sets.newHashSet;
import static no.nav.tps.forvalteren.domain.rs.skd.AddressOrigin.FAR;
import static no.nav.tps.forvalteren.domain.rs.skd.AddressOrigin.MOR;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.BOST;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.DNR;
import static no.nav.tps.forvalteren.domain.rs.skd.IdentType.FNR;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.skd.RsTpsFoedselsmeldingRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfTechnicalException;
import no.nav.tps.forvalteren.service.command.testdata.response.lagretiltps.SendSkdMeldingTilTpsResponse;
import no.nav.tps.forvalteren.service.command.testdata.skd.SendSkdMeldingTilGitteMiljoer;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMessageCreatorTrans1;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.PersonAdresseService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.PersonhistorikkService;
import no.nav.tps.xjc.ctg.domain.s018.PersonStatus;
import no.nav.tps.xjc.ctg.domain.s018.PersonstatusType;
import no.nav.tps.xjc.ctg.domain.s018.S018PersonType;

@RunWith(MockitoJUnitRunner.class)
public class SendTpsFoedselsmeldingServiceTest {

    private static final String NAVN_FOEDSELSMELDING = "Fødselsmelding";
    private static final String IDENT_MOR = "12129012345";
    private static final String IDENT_FAR = "13118912345";

    @Mock
    private PersonhistorikkService personhistorikkService;

    @Mock
    private SkdMessageCreatorTrans1 skdMessageCreatorTrans1;

    @Mock
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilGitteMiljoer;

    @Mock
    private PersonAdresseService personAdresseService;

    @Mock
    private OpprettPersonMedEksisterendeForeldreService opprettPersonMedEksisterendeForeldreService;

    @Mock
    private SkdMeldingResolver foedselsmelding;

    @Mock
    private SkdMeldingTrans1 skdMeldingTrans1;

    @InjectMocks
    private SendTpsFoedselsmeldingService sendTpsFoedselsmeldingService;

    private RsTpsFoedselsmeldingRequest rsTpsFoedselsmeldingRequest;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        when(personhistorikkService.hentPersonhistorikk(anyString(), any(LocalDateTime.class), anyString())).thenReturn(hentPersonType());
        when(skdMessageCreatorTrans1.execute(eq(NAVN_FOEDSELSMELDING), any(Person.class), eq(true))).thenReturn(skdMeldingTrans1);
    }

    @Test
    public void tpsEndringsmeldingServiceMissingRequestParameters() {

        rsTpsFoedselsmeldingRequest = RsTpsFoedselsmeldingRequest.builder()
                .foedselsdato(LocalDateTime.now())
                .miljoer(newHashSet("u5"))
                .build();

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage("Påkrevet parameter mangler.");

        sendTpsFoedselsmeldingService.sendFoedselsmelding(rsTpsFoedselsmeldingRequest);
    }

    @Test
    public void tpsEndringsmeldingServiceAdrFraMorOk() {

        rsTpsFoedselsmeldingRequest = RsTpsFoedselsmeldingRequest.builder()
                .foedselsdato(LocalDateTime.now())
                .identMor(IDENT_MOR)
                .identtype(FNR)
                .miljoer(newHashSet("u5"))
                .build();

        when(opprettPersonMedEksisterendeForeldreService.execute(rsTpsFoedselsmeldingRequest)).thenReturn(new Person());
        when(personAdresseService.hentAdresserForDato(any(S018PersonType.class), any(LocalDateTime.class))).thenReturn(new AdresserResponse());

        sendTpsFoedselsmeldingService.sendFoedselsmelding(rsTpsFoedselsmeldingRequest);

        verify(personhistorikkService).hentPersonhistorikk(anyString(), any(LocalDateTime.class), anyString());
    }

    @Test
    public void tpsEndringsmeldingServiceAdrFraFarOk() {

        rsTpsFoedselsmeldingRequest = RsTpsFoedselsmeldingRequest.builder()
                .foedselsdato(LocalDateTime.now())
                .identMor(IDENT_MOR)
                .identFar(IDENT_FAR)
                .identtype(DNR)
                .adresseFra(FAR)
                .miljoer(newHashSet("u6"))
                .build();

        when(opprettPersonMedEksisterendeForeldreService.execute(rsTpsFoedselsmeldingRequest)).thenReturn(new Person());
        when(personAdresseService.hentAdresserForDato(any(S018PersonType.class), any(LocalDateTime.class))).thenReturn(new AdresserResponse());

        sendTpsFoedselsmeldingService.sendFoedselsmelding(rsTpsFoedselsmeldingRequest);

        verify(personhistorikkService, times(2)).hentPersonhistorikk(anyString(), any(LocalDateTime.class), anyString());
    }

    @Test
    public void tpsEndringsmeldingServiceAdrFraFarNok() {

        rsTpsFoedselsmeldingRequest = RsTpsFoedselsmeldingRequest.builder()
                .foedselsdato(LocalDateTime.now())
                .identMor(IDENT_MOR)
                .adresseFra(FAR)
                .identtype(BOST)
                .miljoer(newHashSet("u5"))
                .build();

        when(opprettPersonMedEksisterendeForeldreService.execute(rsTpsFoedselsmeldingRequest)).thenReturn(new Person());

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage("Suppler ident fra far for å kunne hente adresse fra TPS.");

        sendTpsFoedselsmeldingService.sendFoedselsmelding(rsTpsFoedselsmeldingRequest);
    }

    @Test
    public void tpsEndringsmeldingServiceAdrForelderFinnesIkke() {

        rsTpsFoedselsmeldingRequest = RsTpsFoedselsmeldingRequest.builder()
                .identMor(IDENT_MOR)
                .adresseFra(MOR)
                .foedselsdato(LocalDateTime.now())
                .identtype(FNR)
                .miljoer(newHashSet("u27"))
                .build();

        when(personhistorikkService.hentPersonhistorikk(anyString(), any(LocalDateTime.class), anyString()))
                .thenThrow(new TpsfTechnicalException("test"));

        SendSkdMeldingTilTpsResponse sendSkdMeldingTilTpsResponse = sendTpsFoedselsmeldingService.sendFoedselsmelding(rsTpsFoedselsmeldingRequest);

        assertThat(sendSkdMeldingTilTpsResponse.getStatus().get("u27"), is("FEIL: Person med ident " + IDENT_MOR + " finnes ikke i miljø u27."));
    }

    private S018PersonType hentPersonType() {

        S018PersonType s018PersonType = new S018PersonType();
        s018PersonType.setIdentType("FNR");
        PersonstatusType personstatusType = new PersonstatusType();
        personstatusType.setDatoFom("2000-01-01");
        personstatusType.setKodePersonstatus(PersonStatus.BOSA);
        s018PersonType.getPersonStatus().add(personstatusType);

        return s018PersonType;
    }
}