package no.nav.tps.forvalteren.repository.jpa;

import static no.nav.tps.forvalteren.domain.test.provider.SkdEndringsmeldingGruppeProvider.aSkdEndringsmeldingGruppe;
import static no.nav.tps.forvalteren.domain.test.provider.SkdEndringsmeldingProvider.aSkdEndringsmelding;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsNull.nullValue;

import java.util.Arrays;
import java.util.List;
import javax.transaction.Transactional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmelding;
import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingGruppe;
import no.nav.tps.forvalteren.repository.jpa.config.RepositoryTestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RepositoryTestConfig.class)
@Transactional
public class SkdEndringsmeldingRepositoryComponentTest {

    @Autowired
    private SkdEndringsmeldingTestRepository testRepository;

    @Autowired
    private SkdEndringsmeldingGruppeTestRepository skdEndringsmeldingGruppeTestRepository;

    @Autowired
    private SkdEndringsmeldingRepository repository;

    private SkdEndringsmelding skdEndringsmelding = aSkdEndringsmelding().build();
    private SkdEndringsmeldingGruppe gruppe = aSkdEndringsmeldingGruppe().build();

    @Before
    public void setup() {
        SkdEndringsmeldingGruppe storedGruppe = skdEndringsmeldingGruppeTestRepository.save(gruppe);
        skdEndringsmelding.setGruppe(storedGruppe);
    }

    @Test
    @Rollback
    public void findById() {
        SkdEndringsmelding storedSkdEndringsmelding = testRepository.save(skdEndringsmelding);

        SkdEndringsmelding result = repository.findById(storedSkdEndringsmelding.getId());

        assertThat(result, is(storedSkdEndringsmelding));
    }

    @Test
    @Rollback
    public void save() {
        SkdEndringsmelding storedSkdEndringsmelding = repository.save(skdEndringsmelding);

        SkdEndringsmelding result = testRepository.findOne(storedSkdEndringsmelding.getId());

        assertThat(result, is(storedSkdEndringsmelding));
    }

    @Test
    @Rollback
    public void deleteByIdIn() {
        SkdEndringsmelding storedSkdEndringsmelding = testRepository.save(skdEndringsmelding);

        repository.deleteByIdIn(Arrays.asList(storedSkdEndringsmelding.getId()));

        SkdEndringsmelding result = testRepository.findOne(storedSkdEndringsmelding.getId());

        assertThat(result, is(nullValue()));
    }

    @Test
    @Rollback
    public void findAllByGruppe() {
        SkdEndringsmelding storedSkdEndringsmelding1 = testRepository.save(skdEndringsmelding);
        SkdEndringsmelding storedSkdEndringsmelding2 = testRepository.save(aSkdEndringsmelding().gruppe(gruppe).build());
        SkdEndringsmelding storedSkdEndringsmelding3 = testRepository.save(aSkdEndringsmelding().gruppe(gruppe).build());

        List<SkdEndringsmelding> result = repository.findAllByGruppe(skdEndringsmelding.getGruppe());

        assertThat(result, hasSize(3));
        assertThat(result, hasItems(storedSkdEndringsmelding1, storedSkdEndringsmelding2, storedSkdEndringsmelding3));
    }
}
