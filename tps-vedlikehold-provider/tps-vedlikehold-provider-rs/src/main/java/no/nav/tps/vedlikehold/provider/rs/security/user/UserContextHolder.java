package no.nav.tps.vedlikehold.provider.rs.security.user;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * Created by G148232 on 22.06.2016.
 */
public interface UserContextHolder {
    String getDisplayName();
    String getUsername();

    Authentication getAuthentication();
    Boolean isAuthenticated();

    Collection<? extends GrantedAuthority> roles();

    void logout(HttpServletRequest request, HttpServletResponse response);
}
