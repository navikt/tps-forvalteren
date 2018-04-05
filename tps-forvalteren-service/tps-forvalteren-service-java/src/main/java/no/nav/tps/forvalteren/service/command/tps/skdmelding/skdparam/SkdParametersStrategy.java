package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam;

import no.nav.tps.forvalteren.domain.jpa.Person;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;
import no.nav.tps.forvalteren.service.command.testdata.skd.SkdMeldingTrans1;

public interface SkdParametersStrategy {

    String hentTildelingskode();

    boolean isSupported(SkdParametersCreator creator);
    
    SkdMeldingTrans1 execute(Person person);
}
