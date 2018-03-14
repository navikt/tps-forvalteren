package no.nav.tps.forvalteren.service.command.tps;

import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.XML_REQUEST_QUEUE_ENDRINGSMELDING_ALIAS;
import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.XML_REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;
import no.nav.tps.forvalteren.service.command.tps.xmlmelding.GetQueueName;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GetQueueNameTest {

    @InjectMocks
    private GetQueueName getQueueName;

    private String queue1 = XML_REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;
    private String queue2 = XML_REQUEST_QUEUE_ENDRINGSMELDING_ALIAS;

    @Test
    public void getQueueNameTest() {
        assertThat(getQueueName.execute("u5", queue1), is(equalTo("QA.D8_411.TPS_FORESPORSEL_XML_O")));
        assertThat(getQueueName.execute("d8", queue2), is(equalTo("QA.D8_412.SFE_ENDRINGSMELDING")));
        assertThat(getQueueName.execute("q10", queue1), is(equalTo("QA.Q10_411.TPS_FORESPORSEL_XML_O")));
        assertThat(getQueueName.execute("t9", queue1), is(equalTo("QA.T9_411.TPS_FORESPORSEL_XML_O")));
        assertThat(getQueueName.execute("t0", queue2), is(equalTo("QA.T0_412.SFE_ENDRINGSMELDING")));

    }
}
