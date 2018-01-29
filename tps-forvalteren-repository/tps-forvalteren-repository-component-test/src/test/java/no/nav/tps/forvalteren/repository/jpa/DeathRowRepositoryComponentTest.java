package no.nav.tps.forvalteren.repository.jpa;

import static no.nav.tps.forvalteren.domain.test.provider.DeathRowProvider.aDeathRow;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import no.nav.tps.forvalteren.domain.jpa.DeathRow;
import no.nav.tps.forvalteren.repository.jpa.config.RepositoryTestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RepositoryTestConfig.class)
@Transactional
public class DeathRowRepositoryComponentTest {

    @Autowired
    private DeathRowRepository deathRowRepository;

    @Autowired
    private DeathRowTestRepository deathRowTestRepository;

    @Test
    @Rollback
    public void findDeathRow() {
        DeathRow deathRow = deathRowTestRepository.save(aDeathRow().build());

        DeathRow result = deathRowRepository.findById(deathRow.getId());

        assertThat(result, is(deathRow));
    }

    @Test
    @Rollback
    public void saveDeathRow() {
        DeathRow deathRow = deathRowRepository.save(aDeathRow().build());

        DeathRow result = deathRowTestRepository.findOne(deathRow.getId());

        assertThat(result, is(deathRow));
    }

    @Test
    @Rollback
    public void deleteDeathRow() {
        DeathRow deathRow = deathRowTestRepository.save(aDeathRow().build());

        deathRowRepository.deleteById(deathRow.getId());

        DeathRow result = deathRowTestRepository.findOne(deathRow.getId());

        assertThat(result, is(nullValue()));
    }

}
