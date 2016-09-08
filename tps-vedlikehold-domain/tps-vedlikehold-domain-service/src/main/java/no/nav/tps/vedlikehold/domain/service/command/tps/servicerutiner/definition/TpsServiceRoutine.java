package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition;

import java.util.List;

import com.fasterxml.jackson.xml.annotate.JacksonXmlProperty;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class TpsServiceRoutine {

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String internalName;

    @JacksonXmlProperty
    private List<TpsServiceRoutineParameter> parameters;

    public void setParameters(List<TpsServiceRoutineParameter> parameters) {
        this.parameters = parameters;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getInternalName() {
        return internalName;
    }

    public List<TpsServiceRoutineParameter> getParameters() {
        return parameters;
    }
}
