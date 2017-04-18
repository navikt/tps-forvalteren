package no.nav.tps.vedlikehold.provider.rs.api.v1.config;

import no.nav.tps.vedlikehold.service.user.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;

import java.util.Collections;

public class TestUserDetails implements UserDetailsService {

    public static final String USERNAME = "test_username";
    public static final String DISPLAY_NAME = "test_dn";

    public static final String ACCESS_ROLE = "0000-GA-NORG_Skriv";


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        if (USERNAME.equals(s)) {
            GrantedAuthority ga = UserRole.ROLE_ACCESS;

            LdapUserDetailsImpl.Essence ldapUserDetails = new LdapUserDetailsImpl.Essence();
            ldapUserDetails.setUsername(USERNAME);
            ldapUserDetails.setDn(DISPLAY_NAME);
            ldapUserDetails.setAuthorities(Collections.singletonList(ga));

            return ldapUserDetails.createUserDetails();
        }
        throw new UsernameNotFoundException(String.format("Finner ikke brukernavn %s", s));
    }
}
