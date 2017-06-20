package no.nav.tps.forvalteren.service.command.testdata.opprett;

import no.nav.tps.forvalteren.domain.jpa.Person;

import java.util.List;

public interface SetNameOnPersonsService {

    void execute(List<Person> personer);

}
