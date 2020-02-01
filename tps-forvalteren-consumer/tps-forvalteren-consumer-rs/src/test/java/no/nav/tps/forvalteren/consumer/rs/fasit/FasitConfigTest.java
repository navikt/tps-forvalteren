package no.nav.tps.forvalteren.consumer.rs.fasit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import no.nav.tps.forvalteren.consumer.rs.fasit.config.FasitConfig;


@RunWith(MockitoJUnitRunner.class)
public class FasitConfigTest {

    @Mock
    private AutowireCapableBeanFactory beanFactoryMock;

    @InjectMocks
    private FasitConfig fasitConfig;

    @Test
    public void getTpswsFasitQueueConsumerAutowiresTheInstatiatedConsumer() {
//        fasitConfig.getTpswsFasitMessageQueueQueueConsumer();
//        verify(beanFactoryMock).autowireBean( isA(DefaultFasitMessageQueueConsumer.class) );
    }
}
