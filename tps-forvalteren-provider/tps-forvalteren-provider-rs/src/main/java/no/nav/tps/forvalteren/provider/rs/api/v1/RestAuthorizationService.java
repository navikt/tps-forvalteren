package no.nav.tps.forvalteren.provider.rs.api.v1;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

import no.nav.tps.forvalteren.service.command.exceptions.HttpUnauthorisedException;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import no.nav.tps.forvalteren.service.user.UserRole;

public class RestAuthorizationService {

    @Autowired
    UserContextHolder userContextHolder;

    public void assertAuthorized(UserRole... requiredRoles){
        Set<UserRole> roller = userContextHolder.getRoles();
        for(UserRole role : requiredRoles){
            if(!roller.contains(role)){
                throw new HttpUnauthorisedException("Ikke lov til å gjøre denne spørringen", "/");
            }
        }
    }
}
