package no.nav.tps.forvalteren.service.command.authorisation.strategy;

import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ServiceRutineAuthorisationStrategy;

public interface SecurityStrategy {
    boolean isSupported(ServiceRutineAuthorisationStrategy a1);
    void handleUnauthorised();

}
