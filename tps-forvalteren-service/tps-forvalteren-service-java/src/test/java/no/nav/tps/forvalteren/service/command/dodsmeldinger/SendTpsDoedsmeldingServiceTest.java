package no.nav.tps.forvalteren.service.command.dodsmeldinger;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Map;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.skd.DoedsmeldingHandlingType;
import no.nav.tps.forvalteren.domain.rs.skd.RsTpsDoedsmeldingRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import no.nav.tps.forvalteren.service.command.exceptions.TpsfFunctionalException;
import no.nav.tps.forvalteren.service.command.testdata.skd.SendSkdMeldingTilGitteMiljoer;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMessageCreatorTrans1;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.PersonAdresseService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.PersonstatusService;
import no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.utils.ConvertDateToString;
import no.nav.tps.xjc.ctg.domain.s004.PersondataFraTpsS004;

@RunWith(MockitoJUnitRunner.class)
public class SendTpsDoedsmeldingServiceTest {

    private static final String IDENT = "12345678901";
    private static final String MILJOE = "u2";
    
    @Mock
    private SkdMessageCreatorTrans1 skdCreator;

    @Mock
    private SendSkdMeldingTilGitteMiljoer sendSkdMeldingTilMiljoe;

    @Mock
    private SkdMeldingResolver doedsmelding;

    @Mock
    private SkdMeldingResolver doedsmeldingAnnuller;

    @Mock
    private PersonstatusService personstatusService;

    @Mock
    private PersonAdresseService personAdresseService;

    @InjectMocks
    private SendTpsDoedsmeldingService sendTpsDoedsmeldingService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private PersondataFraTpsS004 persondataFraTps;

    @Mock
    private Map<String, String> sendStatus;

    @Mock
    private SkdMeldingTrans1 skdMeldingTrans1;

    @Before
    public void setup() {
        when(personstatusService.hentPersonstatus(IDENT, MILJOE)).thenReturn(persondataFraTps);

        when(skdCreator.execute(anyString(), any(Person.class), anyBoolean())).thenReturn(skdMeldingTrans1);
        when(sendSkdMeldingTilMiljoe.execute(anyString(), any(TpsSkdRequestMeldingDefinition.class), anySet())).thenReturn(sendStatus);
    }

    @Test
    public void sendDoedsmeldingValidateParamsError() {
        RsTpsDoedsmeldingRequest request = new RsTpsDoedsmeldingRequest();

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage("Påkrevet parameter mangler.");
        sendTpsDoedsmeldingService.sendDoedsmelding(request);
    }

    @Test
    public void sendAnnuleringPersonErIkkeDoed() {

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage("Personen med ident " + IDENT + " er ikke død i miljø " + MILJOE + ".");

        sendTpsDoedsmeldingService.sendDoedsmelding(buildRequest(DoedsmeldingHandlingType.D));
    }

    @Test
    public void sendAnnuleringOK() {

        when(persondataFraTps.getDatoDo()).thenReturn(ConvertDateToString.yyyysMMsdd(LocalDateTime.now()));

        sendTpsDoedsmeldingService.sendDoedsmelding(buildRequest(DoedsmeldingHandlingType.D));

        verify(personstatusService).hentPersonstatus(IDENT, MILJOE);
        verify(sendSkdMeldingTilMiljoe).execute(anyString(), any(TpsSkdRequestMeldingDefinition.class), anySet());
    }

    @Test
    public void sendDoedsmeldingPersonErAlleredeDoed() {

        when(persondataFraTps.getDatoDo()).thenReturn(ConvertDateToString.yyyysMMsdd(LocalDateTime.now()));

        expectedException.expect(TpsfFunctionalException.class);
        expectedException.expectMessage("Personen med ident " + IDENT + " er allerede død i miljø " + MILJOE + ".");
        sendTpsDoedsmeldingService.sendDoedsmelding(buildRequest(DoedsmeldingHandlingType.C));
    }

    @Test
    public void sendDoedsmeldingOK() {

        when(persondataFraTps.getDatoDo()).thenReturn(null);

        sendTpsDoedsmeldingService.sendDoedsmelding(buildRequest(DoedsmeldingHandlingType.C));

        verify(personstatusService).hentPersonstatus(IDENT, MILJOE);
        verify(sendSkdMeldingTilMiljoe).execute(anyString(), any(TpsSkdRequestMeldingDefinition.class), anySet());
    }

    private RsTpsDoedsmeldingRequest buildRequest(DoedsmeldingHandlingType handling) {
        return RsTpsDoedsmeldingRequest.builder()
                .doedsdato(LocalDateTime.now())
                .ident(IDENT)
                .miljoe(MILJOE)
                .handling(handling)
                .build();
    }
}