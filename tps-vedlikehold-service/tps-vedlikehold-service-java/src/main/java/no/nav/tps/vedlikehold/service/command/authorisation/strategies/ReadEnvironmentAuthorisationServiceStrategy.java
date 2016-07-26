package no.nav.tps.vedlikehold.service.command.authorisation.strategies;

import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;
import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class ReadEnvironmentAuthorisationServiceStrategy implements AuthorisationServiceStrategy {

    private User user;
    private String environment;

    private Map<String, Set<String>> environmentRolesMap = new HashMap<>();


    /**
     * Find the intersection between the user's roles, and the authorised roles in the current environment.
     * If the intersection is nonempty, the user is authorised.
     *
     * @return <code>Boolean</code> indicating whether the user is authorised
     */
    @Override
    public Boolean isAuthorised() {
        Set<String> userRoles       = new HashSet<>(user.getRoles());               // retainAll() should not modify the users objects roles
        Set<String> authorisedRoles = authorisedRoles();

        /* Retain all roles present in both authorised roles, and the users roles */
        userRoles.retainAll(authorisedRoles);

        return !userRoles.isEmpty();
    }


    public void addRoleForEnvironment(String role, String environment) {
        addRolesForEnvironment(singletonList(role), environment);
    }

    public void addRolesForEnvironment(Collection<String> roles, String environment) {
        if ( !environmentRolesMap.containsKey(environment) ) {
            environmentRolesMap.put(environment, new HashSet<>());
        }

        Set<String> environmentRoles = environmentRolesMap.get(environment);

        environmentRoles.addAll(roles);
    }


    public void setUser(User user) {
        this.user = user;
    }

    public void setEnvironment(String environment) {
        if (isEmpty(environment)) {
            this.environment = "";
            return;
        }

        this.environment = Character.toString( environment.charAt(0) ).toLowerCase();
    }

    /** Get all roles authorised to read in the current environment */
    private Set<String> authorisedRoles() {
        if ( !environmentRolesMap.containsKey(environment) ) {
            return emptySet();
        }

        return environmentRolesMap.get(environment);
    }
}
