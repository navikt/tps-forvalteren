package no.nav.tps.forvalteren.service.command.testdata.opprett;

import java.util.List;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.rs.RsPersonMalRequest;

@FunctionalInterface
public interface SetValuesFromMalOnPersonsService {
    void execute(List<Person> personer, RsPersonMalRequest inputPersonRequest);
}
