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
    ROLE_WRITE_Q,
    ROLE_READ_U,
    ROLE_WRITE_U,
    ROLE_READ_P,
    ROLE_WRITE_P,
    ROLE_READ_O,
    ROLE_WRITE_O;

    @Override
    public String getAuthority() {
        return name();
    }
}
