package no.nav.tps.forvalteren.provider.rs.api.v1.config;

import java.util.Arrays;

import no.nav.tps.forvalteren.service.user.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;

public class TestUserDetails implements UserDetailsService {

    public static final String USERNAME = "test_username";
    public static final String DISPLAY_NAME = "test_dn";

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        if (USERNAME.equals(s)) {
            GrantedAuthority ga = UserRole.ROLE_ACCESS;
            GrantedAuthority servicerutiner = UserRole.ROLE_TPSF_SERVICERUTINER;

            LdapUserDetailsImpl.Essence ldapUserDetails = new LdapUserDetailsImpl.Essence();
            ldapUserDetails.setUsername(USERNAME);
            ldapUserDetails.setDn(DISPLAY_NAME);
            ldapUserDetails.setAuthorities(Arrays.asList(ga, servicerutiner));

            return ldapUserDetails.createUserDetails();
        }
        throw new UsernameNotFoundException(String.format("Finner ikke brukernavn %s", s));
    }
}
