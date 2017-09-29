package no.nav.tps.forvalteren.service.command.testdata.skd;

import java.util.Map;

@FunctionalInterface
public interface SkdOpprettSkdMeldingMedHeaderOgInnhold {

    String execute(Map<String, String> skdInputMap, SkdFelterContainer skdFelterContainer);
}
