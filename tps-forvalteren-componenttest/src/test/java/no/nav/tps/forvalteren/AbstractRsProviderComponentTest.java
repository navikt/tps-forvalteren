package no.nav.tps.forvalteren;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;

import no.nav.tps.forvalteren.config.ComptestConfig;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { ComptestConfig.class, ApplicationStarter.class })
@ActiveProfiles("comptest")
public abstract class AbstractRsProviderComponentTest {
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Autowired(required = false)
    protected WebApplicationContext context;

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
    }
    protected String getRequestBody(String path) {
        URL fileUrl = Resources.getResource(path);
        try {
            return Resources.toString(fileUrl, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static String convertObjectToJson(Object object) throws IOException {
        return MAPPER.writeValueAsString(object);
    }

    protected static <T> T convertMvcResultToObject(MvcResult mvcResult, Class<T> resultClass) throws IOException {
        return MAPPER.readValue(mvcResult.getResponse().getContentAsString(), resultClass);
    }

    protected static <T> List<T> convertMvcResultToList(MvcResult mvcResult, Class<T> resultClass) throws IOException {
        JavaType type = MAPPER.getTypeFactory().constructCollectionType(List.class, resultClass);
        return MAPPER.readValue(mvcResult.getResponse().getContentAsString(), type);
    }

    protected static String getErrorMessage(MvcResult mvcResult) throws IOException {
        JavaType type = MAPPER.getTypeFactory().constructMapType(Map.class, String.class, String.class);
        Map<String, String> json = MAPPER.readValue(mvcResult.getResponse().getContentAsString(), type);
        return json.get("message");
    }

    protected static Map<String, String> getErrorMessageAtIndex(MvcResult mvcResult, int index) throws IOException {
        JavaType mapType = MAPPER.getTypeFactory().constructMapType(Map.class, String.class, String.class);
        JavaType listOfMapType = MAPPER.getTypeFactory().constructCollectionLikeType(List.class, mapType);

        List<Map<String, String>> listOfMap = MAPPER.readValue(mvcResult.getResponse().getContentAsString(), listOfMapType);
        return listOfMap.get(index);
    }
}
