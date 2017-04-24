package no.nav.tps.forvalteren.service.command.authorisation.strategy;

public interface RestSecurityStrategy extends SecurityStrategy{
    boolean isAuthorised();
}
