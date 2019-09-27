package no.nav.tps.forvalteren.service.command.testdata.restreq;

import static java.util.Collections.singletonList;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.jpa.IdentHistorikk;
import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsIdenthistorikkKriterium;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;

@Service
public class IdenthistorikkService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @Transactional
    public Person save(String ident, List<Person> duplicatedPersons, List<RsIdenthistorikkKriterium> identhistorikk) {

        Person mainPerson = personRepository.findByIdent(ident);

        personService.deleteIdenthistorikk(singletonList(mainPerson));

        for (int i = 0; i < duplicatedPersons.size(); i++) {
            mainPerson.getIdentHistorikk().add(IdentHistorikk.builder()
                    .person(mainPerson)
                    .aliasPerson(duplicatedPersons.get(i))
                    .historicIdentOrder(i + 1)
                    .regdato(identhistorikk.get(i).getRegdato())
                    .build());
            duplicatedPersons.get(i).getIdentHistorikk().add(IdentHistorikk.builder()
                    .person(duplicatedPersons.get(0))
                    .aliasPerson(mainPerson)
                    .historicIdentOrder(1)
                    .regdato(identhistorikk.get(i).getRegdato())
                    .build());
        }

        return personRepository.save(mainPerson);
    }
}
