package no.nav.tps.forvalteren.service.command.testdata.opprett;

import no.nav.tps.forvalteren.domain.jpa.Person;

import java.util.List;

@FunctionalInterface
public interface SetGruppeIdOnPersons {

    void setGruppeId(List<Person> persons, Long gruppeId);

}
