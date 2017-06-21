package no.nav.tps.forvalteren.service.command.testdata.opprett;

import java.util.Set;

@FunctionalInterface
public interface FindIdenterNotUsedInDB {

    Set<String> filtrer(Set<String> identer);

}
