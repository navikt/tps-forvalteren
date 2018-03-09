package no.nav.tps.forvalteren.provider.rs.api.v1.endpoints;

import java.util.Collections;
import java.util.List;

import no.nav.tps.forvalteren.domain.rs.RsXmlMeldingKo;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.command.tpsconfig.GetEnvironments;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HentKoerControllerTest {

    @Mock
    private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;

    @Mock
    private GetEnvironments getEnvironments;

    @InjectMocks
    private HentKoerController hentKoerController;

    @Before
    public void setup() {
        when(getEnvironments.getEnvironmentsFromFasit("tpsws")).thenReturn(Collections.singleton("u6"));
        when(filterEnvironmentsOnDeployedEnvironment.execute(getEnvironments.getEnvironmentsFromFasit("tpsws")))
                .thenReturn(Collections.singleton("u6"));
    }

    @Test
    public void getQueuesTest() {
        List<RsXmlMeldingKo> bla = hentKoerController.getQueues();

        assertThat(bla.get(0).getKoNavn(), is(equalTo("QA.D8_411.TPS_FORESPORSEL_XML_O")));
    }

}
