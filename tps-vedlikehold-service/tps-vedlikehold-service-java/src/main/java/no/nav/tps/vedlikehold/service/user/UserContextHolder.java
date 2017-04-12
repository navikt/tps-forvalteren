package no.nav.tps.vedlikehold.service.user;

import no.nav.tps.vedlikehold.domain.service.user.User;

import java.util.Set;

/**
 * Abstraction of the user context for testability and reusability
 */

public interface UserContextHolder {
    String getDisplayName();
    String getUsername();

    boolean isAuthenticated();

    User getUser();

    Set<UserRole> getRoles();

}
