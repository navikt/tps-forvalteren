package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.Set;

@FunctionalInterface
public interface SendSkdMeldingTilGitteMiljoer {

    void execute(String skdMelding, Set<String> environments);

}
