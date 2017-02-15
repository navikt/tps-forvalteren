package no.nav.tps.vedlikehold.service.command.testdata.testperson;

import no.nav.tps.vedlikehold.domain.repository.jpa.repoes.TPSKTestPersonRepository;
import no.nav.tps.vedlikehold.domain.service.jpa.TPSKTestPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Peter Fløgstad on 07.02.2017.
 */

@Service
public class TestPersonCreator {

    @Autowired
    private TPSKTestPersonRepository testPersonRepository;

    public void createTestPerson(TPSKTestPerson testPerson){
        testPersonRepository.save(testPerson);
    }

}
