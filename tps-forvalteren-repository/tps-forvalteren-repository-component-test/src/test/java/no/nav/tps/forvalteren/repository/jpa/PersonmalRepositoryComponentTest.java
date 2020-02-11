package no.nav.tps.forvalteren.repository.jpa;

import static no.nav.tps.forvalteren.domain.test.provider.PersonmalProvider.personmalA;
import static no.nav.tps.forvalteren.domain.test.provider.PersonmalProvider.personmalB;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import no.nav.tps.forvalteren.domain.jpa.Personmal;
import no.nav.tps.forvalteren.repository.jpa.config.RepositoryTestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RepositoryTestConfig.class)
@Transactional
public class PersonmalRepositoryComponentTest {

    @Autowired
    private PersonmalRepository personmalRepository;

    @Autowired
    private PersonmalTestRepository personmalTestRepository;

    @Test
    @Rollback
    public void savePersonmal() {
        Personmal personmal = personmalA().build();
        personmalRepository.save(personmal);

        Personmal resultPersonmal = personmalTestRepository.findById(personmal.getId()).get();

        assertThat(resultPersonmal, is(personmal));
    }

    @Test
    @Rollback
    public void deletePersonmal() {
        Personmal personmal = personmalTestRepository.save(personmalA().build());

        personmalRepository.deleteById(personmal.getId());

        Optional<Personmal> resultPersonmal = personmalTestRepository.findById(personmal.getId());

        assertThat(resultPersonmal.isPresent(), is(false));
    }

    @Test
    @Rollback
    public void findPersonmal() {
        Personmal personmal = personmalTestRepository.save(personmalA().build());
        Personmal resultPersonmal = personmalRepository.findById(personmal.getId());

        assertThat(resultPersonmal, is(personmal));

    }

    @Test
    @Rollback
    public void findAllPersonmal() {
        Personmal personmalA = personmalTestRepository.save(personmalA().build());
        Personmal personmalB = personmalTestRepository.save(personmalB().build());

        List<Personmal> result = personmalRepository.findAll();

        assertThat(result, contains(personmalA, personmalB));
    }
}
