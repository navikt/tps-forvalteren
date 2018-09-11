package no.nav.tps.forvalteren.service.command.testdata.utils;

import static no.nav.tps.forvalteren.service.command.tps.servicerutiner.utils.RsTpsResponseMappingUtils.STATUS_KEY;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response.TpsServiceRoutineResponse;

@RunWith(MockitoJUnitRunner.class)
public class BehandleTpsResponsTest {

    @Mock
    private TpsServiceRoutineResponse tpsServiceRoutineResponse;

    @Mock
    private ResponseStatus responseStatus;

    @Test (expected = IllegalAccessException.class)
    public void privateConstructorCannotBeCalled() throws Exception {

        Class clas = Class.forName("no.nav.tps.forvalteren.service.command.testdata.utils.BehandleTpsRespons");
        clas.newInstance();
    }

    @Test
    public void ekstraherStatusFraServicerutineRespons() throws Exception {
        Map<String, ResponseStatus> linkedMap = new LinkedHashMap<>();
        linkedMap.put(STATUS_KEY, responseStatus);
        when(tpsServiceRoutineResponse.getResponse()).thenReturn(linkedMap);

        ResponseStatus target = BehandleTpsRespons.ekstraherStatusFraServicerutineRespons(tpsServiceRoutineResponse);

        assertThat(target, is(responseStatus));
    }
}