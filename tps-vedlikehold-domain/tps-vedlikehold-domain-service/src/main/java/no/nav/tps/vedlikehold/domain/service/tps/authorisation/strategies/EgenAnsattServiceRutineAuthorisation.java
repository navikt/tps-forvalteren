package no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies;

import org.codehaus.jackson.annotate.JsonIgnoreType;


@JsonIgnoreType
public class EgenAnsattServiceRutineAuthorisation implements ServiceRutineAuthorisationStrategy {
    public static EgenAnsattServiceRutineAuthorisation egenAnsattAuthorisation(){
        return new EgenAnsattServiceRutineAuthorisation();
    }
}

