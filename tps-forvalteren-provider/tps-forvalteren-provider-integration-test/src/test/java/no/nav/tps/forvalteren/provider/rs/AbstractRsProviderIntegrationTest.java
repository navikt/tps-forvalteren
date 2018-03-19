package no.nav.tps.forvalteren.provider.rs;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.IOException;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.tjeneste.pip.diskresjonskode.binding.DiskresjonskodePortType;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeBolkResponse;
import no.nav.tjeneste.pip.diskresjonskode.meldinger.HentDiskresjonskodeResponse;
import no.nav.tps.forvalteren.consumer.ws.tpsws.diskresjonskode.DiskresjonskodeConsumer;
import no.nav.tps.forvalteren.consumer.ws.tpsws.egenansatt.EgenAnsattConsumer;
import no.nav.tps.forvalteren.provider.rs.api.v1.config.RsProviderIntegrationTestConfig;

//TODO Bare legg til disse her, så vil man få feil grunnet kluss i dependencies.
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RsProviderIntegrationTestConfig.class)
public abstract class AbstractRsProviderIntegrationTest {

    @Autowired(required = false)
    protected WebApplicationContext context;

    @Mock
    protected DiskresjonskodeConsumer diskresjonskodeConsumerMock;

    @Mock
    protected EgenAnsattConsumer egenAnsattConsumerMock;

    protected MockMvc mvc;

    protected static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    protected static String convertObjectToJson(Object object) throws IOException {
        return MAPPER.writeValueAsString(object);
    }

    @Before
    public void setup() {
        if (context != null) {
            mvc = MockMvcBuilders.webAppContextSetup(context).build();
        }

        HentDiskresjonskodeResponse response = new HentDiskresjonskodeResponse();
//        response.setDiskresjonskode("1");
        response.setDiskresjonskode("");
        doReturn(response).when(diskresjonskodeConsumerMock).getDiskresjonskodeResponse(anyString());
        when(diskresjonskodeConsumerMock.getDiskresjonskodeResponse(anyString())).thenReturn(response);

        HentDiskresjonskodeBolkResponse bolkResponse = new HentDiskresjonskodeBolkResponse();
        when(diskresjonskodeConsumerMock.getDiskresjonskodeBolk(anyListOf(String.class))).thenReturn(bolkResponse);

        when(egenAnsattConsumerMock.isEgenAnsatt(any(String.class))).thenReturn(false);
    }
}
