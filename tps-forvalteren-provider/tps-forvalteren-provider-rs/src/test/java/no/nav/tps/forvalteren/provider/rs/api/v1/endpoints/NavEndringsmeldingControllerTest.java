package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import java.util.HashMap;
import java.util.Map;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsRequestSender;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsRequestMappingUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NavEndringsmeldingControllerTest {

    private Map<String, Object> testParams;
    private static final String SERVICERUTINE_NAVN = "test_navn";

    @Mock
    private TpsRequestSender tpsRequestSender;

    @Mock
    private RsTpsRequestMappingUtils mappingUtils;

    @Mock
    private TpsServiceRoutineRequest tpsServiceRoutineRequest;

    @InjectMocks
    private NavEndringsmeldingController navEndringsmeldingController;

    @Before
    public void setup() {
        testParams = new HashMap<>();
        testParams.put("testIdent", "12345678900");
        testParams.put("environment", "u1");

        when(mappingUtils.convertToTpsServiceRoutineRequest(SERVICERUTINE_NAVN, testParams)).thenReturn(tpsServiceRoutineRequest);
    }

    @Test
    public void navEndringsmeldingControllerTest() {

        ArgumentCaptor<TpsRequestContext> captor = ArgumentCaptor.forClass(TpsRequestContext.class);
        navEndringsmeldingController.sendMelding(testParams, "test_navn");

        verify(tpsRequestSender).sendTpsRequest(any(TpsServiceRoutineRequest.class), captor.capture());
    }
}
