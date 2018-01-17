package no.nav.tps.forvalteren.service.command.testdata.opprett;

import java.util.List;

import no.nav.tps.forvalteren.domain.jpa.Person;

@FunctionalInterface
public interface SetDummyAdresseOnPersons {
    
    void execute(List<Person> persons);
}
