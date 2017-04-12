package no.nav.tps.vedlikehold.service.command.authorisation.strategy;

import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.ReadServiceRutineAuthorisation;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.ServiceRutineAuthorisationStrategy;
import no.nav.tps.vedlikehold.service.command.exceptions.HttpUnauthorisedException;
import no.nav.tps.vedlikehold.service.user.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DefaultReadSecurityStrategy implements ReadSecurityStrategy {

    private static final UserRole requiredReadRole = UserRole.ROLE_ACCESS;

    @Autowired
    private MessageProvider messageProvider;

    @Override
    public boolean isSupported(ServiceRutineAuthorisationStrategy a1) {
        return a1 instanceof ReadServiceRutineAuthorisation;
    }

    @Override
    public void handleUnauthorised() {
        throw new HttpUnauthorisedException(messageProvider.get("rest.service.request.exception.Unauthorized"), "api/v1/service/");
    }

    @Override
    public boolean isAuthorised(Set<UserRole> userRoles) {
        return userRoles.contains(requiredReadRole);
    }
}
