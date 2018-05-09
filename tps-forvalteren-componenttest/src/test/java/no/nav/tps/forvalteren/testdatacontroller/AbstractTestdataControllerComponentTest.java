package no.nav.tps.forvalteren.testdatacontroller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.google.common.base.Charsets;

import no.nav.tps.forvalteren.AbstractRsProviderComponentTest;
import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.GruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;

public abstract class AbstractTestdataControllerComponentTest extends AbstractRsProviderComponentTest {
    private static final String BASE_URL = "/api/v1/testdata";
    protected final String IDENT1 = "11111111111";
    protected final String IDENT2 = "22222222222";
    protected final String GRUPPENAVN = "regresjonstest gruppe";
    
    @Autowired(required = false)
    protected WebApplicationContext context;
    protected MockMvc mvc;
    @Autowired
    protected GruppeRepository gruppeRepository;
    @Autowired
    protected PersonRepository personRepository;
    private List<NameValuePair> params = new ArrayList<>();
    
    protected List<Person> constructTestpersonsInTpsfDatabase(Gruppe gruppe) {
        Person person = personRepository.save(Person.builder().gruppe(gruppe).identtype("per").kjonn('m').regdato(LocalDateTime.now()).fornavn("lol").etternavn("sdf").ident(IDENT1).statsborgerskap("nor").build());
        Person person2 = personRepository.save(Person.builder().gruppe(gruppe).identtype("per").kjonn('k').regdato(LocalDateTime.now()).fornavn("fnavn").etternavn("etternavn2").ident(IDENT2).statsborgerskap("nor").build());
        return Arrays.asList(person, person2);
    }
    
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
        
        gruppeRepository.deleteAll();
        personRepository.deleteAll();
    }
    
    @After
    public void clearParam() {
        params.clear();
    }
}
