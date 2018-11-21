package no.nav.tps.forvalteren.service.command.testdata.skd;

import static no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsResponseMappingUtils.STATUS_KEY;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreEgenansattRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreSikkerhetstiltakRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.domain.service.user.User;
import no.nav.tps.forvalteren.service.command.testdata.EndreSpraakkodeService;
import no.nav.tps.forvalteren.service.command.testdata.OpprettEgenAnsattMelding;
import no.nav.tps.forvalteren.service.command.testdata.OpprettSikkerhetstiltakMelding;
import no.nav.tps.forvalteren.service.command.testdata.utils.TpsPacemaker;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsRequestSender;
import no.nav.tps.forvalteren.service.user.UserContextHolder;

@RunWith(MockitoJUnitRunner.class)
public class SendNavEndringsmeldingerTest {

    @Mock
    private OpprettEgenAnsattMelding opprettEgenAnsattMelding;

    @Mock
    private OpprettSikkerhetstiltakMelding opprettSikkerhetstiltakMelding;

    @Mock
    private EndreSpraakkodeService endreSpraakkodeService;

    @Mock
    private UserContextHolder userContextHolder;

    @Mock
    private TpsNavEndringsMelding tpsNavEndringsMelding;

    @Mock
    private TpsServiceRoutineRequest tpsServiceRoutineRequest;

    @Mock
    private TpsRequestSender tpsRequestSender;

    @Mock
    private TpsRequestContext tpsRequestContext;

    @Mock
    private TpsServiceRoutineResponse tpsServiceRoutineResponse;

    @Mock
    private TpsPacemaker tpsPacemaker;

    @InjectMocks
    private SendNavEndringsmeldinger sendNavEndringsmeldinger;

    private Person testPerson1 = new Person();
    private Person testPerson2 = new Person();
    List<Person> testPersonerListe = new ArrayList<>();
    
    private Set<String> environments;

    @Before
    public void setup() {
        
        testPersonerListe.add(testPerson1);
        testPersonerListe.add(testPerson2);

        tpsServiceRoutineRequest = new TpsServiceRoutineRequest();

        environments = new HashSet<String>();
        environments.add("u5");
        environments.add("u6");
    
        setupMocks();
    }
    
    private void setupMocks() {
        when(userContextHolder.getUser()).thenReturn(new User("Z111111", "Z111111"));
    
        LinkedHashMap xmlAsMap = new LinkedHashMap();
        xmlAsMap.put(STATUS_KEY, new ResponseStatus("00","melding","utfyllende melding"));
        when(tpsRequestSender.sendTpsRequest(any(), any())).thenReturn(new TpsServiceRoutineResponse("xml", xmlAsMap));
    }
    
    /**
     * HVIS sendNavEndringsmelding.sendMessage blir kalt, SÅ skal opprettEgenAnsatt og opprettSikTiltak kalles og meldingene fra dem skal sendes til TPS.
     */
    @Test
    public void shouldSendeNavEndringsmeldingerTilTps() {
        List<TpsNavEndringsMelding> opprettEgenAnsattResultat = new ArrayList<>();
        List<TpsNavEndringsMelding> opprettSikTiltakResultat = new ArrayList<>();

        opprettEgenAnsattResultat.add(new TpsNavEndringsMelding(new TpsEndreEgenansattRequest(),"u5"));
        opprettSikTiltakResultat.add(new TpsNavEndringsMelding( new TpsEndreSikkerhetstiltakRequest(), "u5"));

        when(opprettEgenAnsattMelding.execute(any(), eq(environments))).thenReturn(opprettEgenAnsattResultat);
        when(opprettSikkerhetstiltakMelding.execute(any(), eq(environments))).thenReturn(opprettSikTiltakResultat);
        when(tpsNavEndringsMelding.getMelding()).thenReturn(tpsServiceRoutineRequest);

        sendNavEndringsmeldinger.execute(testPersonerListe, environments);

        verify(tpsRequestSender, times(4)).sendTpsRequest(any(), any());
    }
}
