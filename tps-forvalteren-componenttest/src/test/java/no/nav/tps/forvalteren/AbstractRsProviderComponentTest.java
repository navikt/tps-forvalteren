package no.nav.tps.forvalteren;

import static com.google.common.collect.Sets.newHashSet;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.io.Resources;

import no.nav.tps.forvalteren.consumer.rs.environments.FasitApiConsumer;
import no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.TestdataController;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ComptestConfig.class)
@ActiveProfiles("comptest")
public abstract class AbstractRsProviderComponentTest {

    protected static final Set ENV_SET = newHashSet("t0", "t1", "t2", "t3");
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Autowired(required = false)
    protected WebApplicationContext context;

    @Autowired
    protected TestdataController testdataController;

    public MockMvc mvc;

    @Autowired
    private FasitApiConsumer fasitApiConsumer;

    protected static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.registerModule(new JavaTimeModule());
    }

    @Before
    public void setupmockmvc() {
        if (context != null) {
            mvc = MockMvcBuilders.webAppContextSetup(context).build();
        }
        when(fasitApiConsumer.getEnvironments("tpsws")).thenReturn(ENV_SET);
    }
    
    protected String getResourceFileContent(String path) {
        URL fileUrl = Resources.getResource(path);
        try {
            return Resources.toString(fileUrl, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    protected String removeWhitespaceBetweenTags(String text) {
        return text.replaceAll(">\\s+<", "><");
    }

    protected static <T> T convertMvcResultToObject(MvcResult mvcResult, Class<T> resultClass) throws IOException {
        return MAPPER.readValue(mvcResult.getResponse().getContentAsString(), resultClass);
    }

    protected static <T> List<T> convertMvcResultToList(MvcResult mvcResult, Class<T> resultClass) throws IOException {
        JavaType type = MAPPER.getTypeFactory().constructCollectionType(List.class, resultClass);
        return MAPPER.readValue(mvcResult.getResponse().getContentAsString(), type);
    }
}
