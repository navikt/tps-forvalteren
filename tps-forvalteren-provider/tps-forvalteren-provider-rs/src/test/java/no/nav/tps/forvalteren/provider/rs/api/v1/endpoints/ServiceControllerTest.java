package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.FindServiceRoutineByName;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.GetTpsServiceRoutineResponse;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsRequestSender;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsRequestMappingUtils;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceControllerTest {
    private static final String FNR = "12345678910";
    private static final String SERVICE_RUTINE_NAME = "serviceRutineName";
    private static final String ENVIRONMENT_U = "u1";
    private static final String ENVIRONMENT_PROD = "p";

    private Map<String, Object> parameters;

    @Mock
    private UserContextHolder userContextHolderMock;

    @Mock
    private FindServiceRoutineByName findServiceRoutineByName;

    @Mock
    private TpsServiceRoutineDefinitionRequest tpsServiceRoutineDefinitionMock;

    @Mock
    private TpsServiceRoutineRequest serviceRoutineRequestMock;

    @Mock
    private GetTpsServiceRoutineResponse getTpsServiceRoutineResponse;

    @Mock
    private RsTpsRequestMappingUtils mappingUtilsMock;

    @Mock
    private TpsRequestSender tpsRequestSenderMock;

    @InjectMocks
    private ServiceController controller;

    @Before
    @SuppressWarnings("unchecked")
    public void before() {
        parameters = new HashMap<>();
        parameters.put("environment", ENVIRONMENT_U);
        parameters.put("serviceRutinenavn", SERVICE_RUTINE_NAME);

        when(mappingUtilsMock.convertToTpsServiceRoutineRequest(SERVICE_RUTINE_NAME, parameters)).thenReturn(serviceRoutineRequestMock);

        when(findServiceRoutineByName.execute(anyString())).thenReturn(Optional.of(tpsServiceRoutineDefinitionMock));
        when(serviceRoutineRequestMock.getServiceRutinenavn()).thenReturn(SERVICE_RUTINE_NAME);
    }

    @Test
    public void getServiceUsingHttpGetSetsServiceRoutineNameOnParameters() {
        parameters.put("environment", ENVIRONMENT_U);
        parameters.put("fnr", FNR);

        controller.getService(parameters, SERVICE_RUTINE_NAME);

        assertThat(parameters.get("serviceRutinenavn"), is(SERVICE_RUTINE_NAME));
    }

    @Test
    public void getServiceInProdTryingToCallProdEnvironmentDoNotThrowIllegalEnvironmentException() {
        parameters.put("environment", ENVIRONMENT_PROD);

        controller.getService(parameters, SERVICE_RUTINE_NAME);
    }
}