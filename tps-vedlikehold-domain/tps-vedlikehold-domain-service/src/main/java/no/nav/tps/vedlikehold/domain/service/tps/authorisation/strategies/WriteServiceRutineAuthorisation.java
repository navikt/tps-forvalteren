package no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies;

public class WriteServiceRutineAuthorisation implements ServiceRutineAuthorisationStrategy {
    public static WriteServiceRutineAuthorisation writeAuthorisation(){
        return new WriteServiceRutineAuthorisation();
    }
}

