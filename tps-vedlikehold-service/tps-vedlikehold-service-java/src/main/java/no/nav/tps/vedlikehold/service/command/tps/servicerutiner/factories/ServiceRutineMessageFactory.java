package no.nav.tps.vedlikehold.service.command.tps.servicerutiner.factories;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@FunctionalInterface
public interface ServiceRutineMessageFactory {
    String createMessage(ServiceRutineMessageFactoryStrategy strategy);
}
