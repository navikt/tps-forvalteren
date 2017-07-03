package no.nav.tps.forvalteren.service.command.testdata.skd.utils;

import no.nav.tps.forvalteren.domain.jpa.Person;

import java.util.Map;

public interface PersonToSkdParametersMapper {

    Map<String, String> create(Person person);

    Map<String, String> update(Person person);

}
