package no.nav.tps.forvalteren.repository.jpa;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import no.nav.tps.forvalteren.domain.jpa.Doedsmelding;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.test.provider.PersonProvider;
import no.nav.tps.forvalteren.repository.jpa.config.RepositoryTestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RepositoryTestConfig.class)
@Transactional
public class DoedsmeldingRepositoryComponentTest {

    private static final boolean MELDING_SENT = true;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    DoedsmeldingRepository doedsmeldingRepository;

    @Autowired
    DoedsmeldingTestRepository doedsmeldingTestRepository;

    private Person aPerson;
    private Doedsmelding aDoedsmelding;

    @Before
    public void setup() {
        aPerson = PersonProvider.aMalePerson().build();
        personRepository.save(Arrays.asList(aPerson));

        aDoedsmelding = new Doedsmelding();
        aDoedsmelding.setPerson(aPerson);
        aDoedsmelding.setIsMeldingSent(MELDING_SENT);
    }

    @Test
    @Rollback
    public void findByPersonIdTest() {
        doedsmeldingTestRepository.save(aDoedsmelding);

        Doedsmelding result = doedsmeldingRepository.findByPersonId(aPerson.getId());

        assertThat(result, is(aDoedsmelding));
        assertThat(result.getPerson(), is(aPerson));
        assertThat(result.getIsMeldingSent(), is(MELDING_SENT));
    }

    @Test
    @Rollback
    public void saveTest() {
        doedsmeldingRepository.save(Arrays.asList(aDoedsmelding));

        Doedsmelding result = doedsmeldingTestRepository.findAll().iterator().next();
        assertThat(result, is(aDoedsmelding));
        assertThat(result.getPerson(), is(aPerson));
        assertThat(result.getIsMeldingSent(), is(MELDING_SENT));
    }

    @Test
    @Rollback
    public void deleteByPersonIdInTest() {
        doedsmeldingTestRepository.save(Arrays.asList(aDoedsmelding));

        doedsmeldingRepository.deleteByPersonIdIn(Arrays.asList(aPerson.getId()));

        Doedsmelding result = doedsmeldingTestRepository.findOne(aDoedsmelding.getId());
        assertThat(result, is(nullValue()));
    }
}
