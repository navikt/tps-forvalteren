package no.nav.tps.forvalteren.domain.service.environment;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class Environment {
    
    private Set<String> environments;
    private boolean productionMode;
    private Set<String> roles;
    
}
