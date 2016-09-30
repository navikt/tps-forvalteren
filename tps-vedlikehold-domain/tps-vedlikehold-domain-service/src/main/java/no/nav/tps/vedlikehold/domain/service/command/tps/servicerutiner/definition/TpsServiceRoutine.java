package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition;

import java.util.List;

import no.nav.tps.vedlikehold.domain.service.command.tps.TpsParameter;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestServiceRoutine;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class TpsServiceRoutine {

    private String name;

    private String internalName;

    @JsonIgnore
    private Class<? extends TpsRequestServiceRoutine> javaClass;

    private List<TpsParameter> parameters;

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

    public Class<? extends TpsRequestServiceRoutine> getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(Class<? extends TpsRequestServiceRoutine> javaClass) {
        this.javaClass = javaClass;
    }

    public List<TpsParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<TpsParameter> parameters) {
        this.parameters = parameters;
    }
}
