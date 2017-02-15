package no.nav.tps.vedlikehold.service.command.tps.transformation.request;

import no.nav.tps.vedlikehold.domain.service.tps.Request;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.request.ServiceRoutineRequestTransform;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ServiceRoutineRequestTransformStrategyTest {

    private static final String XML_PROPERTIES_PREFIX  = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><tpsPersonData>";
    private static final String XML_PROPERTIES_POSTFIX = "</tpsPersonData>";

    @InjectMocks
    private ServiceRoutineRequestTransformStrategy strategy;

    @Test
    public void isSupportedReturnsTrueForServiceRoutineRequestTransform() {
        ServiceRoutineRequestTransform transform = new ServiceRoutineRequestTransform();
        boolean isSupported = strategy.isSupported(transform);

        assertThat(isSupported, is(true));
    }

    @Test
    public void executeUpdatesRequestXmlWithWrappingElements() {
        String xml = "xml";

        Request request = new Request();
        request.setXml(xml);

        strategy.execute(request, null);

        assertThat(request.getXml(), is(equalTo(XML_PROPERTIES_PREFIX+xml+XML_PROPERTIES_POSTFIX)));

    }

}