package no.nav.tps.vedlikehold.service.command.authorisation.strategies;

import java.util.HashSet;
import java.util.Set;

import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class ReadAuthorisationServiceStrategy implements AuthorisationServiceStrategy {

    private User user;
    private Set<String> readRoles;

    /**
     * Find the intersection between the user's roles, and the authorised roles.
     * If the intersection is nonempty, the user is authorised.
     *
     * @return <code>Boolean</code> indicating whether the user is authorised
     */
    @Override
    public boolean isAuthorised() {
        Set<String> userRoles = new HashSet<>(user.getRoles());               // retainAll() should not modify the users objects roles

        /* Retain all roles present in both authorised roles, and the users roles */
        userRoles.retainAll(readRoles);

        return !userRoles.isEmpty();
    }


    public void setReadRoles(Set<String> readRoles) {
        this.readRoles = readRoles;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
