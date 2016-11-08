package no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@FunctionalInterface
public interface AuthorisationStrategy {
    String getRequiredParamKeyName();
}
