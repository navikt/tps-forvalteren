package no.nav.tps.forvalteren.service.command.authorisation.strategy;

import org.springframework.beans.factory.annotation.Autowired;

import no.nav.tps.forvalteren.common.message.MessageProvider;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ServiceRutineAuthorisationStrategy;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.WriteServiceRutineAuthorisation;
import no.nav.tps.forvalteren.service.command.exceptions.HttpForbiddenException;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import no.nav.tps.forvalteren.service.user.UserRole;

public class DefaultWriteSecurityStrategy implements WriteSecurityStrategy{

    @Autowired
    private MessageProvider messageProvider;

    @Autowired
    private UserContextHolder userContextHolder;

    @Override
    public boolean isSupported(ServiceRutineAuthorisationStrategy a1) {
        return a1 instanceof WriteServiceRutineAuthorisation;
    }

    @Override
    public void handleForbiddenCall() {
        throw new HttpForbiddenException(messageProvider.get("rest.service.request.exception.Forbidden"));
    }

    @Override
    public boolean isAuthorised() {
        return userContextHolder.getRoles().contains(UserRole.ROLE_ACCESS);
    }
}
