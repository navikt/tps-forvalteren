package no.nav.tps.vedlikehold.service.command.servicerutiner.factories;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public class DefaultServiceRutineMessageFactoryStrategy implements ServiceRutineMessageFactoryStrategy {

    private Map<String, Object> parameters;

    public DefaultServiceRutineMessageFactoryStrategy(String serviceRutineName, Map<String, Object> parameters) {
        this.parameters = new HashMap<>(parameters);

        this.parameters.put("serviceRutinenavn", serviceRutineName);
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }
}
