package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.service.tps.Response;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;
import no.nav.tps.forvalteren.service.command.tps.TpsRequestService;
import no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsResponseMappingUtils;

@RunWith(MockitoJUnitRunner.class)
public class TpsRequestSenderTest {
    
    private static long TIMEOUT = 5000;
    
    @Mock
    private FindServiceRoutineByName findServiceRoutineByNameMock;
    
    @Mock
    private TpsRequestService tpsRequestServiceMock;
    
    @Mock
    private TpsServiceRoutineDefinitionRequest serviceRoutineDefinitionMock;
    
    @Mock
    private TpsServiceRoutineRequest tpsServiceRoutineRequestMock;
    
    @Mock
    private TpsRequestContext contextMock;
    
    @Mock
    private RsTpsResponseMappingUtils utilsMock;
    
    @InjectMocks
    private TpsRequestSender tpsRequestSender;
    
    @Test
    public void sendTpsRequest() throws Exception {
        Response response = mock(Response.class);
        Optional<TpsServiceRoutineDefinitionRequest> tpsServiceRoutineDefinition = Optional.of(serviceRoutineDefinitionMock);
        
        when(tpsServiceRoutineRequestMock.getServiceRutinenavn()).thenReturn("navn");
        when(findServiceRoutineByNameMock.execute(anyString())).thenReturn(tpsServiceRoutineDefinition);
        when(tpsRequestServiceMock.executeServiceRutineRequest(tpsServiceRoutineRequestMock, serviceRoutineDefinitionMock, contextMock, TIMEOUT)).thenReturn(response);
        
        tpsRequestSender.sendTpsRequest(tpsServiceRoutineRequestMock, contextMock);
        
        verify(tpsRequestServiceMock).executeServiceRutineRequest(tpsServiceRoutineRequestMock, serviceRoutineDefinitionMock, contextMock, TIMEOUT);
        verify(utilsMock).convertToTpsServiceRutineResponse(response);
    }
}