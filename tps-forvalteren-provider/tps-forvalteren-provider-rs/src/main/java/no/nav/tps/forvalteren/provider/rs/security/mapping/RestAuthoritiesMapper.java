package no.nav.tps.forvalteren.provider.rs.security.mapping;

import no.nav.tps.forvalteren.service.user.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

import java.util.Collection;
import java.util.Objects;

import static java.util.stream.Collectors.toSet;


public class RestAuthoritiesMapper implements GrantedAuthoritiesMapper {

    @Override
    public Collection<UserRole> mapAuthorities(Collection<? extends GrantedAuthority> collection) {
        return collection.stream()
                .filter(Objects::nonNull)
                .map(GrantedAuthority::getAuthority)
                .map(UserRole::getEnumFromName)
                .filter(Objects::nonNull)
                .collect(toSet());
    }

}