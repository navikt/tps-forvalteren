package no.nav.tps.vedlikehold.provider.rs.security.user;

import com.google.common.base.Function;
import org.springframework.security.core.GrantedAuthority;

import java.util.Map;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
public class GrantedAuthorityFunctions {

    /**
     * Maps a {@link @UserRole} to the String-representation of a {@link @UserRole}.
     */
    public static Function<GrantedAuthority, String> toStringRepresentation() {
        return new Function<GrantedAuthority, String>() {
            @Override
            public String apply(GrantedAuthority ga) {
                if (ga != null) {
                    return ga.getAuthority();
                }
                return null;
            }
        };
    }

    /**
     * Maps the String-representation of a {@link @UserRole} to a {@link @UserRole}.
     */
    public static Function<GrantedAuthority, UserRole> toUserRole(final Map<String, UserRole> roles) {
        return new Function<GrantedAuthority, UserRole>() {
            @Override
            public UserRole apply(GrantedAuthority ga) {
                if (ga != null) {
                    return roles.get(ga.getAuthority());
                }
                return null;
            }
        };
    }
}