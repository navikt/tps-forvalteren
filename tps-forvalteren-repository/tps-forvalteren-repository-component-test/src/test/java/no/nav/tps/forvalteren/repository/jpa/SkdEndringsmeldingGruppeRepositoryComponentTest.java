package no.nav.tps.forvalteren.repository.jpa;

import static no.nav.tps.forvalteren.domain.test.provider.SkdEndringsmeldingGruppeProvider.aSkdEndringsmeldingGruppe;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

import java.util.List;
import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.repository.jpa.config.RepositoryTestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RepositoryTestConfig.class)
@Transactional
public class SkdEndringsmeldingGruppeRepositoryComponentTest {

    @Autowired
    private SkdEndringsmeldingGruppeTestRepository testRepository;

    @Autowired
    private SkdEndringsmeldingGruppeRepository repository;

    private SkdEndringsmeldingGruppe gruppe = aSkdEndringsmeldingGruppe().build();

    @Test
    @Rollback
    public void findAllByOrderByIdAsc() {
        SkdEndringsmeldingGruppe gruppe1 = testRepository.save(gruppe);
        SkdEndringsmeldingGruppe gruppe2 = testRepository.save(aSkdEndringsmeldingGruppe().navn("test2").build());
        SkdEndringsmeldingGruppe gruppe3 = testRepository.save(aSkdEndringsmeldingGruppe().navn("test3").build());

        List<SkdEndringsmeldingGruppe> result = repository.findAllByOrderByIdAsc();

        assertThat(result, hasSize(3));
        assertThat(result.get(0), is(gruppe1));
        assertThat(result.get(1), is(gruppe2));
        assertThat(result.get(2), is(gruppe3));
    }

    @Test
    @Rollback
    public void findById() {
        SkdEndringsmeldingGruppe storedGruppe = testRepository.save(gruppe);

        SkdEndringsmeldingGruppe result = repository.findById(storedGruppe.getId());
        
        assertThat(storedGruppe, is(result));
    }
    
    @Test
    @Rollback
    public void save() {
        SkdEndringsmeldingGruppe storedGruppe = repository.save(gruppe);

        SkdEndringsmeldingGruppe result = testRepository.findOne(storedGruppe.getId());
        
        assertThat(result, is(storedGruppe));
    }
    
    @Test
    @Rollback
    public void deleteById() {
        SkdEndringsmeldingGruppe storedGruppe = testRepository.save(gruppe);

        repository.deleteById(storedGruppe.getId());

        SkdEndringsmeldingGruppe result = testRepository.findOne(storedGruppe.getId());

        assertThat(result, is(nullValue()));
    }

}
