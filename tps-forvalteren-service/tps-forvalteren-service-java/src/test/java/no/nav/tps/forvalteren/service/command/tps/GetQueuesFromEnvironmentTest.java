package no.nav.tps.forvalteren.service.command.tps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import no.nav.tps.forvalteren.domain.rs.RsTpsMeldingKo;
import no.nav.tps.forvalteren.service.command.FilterEnvironmentsOnDeployedEnvironment;
import no.nav.tps.forvalteren.service.command.tps.xmlmelding.GetQueueName;
import no.nav.tps.forvalteren.service.command.tps.xmlmelding.GetQueuesFromEnvironment;
import no.nav.tps.forvalteren.service.command.tpsconfig.GetEnvironments;

@RunWith(MockitoJUnitRunner.class)
public class GetQueuesFromEnvironmentTest {

    @Mock
    private FilterEnvironmentsOnDeployedEnvironment filterEnvironmentsOnDeployedEnvironment;

    @Mock
    private GetEnvironments getEnvironments;

    @Mock
    private GetQueueName getQueueName;

    @InjectMocks
    private GetQueuesFromEnvironment getQueuesFromEnvironment;

    @Before
    public void setup() {
        when(getEnvironments.getEnvironments()).thenReturn(new HashSet<>(Arrays.asList("q1", "u5", "u6", "t9")));
        when(filterEnvironmentsOnDeployedEnvironment.execute(getEnvironments.getEnvironments()))
                .thenReturn(new HashSet<>(Arrays.asList("q1", "u5", "u6", "t9")));
        when(getQueueName.execute("U6", "411.TPS_FORESPORSEL_XML_O")).thenReturn("QA.D8_411.TPS_FORESPORSEL_XML_O");
        when(getQueueName.execute("U6", "412.SFE_ENDRINGSMELDING")).thenReturn("QA.D8_412.SFE_ENDRINGSMELDING");
        when(getQueueName.execute("Q1", "411.TPS_FORESPORSEL_XML_O")).thenReturn("QA.Q1_411.TPS_FORESPORSEL_XML_O");
        when(getQueueName.execute("Q1", "412.SFE_ENDRINGSMELDING")).thenReturn("QA.Q1_412.SFE_ENDRINGSMELDING");
        when(getQueueName.execute("T9", "411.TPS_FORESPORSEL_XML_O")).thenReturn("QA.T9_411.TPS_FORESPORSEL_XML_O");
        when(getQueueName.execute("T9", "412.SFE_ENDRINGSMELDING")).thenReturn("QA.T9_412.SFE_ENDRINGSMELDING");
    }

    @Test
    public void getQueuesFromEnvironmentTest() {
        List<RsTpsMeldingKo> test = getQueuesFromEnvironment.execute("tpsws");

        assertThat(test.get(0).getKoNavn(), is(equalTo("QA.Q1_411.TPS_FORESPORSEL_XML_O")));
        assertThat(test.get(1).getKoNavn(), is(equalTo("QA.Q1_412.SFE_ENDRINGSMELDING")));
        assertThat(test.get(2).getKoNavn(), is(equalTo("QA.D8_411.TPS_FORESPORSEL_XML_O")));
        assertThat(test.get(3).getKoNavn(), is(equalTo("QA.D8_412.SFE_ENDRINGSMELDING")));
        assertThat(test.get(4).getKoNavn(), is(equalTo("QA.T9_411.TPS_FORESPORSEL_XML_O")));
        assertThat(test.get(5).getKoNavn(), is(equalTo("QA.T9_412.SFE_ENDRINGSMELDING")));
    }
}
