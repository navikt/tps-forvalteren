package no.nav.tps.forvalteren.service.command.testdata.opprett;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FindIdenterNotUsedInDB {

    @Autowired
    private PersonRepository repository;

    public Set<String> filtrer(Set<String> identer) {
        List<Person> personerSomFinnes = repository.findByIdentIn(new ArrayList<>(identer));
        List<String> opptatteIdenter = personerSomFinnes.stream()
                .map(Person::getIdent)
                .collect(Collectors.toList());

        identer.removeAll(opptatteIdenter);
        return identer;
    }

}
