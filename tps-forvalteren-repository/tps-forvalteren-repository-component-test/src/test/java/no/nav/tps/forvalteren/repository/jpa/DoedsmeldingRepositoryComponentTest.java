package no.nav.tps.forvalteren.repository.jpa;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.List;
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

    private Person person1;
    private Person person2;
    private Doedsmelding doedsmelding1;
    private Doedsmelding doedsmelding2;
    private List<Doedsmelding> doedsmeldinger;

    @Before
    public void setup() {
        person1 = PersonProvider.aMalePerson().build();
        person2 = PersonProvider.aFemalePerson().build();
        personRepository.save(Arrays.asList(person1, person2));

        doedsmelding1 = new Doedsmelding();
        doedsmelding1.setPerson(person1);
        doedsmelding1.setIsMeldingSent(MELDING_SENT);

        doedsmelding2 = new Doedsmelding();
        doedsmelding2.setPerson(person2);

        doedsmeldinger = Arrays.asList(doedsmelding1, doedsmelding2);
    }

    @Test
    @Rollback
    public void findByPersonIdTest() {
        doedsmeldingTestRepository.save(doedsmeldinger);

        Doedsmelding result = doedsmeldingRepository.findByPersonId(person1.getId());

        assertThat(result, is(doedsmelding1));
        assertThat(result.getPerson(), is(person1));
        assertThat(result.getIsMeldingSent(), is(MELDING_SENT));
        assertThat(result, not(is(doedsmelding2)));
    }

    @Test
    @Rollback
    public void saveTest() {
        doedsmeldingRepository.save(doedsmeldinger);

        List<Doedsmelding> results = (List<Doedsmelding>) doedsmeldingTestRepository.findAll();

        assertThat(results.size(), is(equalTo(doedsmeldinger.size())));
        assertThat(results, hasItem(doedsmelding1));
        assertThat(results, hasItem(doedsmelding2));
    }

    @Test
    @Rollback
    public void deleteOneByPersonIdInTest() {
        doedsmeldingTestRepository.save(doedsmeldinger);
        List<Long> idsToDelete = Arrays.asList(person1.getId());

        doedsmeldingRepository.deleteByPersonIdIn(idsToDelete);

        List<Doedsmelding> results = (List<Doedsmelding>) doedsmeldingTestRepository.findAll();
        assertThat(results.size(), is(equalTo(doedsmeldinger.size() - idsToDelete.size())));
        assertThat(results, not(hasItem(doedsmelding1)));
        assertThat(results, hasItem(doedsmelding2));
    }

    @Test
    @Rollback
    public void deleteMultipleByPersonIdInTest() {
        doedsmeldingTestRepository.save(doedsmeldinger);
        List<Long> idsToDelete = Arrays.asList(person1.getId(), person2.getId());

        doedsmeldingRepository.deleteByPersonIdIn(idsToDelete);

        List<Doedsmelding> results = (List<Doedsmelding>) doedsmeldingTestRepository.findAll();
        assertThat(results.size(), is(equalTo(doedsmeldinger.size() - idsToDelete.size())));
        assertThat(results, not(hasItem(doedsmelding1)));
        assertThat(results, not(hasItem(doedsmelding2)));
    }
}
