package no.nav.tps.vedlikehold.service.command.authorisation.strategy;


import no.nav.tps.vedlikehold.service.user.UserRole;

import java.util.Set;

public interface SearchSecurityStrategy extends SecurityStrategy{
    boolean isAuthorised(Set<UserRole> roles, String fnr);
}
