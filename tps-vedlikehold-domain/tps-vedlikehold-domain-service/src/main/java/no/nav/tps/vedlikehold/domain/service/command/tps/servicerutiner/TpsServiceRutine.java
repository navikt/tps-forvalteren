package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner;

import com.fasterxml.jackson.xml.annotate.JacksonXmlProperty;

import java.util.List;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class TpsServiceRutine {

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String internalName;

    @JacksonXmlProperty
    private List<TpsServiceRutineParameter> parameters;

    /* Setters */

    public void setParameters(List<TpsServiceRutineParameter> parameters) {
        this.parameters = parameters;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public void setName(String name) {
        this.name = name;
    }

    /* Getters */

    public String getName() {
        return name;
    }

    public String getInternalName() {
        return internalName;
    }

    public List<TpsServiceRutineParameter> getParameters() {
        return parameters;
    }
}
