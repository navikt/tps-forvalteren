package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindAllPersonService {

    @Autowired
    private PersonRepository repository;

    public List<Person> execute(){
        return repository.findAllByOrderByIdAsc();
    }

}
