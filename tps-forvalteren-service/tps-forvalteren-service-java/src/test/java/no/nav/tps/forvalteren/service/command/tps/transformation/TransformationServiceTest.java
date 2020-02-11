package no.nav.tps.forvalteren.service.command.tps.transformation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.service.tps.Request;
import no.nav.tps.forvalteren.domain.service.tps.Response;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.request.RequestTransformer;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseTransformer;
import no.nav.tps.forvalteren.service.command.tps.transformation.request.RequestTransformStrategy;
import no.nav.tps.forvalteren.service.command.tps.transformation.response.ResponseTransformStrategy;

@RunWith(MockitoJUnitRunner.class)
public class TransformationServiceTest {

    @Spy
    private List<RequestTransformStrategy> requestStrategies = new ArrayList<>();

    @Spy
    private List<ResponseTransformStrategy> responseStrategies = new ArrayList<>();

    @InjectMocks
    private TransformationService serviceMock;


    @Test
    public void callExecuteForAllSupportedPreSendTransformersInServiceRoutineDefinition() {
        Request request = mock(Request.class);

        RequestTransformer transformer1 = mock(RequestTransformer.class);
        RequestTransformer transformer2 = mock(RequestTransformer.class);

        TpsServiceRoutineDefinitionRequest serviceRoutine = mock(TpsServiceRoutineDefinitionRequest.class);

        when(serviceRoutine.getTransformers()).thenReturn(Arrays.asList(transformer1, transformer2));

        RequestTransformStrategy strategy1 = mock(RequestTransformStrategy.class);
        RequestTransformStrategy strategy2 = mock(RequestTransformStrategy.class);

        requestStrategies.add(strategy1);
        requestStrategies.add(strategy2);


        when(strategy1.isSupported(transformer1)).thenReturn(true);
        when(strategy1.isSupported(transformer2)).thenReturn(false);

        when(strategy2.isSupported(transformer1)).thenReturn(false);
        when(strategy2.isSupported(transformer2)).thenReturn(false);


        serviceMock.transform(request, serviceRoutine);

        verify(strategy1, times(1)).execute(request, transformer1);
        verify(strategy1, times(0)).execute(request, transformer2);
        verify(strategy2, times(0)).execute(request, transformer1);
        verify(strategy2, times(0)).execute(request, transformer2);
    }

    @Test
    public void callsExecuteForAllSupportedPostSendTransformersInServiceRoutineDefinition() {

        Response response = mock(Response.class);

        ResponseTransformer transformer1 = mock(ResponseTransformer.class);
        ResponseTransformer transformer2 = mock(ResponseTransformer.class);

        TpsServiceRoutineDefinitionRequest serviceRoutine = mock(TpsServiceRoutineDefinitionRequest.class);

        when(serviceRoutine.getTransformers()).thenReturn(Arrays.asList(transformer1, transformer2));

        ResponseTransformStrategy strategy1 = mock(ResponseTransformStrategy.class);
        ResponseTransformStrategy strategy2 = mock(ResponseTransformStrategy.class);

        responseStrategies.add(strategy1);
        responseStrategies.add(strategy2);


        when(strategy1.isSupported(transformer1)).thenReturn(true);
        when(strategy1.isSupported(transformer2)).thenReturn(false);

        when(strategy2.isSupported(transformer1)).thenReturn(false);
        when(strategy2.isSupported(transformer2)).thenReturn(true);


        serviceMock.transform(response, serviceRoutine);

        verify(strategy1, times(1)).execute(response, transformer1);
        verify(strategy1, times(0)).execute(response, transformer2);
        verify(strategy2, times(0)).execute(response, transformer1);
        verify(strategy2, times(1)).execute(response, transformer2);


    }



}