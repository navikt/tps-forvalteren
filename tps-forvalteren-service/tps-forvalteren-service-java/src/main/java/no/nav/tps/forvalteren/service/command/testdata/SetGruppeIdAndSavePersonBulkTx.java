package no.nav.tps.forvalteren.service.command.testdata;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.service.command.testdata.opprett.SetGruppeIdOnPersons;

@Service
public class SetGruppeIdAndSavePersonBulkTx {

    @Autowired
    private SetGruppeIdOnPersons setGruppeIdOnPersons;

    @Autowired
    private SavePersonBulk savePersonBulk;
    
    @Transactional
    public void execute(List<Person> personerSomSkalPersisteres, Long gruppeId) {
        setGruppeIdOnPersons.setGruppeId(personerSomSkalPersisteres, gruppeId);
        savePersonBulk.execute(personerSomSkalPersisteres);
    }
    
}
