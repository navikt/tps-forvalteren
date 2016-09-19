package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition;

import java.util.List;

import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequest;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class TpsServiceRoutine {

    private String name;

    private String internalName;

    @JsonIgnore
    private Class<? extends TpsRequest> javaClass;

    private List<TpsServiceRoutineParameter> parameters;

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

    public Class<? extends TpsRequest> getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(Class<? extends TpsRequest> javaClass) {
        this.javaClass = javaClass;
    }

    public List<TpsServiceRoutineParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<TpsServiceRoutineParameter> parameters) {
        this.parameters = parameters;
    }
}
