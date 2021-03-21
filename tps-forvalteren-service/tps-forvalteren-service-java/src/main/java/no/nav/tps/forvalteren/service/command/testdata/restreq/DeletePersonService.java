package no.nav.tps.forvalteren.service.command.testdata.restreq;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import no.nav.tps.forvalteren.repository.jpa.DoedsmeldingRepository;
import no.nav.tps.forvalteren.repository.jpa.FullmaktRepository;
import no.nav.tps.forvalteren.repository.jpa.IdenthistorikkRepository;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.repository.jpa.RelasjonRepository;
import no.nav.tps.forvalteren.repository.jpa.SivilstandRepository;
import no.nav.tps.forvalteren.repository.jpa.VergemaalRepository;

@Service
public class DeletePersonService {

    // single TransactionTemplate shared amongst all methods in this instance
    private TransactionTemplate transactionTemplate;

    private SivilstandRepository sivilstandRepository;
    private RelasjonRepository relasjonRepository;
    private VergemaalRepository vergemaalRepository;
    private FullmaktRepository fullmaktRepository;
    private IdenthistorikkRepository identhistorikkRepository;
    private DoedsmeldingRepository doedsmeldingRepository;
    private PersonRepository personRepository;

    public DeletePersonService(PlatformTransactionManager transactionManager,
            SivilstandRepository sivilstandRepository,
            RelasjonRepository relasjonRepository,
            VergemaalRepository vergemaalRepository,
            FullmaktRepository fullmaktRepository,
            IdenthistorikkRepository identhistorikkRepository,
            DoedsmeldingRepository doedsmeldingRepository,
            PersonRepository personRepository) {

        this.transactionTemplate =  new TransactionTemplate(transactionManager);
        this.sivilstandRepository = sivilstandRepository;
        this.relasjonRepository = relasjonRepository;
        this.vergemaalRepository = vergemaalRepository;
        this.fullmaktRepository = fullmaktRepository;
        this.identhistorikkRepository = identhistorikkRepository;
        this.doedsmeldingRepository = doedsmeldingRepository;
        this.personRepository = personRepository;

        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
    }

    public void execute(Long id) {

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            protected void doInTransactionWithoutResult(TransactionStatus status) {
                sivilstandRepository.deleteByPersonRelasjonMedId(id);
                relasjonRepository.deleteByPersonRelasjonMedId(id);
                vergemaalRepository.deleteByVergeId(id);
                fullmaktRepository.deleteByFullmektigId(id);
                identhistorikkRepository.deleteByAliasPersonId(id);
                doedsmeldingRepository.deleteByPersonId(id);
                personRepository.deleteById(id);
            }
        });
    }
}
