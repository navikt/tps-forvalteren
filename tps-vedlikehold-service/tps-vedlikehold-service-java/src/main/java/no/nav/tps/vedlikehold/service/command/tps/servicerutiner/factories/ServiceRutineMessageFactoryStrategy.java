package no.nav.tps.vedlikehold.service.command.tps.servicerutiner.factories;

import java.util.Map;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@FunctionalInterface
public interface ServiceRutineMessageFactoryStrategy {
    Map<String, Object> getParameters();
}
