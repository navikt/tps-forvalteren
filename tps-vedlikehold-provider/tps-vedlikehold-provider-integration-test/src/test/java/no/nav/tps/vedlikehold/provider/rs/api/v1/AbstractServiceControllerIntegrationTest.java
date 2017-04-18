package no.nav.tps.vedlikehold.provider.rs.api.v1;

import com.google.common.base.Charsets;
import no.nav.tps.vedlikehold.consumer.mq.consumers.DefaultMessageQueueConsumer;
import no.nav.tps.vedlikehold.provider.rs.AbstractRsProviderIntegrationTest;
import no.nav.tps.vedlikehold.provider.rs.api.v1.config.RequestQueueListener;
import org.apache.activemq.ActiveMQSession;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Before;
import org.mockito.Mockito;

import javax.inject.Inject;
import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;

public abstract class AbstractServiceControllerIntegrationTest extends AbstractRsProviderIntegrationTest {

    @Inject
    protected RequestQueueListener requestQueueListener;

    @Inject
    private DefaultMessageQueueConsumer defaultMessageQueueConsumer;

    private static final String BASE_URL = "/api/v1/service/";

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
        try {
            Mockito.doReturn(requestQueueListener.getResponseQueue()).when(defaultMessageQueueConsumer).createTemporaryQueueFor(any(ActiveMQSession.class));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    protected void setResponseQueueMessage(String message) {
        requestQueueListener.setResponseMessage(message);
    }
}
