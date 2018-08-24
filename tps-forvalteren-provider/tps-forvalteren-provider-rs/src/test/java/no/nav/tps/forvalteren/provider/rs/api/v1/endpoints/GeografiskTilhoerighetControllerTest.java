package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.service.command.tps.servicerutiner.TpsServiceRoutineService;

@RunWith(MockitoJUnitRunner.class)
public class GeografiskTilhoerighetControllerTest {

    @InjectMocks
    private GeografiskTilhoerighetController gtController;

    @Mock
    private Map tpsParameters;

    @Mock
    private TpsServiceRoutineService tpsServiceRoutineService;

    @Test
    public void getKerninfo() throws Exception {

        gtController.getKerninfo(tpsParameters);

        verify(tpsServiceRoutineService).execute(anyString(), eq(tpsParameters), anyBoolean());
    }

    @Test
    public void getAdrhist() throws Exception {

        gtController.getAdrhist(tpsParameters);

        verify(tpsServiceRoutineService).execute(anyString(), eq(tpsParameters), anyBoolean());
    }

    @Test
    public void getAdrlinjhist() throws Exception {

        gtController.getAdrlinjhist(tpsParameters);

        verify(tpsServiceRoutineService).execute(anyString(), eq(tpsParameters), anyBoolean());
    }

    @Test
    public void getSoaihist() throws Exception {

        gtController.getSoaihist(tpsParameters);

        verify(tpsServiceRoutineService).execute(anyString(), eq(tpsParameters), anyBoolean());
    }
}