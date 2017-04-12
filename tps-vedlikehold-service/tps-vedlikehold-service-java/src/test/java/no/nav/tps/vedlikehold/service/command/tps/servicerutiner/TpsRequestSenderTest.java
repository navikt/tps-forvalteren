package no.nav.tps.vedlikehold.service.command.tps.servicerutiner;

import no.nav.tps.vedlikehold.service.command.tps.TpsRequestService;
import no.nav.tps.vedlikehold.service.command.tps.servicerutiner.utils.RsTpsResponseMappingUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TpsRequestSenderTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private FindServiceRoutineByName findServiceRoutineByNameMock;

    @Mock
    private RsTpsResponseMappingUtils rsTpsResponseMappingUtilsMock;

    @Mock
    private TpsRequestService tpsRequestServiceMock;

    @InjectMocks
    private TpsRequestSender tpsRequestSender;

    //TODO Skriv test til denne!
    @Test
    public void sendTpsRequest() throws Exception {

    }

    @Test
    public void tpsRequestSenderThrowsHttpsUnauthorisedIfExceptionBoilsUpFromUsedClass() throws Exception {

    }

}