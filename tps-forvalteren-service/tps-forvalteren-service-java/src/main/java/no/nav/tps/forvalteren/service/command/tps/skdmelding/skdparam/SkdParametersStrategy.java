package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam;

import java.util.Map;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;

public interface SkdParametersStrategy {

    String hentTildelingskode();

    boolean isSupported(SkdParametersCreator creator);

    Map<String,String> execute(Person person);
}
