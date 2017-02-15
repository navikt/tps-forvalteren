package no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies;

import org.codehaus.jackson.annotate.JsonIgnoreType;

/**
 * Created by F148888 on 09.11.2016.
 */

@JsonIgnoreType
public class EgenAnsattAuthorisation implements AuthorisationStrategy{
    public static EgenAnsattAuthorisation egenAnsattAuthorisation(){
        return new EgenAnsattAuthorisation();
    }
}

