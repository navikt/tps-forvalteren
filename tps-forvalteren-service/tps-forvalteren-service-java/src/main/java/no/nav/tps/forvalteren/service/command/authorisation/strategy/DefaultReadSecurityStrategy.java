package no.nav.tps.forvalteren.service.command.authorisation.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import no.nav.tps.forvalteren.common.java.message.MessageProvider;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ReadServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ServiceRutineAuthorisationStrategy;
import no.nav.tps.forvalteren.service.command.exceptions.HttpForbiddenException;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import no.nav.tps.forvalteren.service.user.UserRole;

@Component
public class DefaultReadSecurityStrategy implements ReadSecurityStrategy {

    private static final UserRole requiredReadRole = UserRole.ROLE_ACCESS;

    @Autowired
    private MessageProvider messageProvider;

    @Autowired
    private UserContextHolder userContextHolder;

    @Override
    public boolean isSupported(ServiceRutineAuthorisationStrategy a1) {
        return a1 instanceof ReadServiceRutineAuthorisation;
    }

    @Override
    public void handleForbiddenCall() {
        throw new HttpForbiddenException(messageProvider.get("rest.service.request.exception.Forbidden"));
    }

    @Override
    public boolean isAuthorised() {
        return userContextHolder.getRoles().contains(requiredReadRole);
    }
}
