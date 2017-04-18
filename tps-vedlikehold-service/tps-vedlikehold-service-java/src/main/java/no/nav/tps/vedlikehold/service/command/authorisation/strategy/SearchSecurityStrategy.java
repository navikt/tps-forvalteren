package no.nav.tps.vedlikehold.service.command.authorisation.strategy;

public interface SearchSecurityStrategy extends SecurityStrategy{
    boolean isAuthorised(String fnr);
}
