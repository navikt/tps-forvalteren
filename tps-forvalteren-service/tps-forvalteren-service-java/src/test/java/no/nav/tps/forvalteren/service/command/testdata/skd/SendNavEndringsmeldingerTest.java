package no.nav.tps.forvalteren.service.command.testdata.skd;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsResponseMappingUtils.STATUS_KEY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreEgenansattRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreSikkerhetstiltakRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;
import no.nav.tps.forvalteren.domain.service.user.User;
import no.nav.tps.forvalteren.service.command.testdata.EndreSprakkodeService;
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
    private EndreSprakkodeService endreSprakkodeService;

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

    List<Person> testPersonerListe;

    private Set<String> environments;

    @Before
    public void setup() {

        testPersonerListe = newArrayList(new Person(), new Person());

        tpsServiceRoutineRequest = new TpsServiceRoutineRequest();

        environments = newHashSet("u5", "u6");

        setupMocks();
    }

    private void setupMocks() {
        when(userContextHolder.getUser()).thenReturn(new User("Z111111", "Z111111"));

        LinkedHashMap xmlAsMap = new LinkedHashMap();
        xmlAsMap.put(STATUS_KEY, new ResponseStatus("00", "melding", "utfyllende melding"));
        when(tpsRequestSender.sendTpsRequest(any(), any())).thenReturn(new TpsServiceRoutineResponse("xml", xmlAsMap));
    }

    /**
     * HVIS sendNavEndringsmelding.sendMessage blir kalt, SÅ skal opprettEgenAnsatt og opprettSikTiltak kalles og meldingene fra dem skal sendes til TPS.
     */
    @Test
    public void shouldSendeNavEndringsmeldingerTilTps() {
        List<TpsNavEndringsMelding> opprettEgenAnsattResultat = new ArrayList<>();
        List<TpsNavEndringsMelding> opprettSikTiltakResultat = new ArrayList<>();

        opprettEgenAnsattResultat.add(new TpsNavEndringsMelding(new TpsEndreEgenansattRequest(), "u5"));
        opprettSikTiltakResultat.add(new TpsNavEndringsMelding(new TpsEndreSikkerhetstiltakRequest(), "u5"));

        when(opprettEgenAnsattMelding.execute(any(), eq(environments))).thenReturn(opprettEgenAnsattResultat);
        when(opprettSikkerhetstiltakMelding.execute(any(), eq(environments))).thenReturn(opprettSikTiltakResultat);

        sendNavEndringsmeldinger.execute(testPersonerListe, environments);

        verify(tpsRequestSender, times(4)).sendTpsRequest(any(), any());
    }
}
