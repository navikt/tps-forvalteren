package no.nav.tps.vedlikehold.domain.service.command.tps;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.List;

/**
 * Created by f148888 on 03.11.2016.
 */
public class TpsMessage {

    private String name;
    private String internalName;    // (DisplayName)

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

    public List<TpsParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<TpsParameter> parameters) {
        this.parameters = parameters;
    }
}
