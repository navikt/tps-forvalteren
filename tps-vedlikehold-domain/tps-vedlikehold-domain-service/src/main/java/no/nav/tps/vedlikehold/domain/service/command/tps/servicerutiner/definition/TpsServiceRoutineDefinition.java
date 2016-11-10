package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition;

import no.nav.tps.vedlikehold.domain.service.command.tps.TpsParameter;
import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.AuthorisationStrategy;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.Transformer;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.List;
import java.util.Set;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class TpsServiceRoutineDefinition {

    private String name;
    private String internalName;    // (DisplayName)

    @JsonIgnore
    private Class<?> javaClass;

    private List<TpsParameter> parameters;

    @JsonIgnore
    private List<Transformer> transformers;

    private List<AuthorisationStrategy> securityServiceStrategies;

    private Set<String> requiredRoles;


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

    public Class<?> getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(Class<?> javaClass) {
        this.javaClass = javaClass;
    }

    public List<TpsParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<TpsParameter> parameters) {
        this.parameters = parameters;
    }

    @JsonIgnore
    public List<Transformer> getTransformers() {
        return transformers;
    }

    public void setTransformers(List<Transformer> transformers) {
        this.transformers = transformers;
    }

    public List<AuthorisationStrategy> getSecurityServiceStrategies() {
        return securityServiceStrategies;
    }

    public void setSecurityServiceStrategies(List<AuthorisationStrategy> securityServiceStrategies) {
        this.securityServiceStrategies = securityServiceStrategies;
    }

    public Set<String> getRequiredRoles() {
        return requiredRoles;
    }

    public void setRequiredRoles(Set<String> requiredRoles) {
        this.requiredRoles = requiredRoles;
    }
}
