package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeletePersonerByIdIn {

    @Autowired
    PersonRepository personRepository;

    public void execute(List<Long> ids){
        personRepository.deleteByIdIn(ids);
    }

}
