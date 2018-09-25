package no.nav.tps.forvalteren.config;

import java.util.Arrays;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;

import no.nav.tps.forvalteren.service.user.UserRole;

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
