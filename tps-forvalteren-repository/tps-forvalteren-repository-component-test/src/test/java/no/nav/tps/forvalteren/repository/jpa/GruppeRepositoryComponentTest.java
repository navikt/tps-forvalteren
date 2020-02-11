package no.nav.tps.forvalteren.repository.jpa;

import static no.nav.tps.forvalteren.domain.test.provider.GruppeProvider.aGruppe;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import no.nav.tps.forvalteren.domain.jpa.Gruppe;
import no.nav.tps.forvalteren.repository.jpa.config.RepositoryTestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RepositoryTestConfig.class)
@Transactional
public class GruppeRepositoryComponentTest {

    private Gruppe gruppe = aGruppe().build();
    private Gruppe gruppe2 = aGruppe().navn("Testdata 2").beskrivelse("Testdata for 2").build();

    @Autowired
    private GruppeTestRepository testRepository;

    @Autowired
    private GruppeRepository repository;

    @Test
    @Rollback
    public void findAllReturnsAll() {
        testRepository.save(gruppe);
        testRepository.save(gruppe2);

        List<Gruppe> result = repository.findAllByOrderByIdAsc();

        assertThat(result, hasSize(2));
        assertThat(result, hasItem(gruppe));
        assertThat(result, hasItem(gruppe2));
    }

    @Test
    @Rollback
    public void findById() {
        Gruppe gruppeMedId = testRepository.save(gruppe);

        Gruppe result = repository.findById(gruppeMedId.getId());

        assertThat(result.getId(), is(gruppe.getId()));
        assertThat(result.getNavn(), is(gruppe.getNavn()));
        assertThat(result.getBeskrivelse(), is(gruppe.getBeskrivelse()));
    }

    @Test
    @Rollback
    public void save() {
        Gruppe gruppeMedId = repository.save(gruppe);

        Gruppe result = testRepository.findById(gruppeMedId.getId()).get();

        assertThat(result.getId(), is(gruppe.getId()));
        assertThat(result.getNavn(), is(gruppe.getNavn()));
        assertThat(result.getBeskrivelse(), is(gruppe.getBeskrivelse()));
    }

    @Test
    @Rollback
    public void deleteById() {
        Gruppe savedGruppe = testRepository.save(gruppe);

        repository.deleteById(savedGruppe.getId());

        Gruppe result = repository.findById(savedGruppe.getId());

        assertThat(result, is(nullValue()));
    }

}
