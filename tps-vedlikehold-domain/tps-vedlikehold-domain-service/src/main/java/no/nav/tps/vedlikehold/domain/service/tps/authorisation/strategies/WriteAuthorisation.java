package no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies;

/**
 * Created by F148888 on 09.11.2016.
 */
public class WriteAuthorisation implements AuthorisationStrategy{
    public static WriteAuthorisation writeAuthorisation(){
        return new WriteAuthorisation();
    }
}

