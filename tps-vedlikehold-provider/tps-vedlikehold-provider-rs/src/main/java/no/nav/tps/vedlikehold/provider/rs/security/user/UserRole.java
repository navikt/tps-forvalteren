package no.nav.tps.vedlikehold.provider.rs.security.user;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public enum UserRole implements GrantedAuthority {
    READ,
    WRITE;

    @Override
    public String getAuthority() {
        return name();
    }
}
