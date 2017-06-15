package no.nav.tps.forvalteren.provider.rs.security.mapping;

import no.nav.tps.forvalteren.service.user.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


public class RestAuthoritiesMapper implements GrantedAuthoritiesMapper {

    @Override
    public Collection<UserRole> mapAuthorities(Collection<? extends GrantedAuthority> collection) {
        List<String> userADRoles = collection.stream().filter(Objects::nonNull).map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        Set<UserRole> userRoles = new HashSet<>();
        for(UserRole roleEnum : UserRole.values()){
            for(String enumADRole : roleEnum.getADRolesForEnum()){
                if(userADRoles.contains(enumADRole)){
                    userRoles.add(roleEnum);
                }
            }
        }
        return userRoles;
    }

}