package no.nav.tps.vedlikehold.domain.service.command.tps;

import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.AuthorisationStrategy;

import java.util.List;
import java.util.Set;

/**
 * Created by f148888 on 03.11.2016.
 */
public class TpsMessage {

    private String name;
    private String internalName;    // (DisplayName)

    private List<TpsParameter> parameters;
    private Set<String> requiredRoles;


    private List<AuthorisationStrategy> securityServiceStrategy;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public List<TpsParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<TpsParameter> parameters) {
        this.parameters = parameters;
    }

    public void setRequiredRoles(Set<String> roles){
        this.requiredRoles = roles;
    }

    public Set<String> getRequiredRoles(){
        return this.requiredRoles;
    }

    public List<AuthorisationStrategy> getSecuritySearchAuthorisationStrategies() {
        return securityServiceStrategy;
    }

    public void setSecurityServiceStrategy(List<AuthorisationStrategy> securityServiceStrategy) {
        this.securityServiceStrategy = securityServiceStrategy;
    }
}
