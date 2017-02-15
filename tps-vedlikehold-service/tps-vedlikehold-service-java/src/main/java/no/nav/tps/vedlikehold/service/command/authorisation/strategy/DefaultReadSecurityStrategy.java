package no.nav.tps.vedlikehold.service.command.authorisation.strategy;

import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.AuthorisationStrategy;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.ReadAuthorisation;
import no.nav.tps.vedlikehold.service.command.authorisation.RolesService;
import no.nav.tps.vedlikehold.service.command.exceptions.HttpUnauthorisedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DefaultReadSecurityStrategy implements ReadSecurityStrategy {

    @Autowired
    private RolesService rolesService;

    @Autowired
    private MessageProvider messageProvider;

    @Override
    public boolean isSupported(AuthorisationStrategy a1) {
        return a1 instanceof ReadAuthorisation;
    }

    @Override
    public void handleUnauthorised(Set<String> userRoles, String environment) {
        throw new HttpUnauthorisedException(messageProvider.get("rest.service.request.exception.Unauthorized"), "api/v1/service/");
    }

    @Override
    public boolean isAuthorised(Set<String> roles, String environment) {
        Set<String> rolesRequiredForEnvironment = rolesService.getRolesForEnvironment(environment, RolesService.RoleType.READ);
        return roles.containsAll(rolesRequiredForEnvironment);
    }
}
