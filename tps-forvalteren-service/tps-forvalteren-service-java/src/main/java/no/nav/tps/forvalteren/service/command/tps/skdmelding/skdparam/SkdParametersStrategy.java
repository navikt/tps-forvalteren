package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam;


import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;

import java.util.Map;

public interface SkdParametersStrategy {

    boolean isSupported(SkdParametersCreator creator);

    Map<String,String> execute(Person person);
}
