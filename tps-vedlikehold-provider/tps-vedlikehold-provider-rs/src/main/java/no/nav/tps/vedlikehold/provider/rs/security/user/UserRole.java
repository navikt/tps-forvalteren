package no.nav.tps.vedlikehold.provider.rs.security.user;

import org.springframework.security.core.GrantedAuthority;

/**
 * System user roles
 *
 * @author Tobias Hansen (Visma Consulting AS).
 */
public enum UserRole implements GrantedAuthority {

    ROLE_READ_T,
    ROLE_WRITE_T,
    ROLE_READ_Q,
    ROLE_WRITE_Q;

    @Override
    public String getAuthority() {
        return name();
    }
}
