package no.nav.tps.forvalteren.repository.jpa;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;

import java.util.List;
import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import no.nav.tps.forvalteren.domain.jpa.SkdEndringsmeldingLogg;
import no.nav.tps.forvalteren.domain.test.provider.SkdEndringsmeldingLoggProvider;
import no.nav.tps.forvalteren.repository.jpa.config.RepositoryTestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RepositoryTestConfig.class)
@Transactional
public class SkdEndringsmeldingLoggRepositoryComponentTest {

    @Autowired
    private SkdEndringsmeldingLoggTestRepository testRepository;

    @Autowired
    private SkdEndringsmeldingLoggRepository repository;

    private SkdEndringsmeldingLogg logg = SkdEndringsmeldingLoggProvider.aSkdEndringsmeldingLogg().build();

    @Test
    @Rollback
    public void save() {
        SkdEndringsmeldingLogg storedLogg = repository.save(logg);

        SkdEndringsmeldingLogg result = testRepository.findById(storedLogg.getId()).get();
        
        assertThat(result, is(storedLogg));
    }
    
    @Test
    @Rollback
    public void findAllByMeldingsgruppeId() {
        Long gruppeId = 1L;
        SkdEndringsmeldingLogg logg1 = testRepository.save(logg.toBuilder().meldingsgruppeId(gruppeId).build());
        SkdEndringsmeldingLogg logg2 = testRepository.save(logg.toBuilder().meldingsgruppeId(gruppeId).build());

        List<SkdEndringsmeldingLogg> result = repository.findAllByMeldingsgruppeId(gruppeId);
        
        assertThat(result, hasSize(2));
        assertThat(result, hasItem(logg1));
        assertThat(result, hasItem(logg2));
    }
}
