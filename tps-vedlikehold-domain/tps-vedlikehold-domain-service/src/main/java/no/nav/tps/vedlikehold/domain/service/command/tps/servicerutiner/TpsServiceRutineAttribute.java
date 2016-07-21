package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner;

import com.fasterxml.jackson.xml.annotate.JacksonXmlProperty;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

public class TpsServiceRutineAttribute {

    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String type;

    @JacksonXmlProperty(isAttribute = true)
    private String use;

    /* Setters */

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUse(String use) {
        this.use = use;
    }

    /* Getters */

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getUse() {
        return use;
    }
}
