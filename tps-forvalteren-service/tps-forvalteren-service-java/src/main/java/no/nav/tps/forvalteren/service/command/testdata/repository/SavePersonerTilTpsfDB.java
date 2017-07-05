package no.nav.tps.forvalteren.service.command.testdata.repository;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SavePersonerTilTpsfDB {

    @Autowired
    private PersonRepository personRepository;

    public void execute(List<Person> personer){
        personRepository.save(personer);
    }
}
