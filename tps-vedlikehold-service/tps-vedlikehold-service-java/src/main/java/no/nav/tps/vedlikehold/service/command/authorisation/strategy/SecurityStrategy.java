package no.nav.tps.vedlikehold.service.command.authorisation.strategy;

import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.ServiceRutineAuthorisationStrategy;

public interface SecurityStrategy {
    boolean isSupported(ServiceRutineAuthorisationStrategy a1);
    void handleUnauthorised();

}
