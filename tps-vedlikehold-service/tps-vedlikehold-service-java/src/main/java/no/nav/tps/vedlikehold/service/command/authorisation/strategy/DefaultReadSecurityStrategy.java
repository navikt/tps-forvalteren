package no.nav.tps.vedlikehold.service.command.authorisation.strategy;

import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.AuthorisationStrategy;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.ReadAuthorisationStrategy;
import no.nav.tps.vedlikehold.service.command.authorisation.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Created by f148888 on 08.11.2016.
 */

@Component
public class DefaultReadSecurityStrategy implements ReadSecurityStrategy {

    @Autowired
    private RolesService rolesService;

    @Override
    public boolean isSupported(AuthorisationStrategy a1) {
        return a1 instanceof ReadAuthorisationStrategy;
    }

    @Override
    public boolean authorise(Set<String> userRoles, String environment) {
        Set<String> rolesRequiredForEnvironment = rolesService.getRolesForEnvironment(environment, RolesService.RoleType.READ);
        userRoles.add("${tps.vedlikehold.securityBuilder.t.readroles}");    //TODO Hack for å få den til å være authorisert uten riktig role.

        // Retain all roles present in both authorised roles, and the users roles /
        userRoles.retainAll(rolesRequiredForEnvironment);
        return !userRoles.isEmpty();
    }
}
