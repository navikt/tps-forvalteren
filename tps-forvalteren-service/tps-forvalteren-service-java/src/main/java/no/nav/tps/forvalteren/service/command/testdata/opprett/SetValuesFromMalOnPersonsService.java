package no.nav.tps.forvalteren.service.command.testdata.opprett;

import java.util.List;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPersonMal;

@FunctionalInterface
public interface SetValuesFromMalOnPersonsService {
    void execute(List<Person> personer, RsPersonMal rsPersonMal);
}
