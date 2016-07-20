package no.nav.tps.vedlikehold.service.command.user;

import java.util.Set;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public interface UserFactoryStrategy {
    String getDistinguishedName();
    String getUsername();
    String getToken();
    Set<String> getRoles();
}
