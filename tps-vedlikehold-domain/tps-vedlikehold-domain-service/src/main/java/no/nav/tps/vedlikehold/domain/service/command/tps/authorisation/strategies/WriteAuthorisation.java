package no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies;

/**
 * Created by F148888 on 09.11.2016.
 */
public class WriteAuthorisation {
    public static WriteAuthorsationStrategy writeAuthorisation(String environment){
        return new WriteAuthorsationStrategy(environment);
    }
}

