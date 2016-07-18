package no.nav.tps.vedlikehold.service.java.service.rutine.factories;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public interface ServiceRutineMessageFactory {
    String createMessage(ServiceRutineMessageFactoryStrategy strategy);
}
