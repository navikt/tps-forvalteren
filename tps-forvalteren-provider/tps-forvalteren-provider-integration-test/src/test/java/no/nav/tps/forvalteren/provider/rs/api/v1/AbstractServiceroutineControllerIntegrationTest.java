package no.nav.tps.forvalteren.provider.rs.api.v1;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;

import java.util.ArrayList;
import java.util.List;
import javax.jms.JMSException;
import org.apache.activemq.ActiveMQSession;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Before;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.common.base.Charsets;

import no.nav.tps.forvalteren.consumer.mq.consumers.MessageQueueConsumer;
import no.nav.tps.forvalteren.provider.rs.AbstractRsProviderIntegrationTest;
import no.nav.tps.forvalteren.provider.rs.api.v1.config.RequestQueueListener;

public abstract class AbstractServiceroutineControllerIntegrationTest extends AbstractRsProviderIntegrationTest {

    @Autowired
    protected RequestQueueListener requestQueueListener;

    @Autowired
    protected MessageQueueConsumer messageQueueConsumer;

    private static final String BASE_URL = "/api/v1/serviceroutine/";

    private List<NameValuePair> params = new ArrayList<>();

    protected abstract String getServiceName();

    protected void addRequestParam(String key, Object val) {
        params.add(new BasicNameValuePair(key, val.toString()));
    }

    protected String getUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL);
        sb.append(getServiceName());
        if (!params.isEmpty()) {
            sb.append("?");
            sb.append(URLEncodedUtils.format(params, Charsets.UTF_8));
        }
        return sb.toString();
    }

    @Before
    public void before() {
        reset(messageQueueConsumer);
    
        try {
            Mockito.doReturn(requestQueueListener.getResponseQueue()).when(messageQueueConsumer).createTemporaryQueueFor(any(ActiveMQSession.class));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    protected void setResponseQueueMessage(String message) {
        requestQueueListener.setResponseMessage(message);
    }
}
