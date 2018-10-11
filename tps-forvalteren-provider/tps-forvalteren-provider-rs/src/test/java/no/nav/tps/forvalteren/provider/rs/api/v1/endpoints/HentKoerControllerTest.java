package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import no.nav.tps.forvalteren.consumer.rs.environments.FasitApiConsumer;
import no.nav.tps.forvalteren.consumer.rs.environments.dao.FasitApplication;
import no.nav.tps.forvalteren.consumer.rs.environments.mapper.MqInfoMapper;

@RunWith(MockitoJUnitRunner.class)
public class HentKoerControllerTest {

    private static final String APP = "tpsws";

    @Mock
    private FasitApiConsumer fasitApiConsumer;

    @InjectMocks
    private HentKoerController hentKoerController;

    @Mock
    private FasitApplication fasitApplication;

    @Mock
    private MqInfoMapper mqInfoMapper;

    @Test
    public void getApplicationsOk() {
        hentKoerController.getApplications(APP);

        verify(fasitApiConsumer).getApplications(APP);
    }

    @Test
    public void getQueuesOk() {
        when(fasitApiConsumer.getApplicationInstances(APP, true)).thenReturn(Arrays.asList(fasitApplication));

        hentKoerController.getQueues(APP);

        verify(fasitApiConsumer).getApplicationInstances(APP, true);
        verify(fasitApiConsumer).getUsedResourcesFromAppByTypes(any(FasitApplication.class), any());
        verify(mqInfoMapper).execute(anyList(), anyList(), anyList());
    }
}
