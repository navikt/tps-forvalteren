package no.nav.tps.forvalteren.domain.service.environment;

import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Environment {
    
    private Set<String> environments;
    private boolean productionMode;
    private Map<String, Boolean> roles;
    
}
