package no.nav.tps.forvalteren.service.command.testdata.opprett;

import no.nav.tps.forvalteren.domain.jpa.Person;

import java.util.Collection;
import java.util.List;

public interface OpprettPersoner {

    List<Person> execute(Collection<String> tilgjengeligIdenter);

}
