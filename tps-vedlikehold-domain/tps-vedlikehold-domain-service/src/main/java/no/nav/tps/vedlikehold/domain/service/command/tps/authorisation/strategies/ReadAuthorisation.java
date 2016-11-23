package no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies;

import org.codehaus.jackson.annotate.JsonIgnoreType;

/**
 * Created by F148888 on 09.11.2016.
 */

@JsonIgnoreType
public class ReadAuthorisation implements AuthorisationStrategy{
    public static ReadAuthorisation readAuthorisation(){
        return new ReadAuthorisation();
    }
}
