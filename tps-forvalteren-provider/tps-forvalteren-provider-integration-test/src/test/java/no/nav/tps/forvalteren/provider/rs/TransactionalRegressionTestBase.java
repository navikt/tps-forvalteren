package no.nav.tps.forvalteren.provider.rs;

import javax.transaction.Transactional;
import org.junit.After;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TestTransaction;

import no.nav.tps.forvalteren.repository.jpa.AdresseRepository;
import no.nav.tps.forvalteren.repository.jpa.DeathRowRepository;
import no.nav.tps.forvalteren.repository.jpa.DoedsmeldingRepository;
import no.nav.tps.forvalteren.repository.jpa.GruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingGruppeRepository;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingLoggRepository;
import no.nav.tps.forvalteren.repository.jpa.SkdEndringsmeldingRepository;

@Transactional
public class TransactionalRegressionTestBase {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    protected PersonRepository personRepository;

    @Autowired
    protected AdresseRepository adresseRepository;

    @Autowired
    protected GruppeRepository gruppeRepository;

    @Autowired
    protected RelasjonRepository relasjonRepository;

    @Autowired
    protected DoedsmeldingRepository doedsmeldingRepository;

    @Autowired
    protected DeathRowRepository deathRowRepository;

    @Autowired
    protected SkdEndringsmeldingGruppeRepository skdEndringsmeldingGruppeRepository;

    @Autowired
    protected SkdEndringsmeldingRepository skdEndringsmeldingRepository;

    @Autowired
    protected SkdEndringsmeldingLoggRepository skdEndringsmeldingLoggRepository;

    @After
    public void cleanDatabase() {
        if(TestTransaction.isActive()){
            TestTransaction.end();
        }

        TestTransaction.start();

        personRepository.deleteAll();
        adresseRepository.deleteAll();
        gruppeRepository.deleteAll();
        relasjonRepository.deleteAll();
        doedsmeldingRepository.deleteAll();
        deathRowRepository.deleteAll();
        skdEndringsmeldingGruppeRepository.deleteAll();
        skdEndringsmeldingRepository.deleteAll();
        skdEndringsmeldingLoggRepository.deleteAll();

        TestTransaction.flagForCommit();
        TestTransaction.end();

    }
}
