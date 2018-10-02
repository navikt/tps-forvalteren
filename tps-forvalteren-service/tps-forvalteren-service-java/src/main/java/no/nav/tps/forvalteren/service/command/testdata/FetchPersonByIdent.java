package no.nav.tps.forvalteren.service.command.testdata;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.repository.jpa.PersonRepository;
import no.nav.tps.forvalteren.service.command.exceptions.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FetchPersonByIdent {

    @Autowired
    private PersonRepository repository;

    public Person execute(String ident) {
        Person person = repository.findByIdent(ident);

        if (person == null) {
            throw new NotFoundException("Kunne ikke finne ident: " + ident + " i tabell T_PERSON");
        }

        return person;
    }
}
