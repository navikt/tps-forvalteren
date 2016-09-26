package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition;

import java.util.List;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

public class TpsServiceRoutineParameter {

    private String name;
    private Type type;
    private String use;
    private List<?> values;

    public void setName(String name) {
        this.name = name;
    }

    public void setType(Type type) {
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

    public Type getType() {
        return type;
    }

    public String getUse() {
        return use;
    }

    public List<?> getValues() {
        return values;
    }

    public enum Type {
        STRING, DATE;
    }
}
