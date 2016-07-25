package no.nav.tps.vedlikehold.consumer.ws.fasit;

import no.nav.tps.vedlikehold.consumer.ws.fasit.config.FasitConfig;
import no.nav.tps.vedlikehold.consumer.ws.fasit.queues.DefaultFasitMessageQueueConsumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.verify;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@RunWith(MockitoJUnitRunner.class)
public class FasitConfigTest {

    @Mock
    private AutowireCapableBeanFactory beanFactoryMock;

    @InjectMocks
    private FasitConfig fasitConfig;

    @Test
    public void getTpswsFasitQueueConsumerAutowiresTheInstatiatedConsumer() {
        fasitConfig.getTpswsFasitMessageQueueQueueConsumer();
        verify(beanFactoryMock).autowireBean( isA(DefaultFasitMessageQueueConsumer.class) );
    }
}
