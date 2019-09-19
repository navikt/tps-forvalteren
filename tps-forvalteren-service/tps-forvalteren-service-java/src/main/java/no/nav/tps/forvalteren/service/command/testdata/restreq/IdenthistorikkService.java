package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Collections.singletonList;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.IdentHistorikk;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;

@Service
public class IdenthistorikkService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @Transactional
    public Person save(String ident, List<Person> duplicatedPersons) {

        Person mainPerson = personRepository.findByIdent(ident);

        personService.deleteIdenthistorikk(singletonList(mainPerson));

        AtomicInteger order = new AtomicInteger(0);
        duplicatedPersons.forEach(aliasPerson -> {
            mainPerson.getIdentHistorikk().add(IdentHistorikk.builder()
                    .person(mainPerson)
                    .aliasPerson(aliasPerson)
                    .historicIdentOrder(order.addAndGet(1))
                    .build());
            aliasPerson.getIdentHistorikk().add(IdentHistorikk.builder()
                    .person(aliasPerson)
                    .aliasPerson(mainPerson)
                    .historicIdentOrder(1)
                    .build());
        });

        return personRepository.save(mainPerson);
    }
}
