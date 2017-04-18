package no.nav.tps.vedlikehold.service.command.authorisation.strategy;

public interface RestSecurityStrategy extends SecurityStrategy{
    boolean isAuthorised();
}
