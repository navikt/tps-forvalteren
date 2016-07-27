package no.nav.tps.vedlikehold.provider.rs.api.v1.utils;

import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsHentPersonRequest;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsPingRequest;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequest;
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
        assertThat(RequestClassService.getClassForServiceRutinenavn("FS03-FDNUMMER-PERSDATA-O").getName(), is(TpsHentPersonRequest.class.getName()));
        assertThat(RequestClassService.getClassForServiceRutinenavn("FS03-OTILGANG-TILSRTPS-O").getName(), is(TpsPingRequest.class.getName()));
        assertThat(RequestClassService.getClassForServiceRutinenavn("").getName(), is(TpsRequest.class.getName()));
    }
}
