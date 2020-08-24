package no.nav.tps.forvalteren.provider.rs;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.IOException;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeResponse;
import no.nav.tps.forvalteren.consumer.ws.tpsws.diskresjonskode.DiskresjonskodeConsumer;
import no.nav.tps.forvalteren.consumer.ws.tpsws.egenansatt.EgenAnsattConsumer;
import no.nav.tps.forvalteren.provider.rs.api.v1.config.RsProviderIntegrationTestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RsProviderIntegrationTestConfig.class)
public abstract class AbstractRsProviderIntegrationTest {

    @Autowired(required = false)
    protected WebApplicationContext context;

    @Autowired
    protected DiskresjonskodeConsumer diskresjonskodeConsumerMock;

    @Autowired
    protected EgenAnsattConsumer egenAnsattConsumerMock;

    protected MockMvc mvc;

    protected static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Before
    public void setup() {
        if (context != null) {
            mvc = MockMvcBuilders.webAppContextSetup(context).build();
        }

        HentDiskresjonskodeResponse response = new HentDiskresjonskodeResponse();
        response.setDiskresjonskode("");
        doReturn(response).when(diskresjonskodeConsumerMock).getDiskresjonskodeResponse(anyString());

        when(egenAnsattConsumerMock.isEgenAnsatt(anyString())).thenReturn(false);
    }

    protected static <T> T convertMvcResultToObject(MvcResult mvcResult, Class<T> resultClass) throws IOException {
        return MAPPER.readValue(mvcResult.getResponse().getContentAsString(), resultClass);
    }
}
