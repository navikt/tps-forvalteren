package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.InnvandringCreateSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;

import org.springframework.stereotype.Service;

@Service
public class InnvandringCreateSkdParameterStrategy extends InnvandringSkdParameterStrategy {

    private static final String TILDELINGSKODE_FOR_CREATE = "1";

    @Override
    public boolean isSupported(SkdParametersCreator creator) {
        return creator instanceof InnvandringCreateSkdParametere;
    }

    @Override
    public String hentTildelingskode() {
        return TILDELINGSKODE_FOR_CREATE;
    }
}
