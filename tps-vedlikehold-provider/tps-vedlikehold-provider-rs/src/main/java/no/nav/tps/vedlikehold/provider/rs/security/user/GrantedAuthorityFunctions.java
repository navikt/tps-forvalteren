package no.nav.tps.vedlikehold.provider.rs.security.user;

import com.google.common.base.Function;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
public class GrantedAuthorityFunctions {

    /**
     * Maps a {@link @UserRole} to the String-representation of a {@link @UserRole}.
     */
    public static Function<GrantedAuthority, String> toStringRepresentation() {
        return grantedAuthority -> {
            if (grantedAuthority != null) {
                return grantedAuthority.getAuthority();
            }
            return null;
        };
    }

    /**
     * Maps the String-representation of a {@link @UserRole} to a {@link @UserRole}.
     */
    public static Function<GrantedAuthority, UserRole> toUserRole(final Map<String, UserRole> roles) {
        return grantedAuthority -> {
            if (grantedAuthority != null) {
                return roles.get(grantedAuthority.getAuthority());
            }
            return null;
        };
    }
}