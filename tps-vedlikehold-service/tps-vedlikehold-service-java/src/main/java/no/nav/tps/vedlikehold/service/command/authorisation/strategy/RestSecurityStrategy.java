package no.nav.tps.vedlikehold.service.command.authorisation.strategy;

import java.util.Set;

import no.nav.tps.vedlikehold.service.user.UserRole;

public interface RestSecurityStrategy extends SecurityStrategy{
    boolean isAuthorised(Set<UserRole> roles);
}
