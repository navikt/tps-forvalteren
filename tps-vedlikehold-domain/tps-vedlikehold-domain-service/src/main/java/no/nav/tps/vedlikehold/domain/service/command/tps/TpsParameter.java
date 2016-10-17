package no.nav.tps.vedlikehold.domain.service.command.tps;

import no.nav.tps.vedlikehold.domain.service.command.tps.TpsParameterType;

import java.util.List;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

public class TpsParameter {

    private String name;
    private TpsParameterType type;
    private String use;
    private List<?> values;

    public void setName(String name) {
        this.name = name;
    }

    public void setType(TpsParameterType type) {
        this.type = type;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public void setValues(List<?> values) {
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public TpsParameterType getType() {
        return type;
    }

    public String getUse() {
        return use;
    }

    public List<?> getValues() {
        return values;
    }

}
