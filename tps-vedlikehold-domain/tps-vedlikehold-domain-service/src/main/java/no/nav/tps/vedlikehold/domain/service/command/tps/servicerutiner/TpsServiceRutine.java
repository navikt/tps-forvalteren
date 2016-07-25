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
    private List<String> aksjonsKodes;

    @JacksonXmlProperty
    private List<TpsServiceRutineAttribute> attributes;

    /* Setters */

    public void setAttributes(List<TpsServiceRutineAttribute> attributes) {
        this.attributes = attributes;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public void setAksjonsKodes(List<String> aksjonsKodes) {
        this.aksjonsKodes = aksjonsKodes;
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

    public List<String> getAksjonsKodes() {
        return aksjonsKodes;
    }

    public List<TpsServiceRutineAttribute> getAttributes() {
        return attributes;
    }
}
