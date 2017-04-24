package no.nav.tps.forvalteren.service.command.authorisation.strategy;

public interface SearchSecurityStrategy extends SecurityStrategy{
    boolean isAuthorised(String fnr);
}
