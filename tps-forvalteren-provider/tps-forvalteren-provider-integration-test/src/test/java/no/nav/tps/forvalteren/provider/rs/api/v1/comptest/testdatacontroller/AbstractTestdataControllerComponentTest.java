package no.nav.tps.forvalteren.provider.rs.api.v1.comptest.testdatacontroller;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import com.google.common.base.Charsets;

import no.nav.tps.forvalteren.provider.rs.AbstractRsProviderIntegrationTest;
import no.nav.tps.forvalteren.repository.jpa.GruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
@Transactional //TODO fjern denne og bytt ut kobling mot Oracle DB med H2 database
public abstract class AbstractTestdataControllerComponentTest extends AbstractRsProviderIntegrationTest{
    private static final String BASE_URL = "/api/v1/testdata";
    private List<NameValuePair> params = new ArrayList<>();
    @Autowired(required = false)
    protected WebApplicationContext context;
    protected MockMvc mvc;
    @Autowired
    protected GruppeRepository gruppeRepository;
    @Autowired
    protected PersonRepository personRepository;
    
    
    protected void addRequestParam(String key, Object val) {
        params.add(new BasicNameValuePair(key, val.toString()));
    }
    
    protected abstract String getServiceUrl();
    
    protected String getUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_URL);
        sb.append(getServiceUrl());
        if (!params.isEmpty()) {
            sb.append("?");
            sb.append(URLEncodedUtils.format(params, Charsets.UTF_8));
        }
        return sb.toString();
    }
    @Before
    public void setup() {
        if (context != null) {
            mvc = MockMvcBuilders.webAppContextSetup(context).build();
        }
    }
    @After
    public void clearParam() {
        params.clear();
    }
}
