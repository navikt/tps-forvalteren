package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.FindServiceRoutineByName;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsServiceRoutineService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsRequestMappingUtils;

@RunWith(MockitoJUnitRunner.class)
public class ServiceroutineControllerTest {
    private static final String FNR = "12345678910";
    private static final String SERVICE_RUTINE_NAME = "serviceRutineName";
    private static final String ENVIRONMENT_U = "u1";
    private static final String ENVIRONMENT_PROD = "p";

    private Map<String, Object> parameters;

    @Mock
    private FindServiceRoutineByName findServiceRoutineByName;

    @Mock
    private TpsServiceRoutineDefinitionRequest tpsServiceRoutineDefinitionMock;

    @Mock
    private TpsServiceRoutineRequest serviceRoutineRequestMock;

    @Mock
    private TpsServiceRoutineService getTpsServiceRoutineService;

    @Mock
    private RsTpsRequestMappingUtils mappingUtilsMock;

    @InjectMocks
    private ServiceroutineController serviceroutineController;

    @Before
    public void setup() {
        parameters = new HashMap<>();
        parameters.put("environment", ENVIRONMENT_U);
        parameters.put("serviceRutinenavn", SERVICE_RUTINE_NAME);
    }

    @Test
    public void getServiceUsingHttpGetSetsServiceRoutineNameOnParameters() {
        parameters.put("environment", ENVIRONMENT_U);
        parameters.put("fnr", FNR);

        serviceroutineController.executeServiceRoutine(parameters, SERVICE_RUTINE_NAME);

        assertThat(parameters.get("serviceRutinenavn"), is(SERVICE_RUTINE_NAME));
    }

    @Test
    public void getServiceInProdTryingToCallProdEnvironmentDoNotThrowIllegalEnvironmentException() {
        parameters.put("environment", ENVIRONMENT_PROD);

        serviceroutineController.executeServiceRoutine(parameters, SERVICE_RUTINE_NAME);
    }
}