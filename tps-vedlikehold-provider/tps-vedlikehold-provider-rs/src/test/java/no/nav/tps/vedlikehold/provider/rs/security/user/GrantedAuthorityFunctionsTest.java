package no.nav.tps.vedlikehold.provider.rs.security.user;

import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static no.nav.tps.vedlikehold.provider.rs.security.user.GrantedAuthorityFunctions.toStringRepresentation;
import static no.nav.tps.vedlikehold.provider.rs.security.user.GrantedAuthorityFunctions.toUserRole;
import static no.nav.tps.vedlikehold.provider.rs.security.user.UserRole.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
public class GrantedAuthorityFunctionsTest {

    @Test
    public void toStringRepresentationReturnsStringRepresentation() {
        GrantedAuthority ga = createGa("Role");

        String result = toStringRepresentation().apply(ga);

        assertThat(result, is(equalTo("Role")));
    }

    @Test
    public void toStringRepresentationReturnsNullWhenNull() {
        String result = toStringRepresentation().apply(null);

        assertThat(result, is(nullValue()));
    }

    @Test
    public void toUserRolesReturnsRoleFromRoles() {
        Map<String, UserRole> roles = new HashMap<String, UserRole>();
        roles.put("0000-GA-NORG_Skriv_T", ROLE_WRITE_T);
        roles.put("0000-GA-NORG_Les_T", ROLE_READ_T);
        roles.put("0000-GA-NORG_Skriv_Q", ROLE_WRITE_Q);
        roles.put("0000-GA-NORG_Les_Q", ROLE_READ_Q);

        UserRole resultWriteT = toUserRole(roles).apply(createGa("0000-GA-NORG_Skriv_T"));
        UserRole resultReadT = toUserRole(roles).apply(createGa("0000-GA-NORG_Les_T"));
        UserRole resultWriteQ = toUserRole(roles).apply(createGa("0000-GA-NORG_Skriv_Q"));
        UserRole resultReadQ = toUserRole(roles).apply(createGa("0000-GA-NORG_Les_Q"));

        assertThat(resultWriteT, is(ROLE_WRITE_T));
        assertThat(resultReadT, is(ROLE_READ_T));
        assertThat(resultWriteQ, is(ROLE_WRITE_Q));
        assertThat(resultReadQ, is(ROLE_READ_Q));
    }

    @Test
    public void toUserRoleReturnsNullWhenRoleNotInMap() {
        Map<String, UserRole> roles = singletonMap("0000-GA-NORG_Skriv_T", ROLE_WRITE_T);

        UserRole result = toUserRole(roles).apply(createGa("0000-GA-NORG_Les_T"));

        assertThat(result, is(nullValue()));
    }

    @Test
    public void toUserRoleReturnsNullWhenNoRoleMap() {
        UserRole result = toUserRole(Collections.<String, UserRole>emptyMap()).apply(createGa("0000-GA-NORG_Les_T"));

        assertThat(result, is(nullValue()));
    }

    @Test
    public void toUserRoleReturnsNullWhenGAIsNull() {
        Map<String, UserRole> roles = singletonMap("0000-GA-NORG_Skriv_T", ROLE_WRITE_T);

        UserRole result = toUserRole(roles).apply(null);

        assertThat(result, is(nullValue()));
    }

    private GrantedAuthority createGa(String role) {
        return new SimpleGrantedAuthority(role);
    }
}
