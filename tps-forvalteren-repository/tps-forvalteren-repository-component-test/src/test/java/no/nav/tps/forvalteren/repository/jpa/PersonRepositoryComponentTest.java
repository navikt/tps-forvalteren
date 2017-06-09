package no.nav.tps.forvalteren.repository.jpa;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.config.RepositoryTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aFemalePerson;
import static no.nav.tps.forvalteren.domain.test.provider.PersonProvider.aMalePerson;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RepositoryTestConfig.class)
@Transactional
public class PersonRepositoryComponentTest {

    private Person personOla = aMalePerson().build();
    private Person personKari = aFemalePerson().build();

    @Autowired
    private PersonTestRepository testRepository;

    @Autowired
    private PersonRepository repository;

    @Test
    @Rollback
    public void findAllReturnsAll() {
        testRepository.save(personOla);
        testRepository.save(personKari);

        List<Person> result = repository.findAllByOrderByIdAsc();

        assertThat(result, hasSize(2));
        assertThat(result, hasItem(personOla));
        assertThat(result, hasItem(personKari));
    }

    @Test
    @Rollback
    public void deleteByIdInDeletesAll() {
        Person ola = testRepository.save(personOla);
        Person kari = testRepository.save(personKari);

        List<Long> ids = new ArrayList<>();
        ids.add(ola.getId());
        ids.add(kari.getId());
        repository.deleteByIdIn(ids);

        List<Person> result = repository.findAllByOrderByIdAsc();
        assertThat(result, hasSize(0));

    }

}
