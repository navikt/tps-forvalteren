package no.nav.tps.vedlikehold.provider.rs.api.v1.utils;

import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsHentPersonRequestServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsPingRequestServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestServiceRoutine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by Øyvind Grimnes, Visma Consulting AS on 27.07.2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class RequestClassServiceTest {

    @Test
    public void returnsCorrectClasses() {
        assertThat(RequestClassService.getClassForServiceRutinenavn("FS03-FDNUMMER-PERSDATA-O").getName(), is(TpsHentPersonRequestServiceRoutine.class.getName()));
        assertThat(RequestClassService.getClassForServiceRutinenavn("FS03-OTILGANG-TILSRTPS-O").getName(), is(TpsPingRequestServiceRoutine.class.getName()));
        assertThat(RequestClassService.getClassForServiceRutinenavn("").getName(), is(TpsRequestServiceRoutine.class.getName()));
    }
}
