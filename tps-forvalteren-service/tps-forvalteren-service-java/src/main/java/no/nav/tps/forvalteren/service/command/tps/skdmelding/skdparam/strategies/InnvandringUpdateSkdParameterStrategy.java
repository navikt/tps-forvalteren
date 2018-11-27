package no.nav.tps.forvalteren.service.command.tps.skdmelding.skdparam.strategies;

import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.InnvandringUpdateSkdParametere;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.SkdParametersCreator;

@Service
public class InnvandringUpdateSkdParameterStrategy extends InnvandringSkdParameterStrategy {

    private static final String TILDELINGSKODE_FOR_UPDATE = "2";

    @Override
    public boolean isSupported(SkdParametersCreator creator) {
        return creator instanceof InnvandringUpdateSkdParametere;
    }

    @Override
    public String hentTildelingskode() {
        return TILDELINGSKODE_FOR_UPDATE;
    }
}