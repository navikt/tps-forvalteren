package no.nav.tps.vedlikehold.provider.rs.api.v1.strategies.user;

import java.util.Set;
import javax.servlet.http.HttpSession;
import no.nav.tps.vedlikehold.provider.rs.security.user.UserContextHolder;
import no.nav.tps.vedlikehold.service.command.user.UserFactoryStrategy;
import org.springframework.security.core.GrantedAuthority;
import static java.util.stream.Collectors.toSet;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class UserContextUserFactoryStrategy implements UserFactoryStrategy {

    private UserContextHolder userContextHolder;
    private HttpSession session;

    public UserContextUserFactoryStrategy(UserContextHolder userContextHolder, HttpSession session) {
        this.userContextHolder = userContextHolder;
        this.session = session;
    }

    @Override
    public Set<String> getRoles() {
        return userContextHolder.getRoles()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(toSet());
    }

    @Override
    public String getToken() {
        return session.getId();
    }

    @Override
    public String getUsername() {
        return userContextHolder.getUsername();
    }

    @Override
    public String getDistinguishedName() {
        return userContextHolder.getDisplayName();
    }
}
