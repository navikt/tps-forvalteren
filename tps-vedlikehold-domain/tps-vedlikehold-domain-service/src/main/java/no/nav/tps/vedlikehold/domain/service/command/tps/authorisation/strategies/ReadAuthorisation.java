package no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies;

/**
 * Created by F148888 on 09.11.2016.
 */
public class ReadAuthorisation {
    public static ReadAuthorisationStrategy readAuthorisation(){
        return new ReadAuthorisationStrategy();
    }
}
