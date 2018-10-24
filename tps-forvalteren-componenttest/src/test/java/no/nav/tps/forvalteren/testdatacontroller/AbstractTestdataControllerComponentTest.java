package no.nav.tps.forvalteren.testdatacontroller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TestTransaction;
import com.google.common.base.Charsets;

import no.nav.tps.forvalteren.AbstractRsProviderComponentTest;
import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.DoedsmeldingRepository;
import no.nav.tps.forvalteren.repository.jpa.GruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;

public abstract class AbstractTestdataControllerComponentTest extends AbstractRsProviderComponentTest {

    private static final String BASE_URL = "/api/v1/testdata";
    protected static final String IDENT1 = "11111111111";
    protected static final String IDENT2 = "22222222222";
    protected static final String GRUPPENAVN = "regresjonstest gruppe";

    @Autowired
    protected GruppeRepository gruppeRepository;

    @Autowired
    protected PersonRepository personRepository;

    @Autowired
    protected RelasjonRepository relasjonRepository;

    @Autowired
    protected DoedsmeldingRepository doedsmeldingRepository;

    private List<NameValuePair> params = new ArrayList<>();

    @Before
    public void clearAllRepositories() {
        doedsmeldingRepository.deleteAll();
        relasjonRepository.deleteAll();
        gruppeRepository.deleteAll();
        personRepository.deleteAll();
    }

    @After
    public void clearParam() {
        params.clear();
    }

    protected List<Person> constructTestpersonsInTpsfDatabase(Gruppe gruppe) {
        Person person = personRepository.save(Person.builder()
                .gruppe(gruppe)
                .identtype("per")
                .kjonn("m")
                .regdato(LocalDateTime.now())
                .fornavn("lol").etternavn("sdf")
                .ident(IDENT1)
                .statsborgerskap("nor")
                .opprettetDato(LocalDateTime.now())
                .opprettetAv("a123456")
                .build());
        Person person2 = personRepository.save(Person.builder()
                .gruppe(gruppe)
                .identtype("per")
                .kjonn("k")
                .regdato(LocalDateTime.now())
                .fornavn("fnavn")
                .etternavn("etternavn2")
                .ident(IDENT2)
                .statsborgerskap("nor")
                .opprettetDato(LocalDateTime.now())
                .opprettetAv("b234567")
                .build());
        return Arrays.asList(person, person2);
    }

    protected List<Person> setupTestdataPersonerInTpsfDatabase() {
        clearAllRepositories();
        Gruppe gruppe = gruppeRepository.save(Gruppe.builder().navn(GRUPPENAVN).build());
        List<Person> personList = constructTestpersonsInTpsfDatabase(gruppe);
        return personList;
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

    protected void endTransactionIfActive() {
        if (TestTransaction.isActive()) {
            TestTransaction.end();
        }
    }
}
