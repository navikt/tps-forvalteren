package no.nav.tps.vedlikehold.service.command.authorisation.strategy;

import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.ServiceRutineAuthorisationStrategy;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.WriteServiceRutineAuthorisation;
import no.nav.tps.vedlikehold.service.command.exceptions.HttpUnauthorisedException;
import no.nav.tps.vedlikehold.service.user.UserRole;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class DefaultWriteSecurityStrategy implements WriteSecurityStrategy{

    @Autowired
    private MessageProvider messageProvider;

    @Override
    public boolean isSupported(ServiceRutineAuthorisationStrategy a1) {
        return a1 instanceof WriteServiceRutineAuthorisation;
    }

    @Override
    public void handleUnauthorised() {
        throw new HttpUnauthorisedException(messageProvider.get("rest.service.request.exception.Unauthorized"), "api/v1/service/");
    }

    @Override
    public boolean isAuthorised(Set<UserRole> userRoles) {
        return userRoles.contains(UserRole.ROLE_ACCESS);
    }
}
