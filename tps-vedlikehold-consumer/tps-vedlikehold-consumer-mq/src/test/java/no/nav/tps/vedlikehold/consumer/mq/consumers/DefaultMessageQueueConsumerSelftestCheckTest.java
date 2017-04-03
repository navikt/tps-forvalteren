package no.nav.tps.vedlikehold.consumer.mq.consumers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.jms.JMSException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultMessageQueueConsumerSelftestCheckTest {

    @InjectMocks
    private DefaultMessageQueueConsumerSelftestCheck selftestCheck;

    @Mock
    private MessageQueueConsumer consumer;

    @Test
    public void includesTheApplicationNameInDescription() {
        String uppercaseResult = selftestCheck.getDescription().toUpperCase();
        assertThat(uppercaseResult, containsString("TPS/MQ"));
    }

    @Test
    public void includesOperationNameInEndpoint() {
        String result = selftestCheck.getEndpoint();
        assertThat(result, containsString("ping"));
    }

    @Test
    public void isVital() {
        boolean result = selftestCheck.isVital();
        assertThat(result, is(true));
    }

    @Test
    public void returnsTrueOnPerform() throws JMSException{
        when(consumer.sendMessage(anyString())).thenReturn("TPS/MQ OK");
        boolean result = selftestCheck.perform();
        assertThat(result, is(true));
    }
}
